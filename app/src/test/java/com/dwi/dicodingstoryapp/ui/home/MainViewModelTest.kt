package com.dwi.dicodingstoryapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository
import com.dwi.dicodingstoryapp.data.source.remote.response.StoryResult
import com.dwi.dicodingstoryapp.ui.home.adapter.MainAdapter
import com.dwi.dicodingstoryapp.utils.DataDummy
import com.dwi.dicodingstoryapp.utils.MainDispatcherRule
import com.dwi.dicodingstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyDataRepository: StoryDataRepository


    private val token = DataDummy.generateDummyToken()

    @Test
    fun `When Success Get Story Should Return Data and Not Null`() =
        mainDispatcherRules.runBlockingTest {
            val dummyStories = DataDummy.generateDummyStory()
            val data = StoryPagingSource.snapshot(dummyStories)
            val expectedStory = MutableLiveData<PagingData<StoryResult>>()
            expectedStory.value = data
            Mockito.`when`(storyDataRepository.getStories(token)).thenReturn(expectedStory)

            val mainViewModel = MainViewModel(storyDataRepository)
            val actualStories: PagingData<StoryResult> =
                mainViewModel.getStories(token).getOrAwaitValue()

            val differ = AsyncPagingDataDiffer(
                diffCallback = MainAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(actualStories)

            assertNotNull(differ.snapshot())
            assertEquals(dummyStories.size, differ.snapshot().size)
            assertEquals(dummyStories[0], differ.snapshot()[0])
        }

    @Test
    fun `when Get Story Empty Should Return No Data`() = mainDispatcherRules.runBlockingTest {
        val data: PagingData<StoryResult> = PagingData.from(emptyList())
        val expectedStories = MutableLiveData<PagingData<StoryResult>>()
        expectedStories.value = data
        Mockito.`when`(storyDataRepository.getStories(token)).thenReturn(expectedStories)

        val mainViewModel = MainViewModel(storyDataRepository)
        val actualStories: PagingData<StoryResult> =
            mainViewModel.getStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryResult>>>() {
    companion object {
        fun snapshot(items: List<StoryResult>): PagingData<StoryResult> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResult>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryResult>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}