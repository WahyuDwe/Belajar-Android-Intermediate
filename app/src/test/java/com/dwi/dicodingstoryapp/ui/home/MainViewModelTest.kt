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
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
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
    private lateinit var storyRepository: StoryDataRepository

    @Mock
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel()
    }

    private val dummyToken = "authentication_token"

    @Test
    fun `Get Get Stories successfully Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStory()
        val dataStory: PagingData<StoryResult> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryResult>>()
        expectedStory.value = dataStory
        Mockito.`when`(storyRepository.getStories(dummyToken)).thenReturn(expectedStory)

        val actualStory: PagingData<StoryResult> = mainViewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = mainDispatcherRules.testDispatcher,
            mainDispatcher = mainDispatcherRules.testDispatcher
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
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