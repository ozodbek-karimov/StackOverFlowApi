package pl.ozodbek.stackoverflowapi.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import pl.ozodbek.stackoverflowapi.model.Question
import pl.ozodbek.stackoverflowapi.model.convertTitle
import pl.ozodbek.stackoverflowapi.model.getDate
import com.devtides.stackoverflowquery.viewmodel.DetailViewModel
import pl.ozodbek.stackoverflowapi.databinding.ActivityDeatilBinding
import pl.ozodbek.stackoverflowapi.util.oneliner_viewbinding.viewBinding

class DetailActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDeatilBinding::inflate)
    private val answerAdapter by lazy { AnswerAdapter() }
    private val viewModel: DetailViewModel by viewModels()
    private val lm = LinearLayoutManager(this)


    companion object {
        const val PARAM_QUESTION = "param_question"

        fun getIntent(context: Context, question: Question) =
            Intent(context, DetailActivity::class.java)
                .putExtra(PARAM_QUESTION, question)
    }

    var question: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        question = intent.extras?.getParcelable(PARAM_QUESTION)

        if (question == null) {
            finish()
        }

        binding.answersList.apply {
            adapter = answerAdapter
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val childCount = answerAdapter.itemCount
                        val lastPosition = lm.findLastCompletelyVisibleItemPosition()
                        if (childCount - 1 == lastPosition && binding.loadingView.visibility == View.GONE) {
                            binding.loadingView.visibility = View.VISIBLE
                            viewModel.getNextPage(question!!.questionId)
                        }
                    }
                }
            })
        }

        observeViewModel()
        populateUI()
        viewModel.getNextPage(question!!.questionId)
    }

    private fun observeViewModel() {
        viewModel.answerResponse.observe(this) { items ->
            items?.let {
                binding.answersList.visibility = View.VISIBLE
                answerAdapter.submitList(it)
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            binding.listError.visibility = if (errorMsg == null) View.GONE else View.VISIBLE
            binding.listError.text = errorMsg
        }

        viewModel.loading.observe(this) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.answersList.visibility = View.GONE
                }
            }
        }
    }

    private fun populateUI() {
        binding.questionTitle.text = convertTitle(question?.title)
        binding.questionScore.text = question?.score
        binding.questionDate.text = getDate(question?.date)
    }
}