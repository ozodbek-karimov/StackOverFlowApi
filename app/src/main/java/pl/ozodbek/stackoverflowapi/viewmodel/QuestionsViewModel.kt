package com.devtides.stackoverflowquery.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.ozodbek.stackoverflowapi.model.Question
import pl.ozodbek.stackoverflowapi.model.ResponseWrapper
import pl.ozodbek.stackoverflowapi.model.StackOverFlowService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionsViewModel : ViewModel() {

    val questionsResponse = MutableLiveData<List<Question>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    var page = 0

    fun getNextPage() {
        page++
        getQuestions()
    }

    fun getFirstPage(){
        page = 1
        getQuestions()
    }

    private fun getQuestions() {
        StackOverFlowService.api.getQuestions(page)
            .enqueue(object : Callback<ResponseWrapper<Question>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<Question>>,
                    response: Response<ResponseWrapper<Question>>
                ) {
                    if (response.isSuccessful) {
                        val question = response.body()

                        question?.let {
                            questionsResponse.value = question.items
                            loading.value = false
                            error.value = null
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<Question>>, t: Throwable) {
                    t.localizedMessage?.let { onError(it) }
                }

            })
    }

    private fun onError(message: String) {
        error.value = message
        loading.value = false
    }
}