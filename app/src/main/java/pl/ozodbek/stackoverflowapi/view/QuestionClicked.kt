package pl.ozodbek.stackoverflowapi.view

import pl.ozodbek.stackoverflowapi.model.Question

interface QuestionClicked {

    fun onQuestionClicked(question: Question)

}