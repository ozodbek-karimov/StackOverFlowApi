package pl.ozodbek.stackoverflowapi.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.stackoverflowapi.viewmodel.QuestionsViewModel
import pl.ozodbek.stackoverflowapi.util.oneliner_viewbinding.viewBinding
import pl.ozodbek.stackoverflowapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val questionsAdapter by lazy { QuestionsAdapter() }
    private val viewModel: QuestionsViewModel by viewModels()
    private val lm = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.swipeLayout.setOnRefreshListener {

            binding.swipeLayout.isRefreshing = true
            questionsAdapter.clearQuestions()
            viewModel.getFirstPage()
            binding.loadingView.visibility = View.INVISIBLE
            binding.questionsList.visibility = View.GONE

        }
        binding.questionsList.apply {
            layoutManager = lm
            adapter = questionsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val childCount = questionsAdapter.itemCount
                        val lastPosition = lm.findLastCompletelyVisibleItemPosition()
                        if (childCount - 1 == lastPosition && binding.loadingView.visibility == View.GONE) {
                            binding.loadingView.visibility = View.VISIBLE
                            viewModel.getNextPage()
                        }
                    }
                }
            })

            questionsAdapter.setItemClickListener { answer ->
                startActivity(DetailActivity.getIntent(this@MainActivity, answer))
            }
        }

        observeViewModel()
        viewModel.getNextPage()

    }


    private fun observeViewModel() {
        viewModel.questionsResponse.observe(this) { items ->
            items?.let {
                binding.questionsList.visibility = View.VISIBLE
                questionsAdapter.addQuestions(it)
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            binding.listError.visibility = if (errorMsg == null) View.GONE else View.VISIBLE
            binding.listError.text = "Error\n$errorMsg"
        }

        viewModel.loading.observe(this) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.questionsList.visibility = View.GONE
                } else
                    binding.swipeLayout.isRefreshing = false
            }
        }
    }
}