package pl.ozodbek.stackoverflowapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.ozodbek.stackoverflowapi.model.Answer
import pl.ozodbek.stackoverflowapi.model.ResponseWrapper
import pl.ozodbek.stackoverflowapi.model.StackOverFlowService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel(){


    val answerResponse = MutableLiveData<List<Answer>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    var questionId = 0
    var page = 0

    fun getNextPage(questionId: Int?) {
        questionId?.let {
            this.questionId = it
            page++
            getAnswer()
        }
    }



    private fun getAnswer() {
        StackOverFlowService.api.getAnswers(questionId, page)
            .enqueue(object : Callback<ResponseWrapper<Answer>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<Answer>>,
                    response: Response<ResponseWrapper<Answer>>
                ) {
                   val answers = response.body()
                    answers?.let {
                        answerResponse.value = it.items
                        loading.value = false
                        error.value = null
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<Answer>>, t: Throwable) {
                    t.localizedMessage?.let { onError(it) }
                }

            })
    }

    private fun onError(message: String) {
        error.value = message
        loading.value = false
    }
}