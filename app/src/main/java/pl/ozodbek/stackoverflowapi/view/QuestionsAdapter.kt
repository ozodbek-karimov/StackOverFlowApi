package pl.ozodbek.stackoverflowapi.view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.stackoverflowapi.databinding.QuestionLayoutBinding
import pl.ozodbek.stackoverflowapi.model.Question
import pl.ozodbek.stackoverflowapi.model.convertTitle
import pl.ozodbek.stackoverflowapi.model.getDate
import pl.ozodbek.stackoverflowapi.util.viewBinding

class QuestionsAdapter :
    ListAdapter<Question, QuestionsAdapter.AdapterViewHolder>(QuestionDiffCallback()) {


    private var itemClickListener: ((Question) -> Unit)? = null

    fun setItemClickListener(listener: (Question) -> Unit) {
        itemClickListener = listener
    }

    class AdapterViewHolder(private val binding: QuestionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question, clickListener: ((Question) -> Unit)?) {
            binding.itemTitle.text = convertTitle(question.title)
            binding.itemScore.text = question.score
            binding.itemDate.text = getDate(question.date)

            binding.itemLayout.setOnClickListener {
                clickListener?.invoke(question)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(parent.viewBinding(QuestionLayoutBinding::inflate))
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val remoteData = getItem(position)
        holder.bind(remoteData, itemClickListener)
    }

    fun addQuestions(newQuestions: List<Question>) {
        val currentLength = currentList.size
        submitList(currentList + newQuestions)
        notifyItemRangeInserted(currentLength, newQuestions.size)
    }

    fun clearQuestions() {
        submitList(emptyList())
    }

    private class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.questionId == newItem.questionId
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
    }
}

