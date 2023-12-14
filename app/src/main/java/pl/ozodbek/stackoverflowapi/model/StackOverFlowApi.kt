package pl.ozodbek.stackoverflowapi.model

import pl.ozodbek.stackoverflowapi.model.Answer
import pl.ozodbek.stackoverflowapi.model.Question
import pl.ozodbek.stackoverflowapi.model.ResponseWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackOverFlowApi {

    @GET("/2.2/questions?order=desc&sort=votes&tagged=android&site=stackoverflow")
    fun getQuestions(@Query("page") page: Int)
            : Call<ResponseWrapper<Question>>

    @GET("/2.2/questions/{id}/answers?order=desc&sort=votes&site=stackoverflow")
    fun getAnswers(@Path("id") questionId: Int, @Query("page") page: Int)
            : Call<ResponseWrapper<Answer>>


}