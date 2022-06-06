package com.abhi41.jetpackpaging3.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.abhi41.jetpackpaging3.data.database.UnsplashDatabase
import com.abhi41.jetpackpaging3.data.remote.UnsplashApi
import com.abhi41.jetpackpaging3.model.UnsplashImage
import com.abhi41.jetpackpaging3.model.UnsplashRemoteKeys
import com.abhi41.jetpackpaging3.utils.Constants.ITEMS_PER_PAGE
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UnsplashRemoteMediator(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
) : RemoteMediator<Int, UnsplashImage>() {

    private val unsplashImageDao = unsplashDatabase.unsplashImageDao()
    private val unsplashRemoteKeysDao = unsplashDatabase.unsplashRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImage>
    ): MediatorResult {
        /*
            In this we need to calculate current page and next and previous page as well
            based on their value we can tell our mediator which page we request next
            whenever we need more data.
         */
         try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    //this will trigger when we first time make request to server
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1 //if its null then default value is 1
                }
                LoadType.PREPEND -> {
                    //this will trigger at the start of the paging data
                    val remoteKeys = getRemoteKeyForFirstItem(state)//we will get first hero id
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            //if prevPage is null then we return MediatorResult to our load fun
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage //if prevPage is not null then return prevPage value
                }
                LoadType.APPEND -> { //query exhausted
                    //this will trigger at the end of the paging data
                    val remoteKeys = getRemoteKeyForLastItem(state) //we will get last hero id
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            //if nextPage is null then we return MediatorResult to our load fun
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage //if nextPage is not null then return nextPage
                }
            }

            val response = unsplashApi.getAllImages(page = currentPage, per_page = ITEMS_PER_PAGE)
            val endOfPaginationReached = response.isEmpty()

            //we calculate prevPage and nextPage value on the basis of current page
            //if current page is 1 then prevPage should be null else prevPage value will be currentPage -1
            val prevPage = if (currentPage == 1) null else currentPage - 1
            /*  if endOfPaginationReached true then we have no longer value to fetch from api
                 & hence nextPage wil null else current page +1
             */
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            /*
                 1) this withTransaction function will allows us to execute multiple queries
                 2) withTransaction: Room will only perform at most one transaction at a time,
                 additional transactions are queued and executed on a first come, first serve order.
             */
            unsplashDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    unsplashImageDao.deleteAllImage()
                    unsplashRemoteKeysDao.deleteAllRemoteKeys()
                }
                //val keys contains multiple list of unsplashRemote keys
                val keys = response.map { unsplashImage ->
                    //we are creating multiple UnsplashRemoteKeys object based on unsplashImage.id
                    UnsplashRemoteKeys(
                        id = unsplashImage.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                unsplashRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                unsplashImageDao.addImage(images = response)
            }
            //this means query exhausted
          return  MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }

    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                unsplashRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                unsplashRemoteKeysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                unsplashRemoteKeysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

}