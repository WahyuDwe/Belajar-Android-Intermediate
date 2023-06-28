package com.dwi.dicodingstoryapp.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dwi.dicodingstoryapp.data.source.remote.response.StoryResult
import com.dwi.dicodingstoryapp.network.ApiService

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, StoryResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResult> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(
                token,
                page,
                params.loadSize)
            val data = responseData.story
            LoadResult.Page(
                data = data,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResult>): Int? {
        return state.anchorPosition?.let {anchorPoistion ->
            val anchorPage = state.closestPageToPosition(anchorPoistion)
            anchorPage?.prevKey?:anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}