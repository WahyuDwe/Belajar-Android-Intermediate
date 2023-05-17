package com.dwi.dicodingstoryapp.utils

import com.dwi.dicodingstoryapp.data.source.remote.response.StoryResult

object DataDummy {
    fun generateDummyStory(): List<StoryResult> {
        val storiesList = ArrayList<StoryResult>()
        for (i in 0..10) {
            val stories = StoryResult(
                "https://plus.unsplash.com/premium_photo-1683880731495-ae0f4bf18c7e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80",
                "2021-10-10",
                "Person $i",
                "Description $i",
            )
            storiesList.add(stories)
        }
        return storiesList
    }
}