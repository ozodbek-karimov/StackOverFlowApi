package pl.ozodbek.stackoverflowapi.view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.stackoverflowapi.databinding.AnswerLayoutBinding
import pl.ozodbek.stackoverflowapi.model.Answer
import pl.ozodbek.stackoverflowapi.util.viewBinding

class AnswerAdapter :
    ListAdapter<Answer, AnswerAdapter.AdapterViewHolder>(AnswerDiffCallback()) {

    private var itemClickListener: ((Answer) -> Unit)? = null

    fun setItemClickListener(listener: (Answer) -> Unit) {
        itemClickListener = listener
    }


    class AdapterViewHolder(private val binding: AnswerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            remoteData: Answer,
            clickListener: ((Answer) -> Unit)?,
        ) {
            binding.itemAnswer.text = remoteData.toString()

            binding.root.setOnClickListener {
                clickListener?.invoke(remoteData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(parent.viewBinding(AnswerLayoutBinding::inflate))
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val remoteData = getItem(position)
        holder.bind(remoteData, itemClickListener)
    }

    private class AnswerDiffCallback : DiffUtil.ItemCallback<Answer>() {
        override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
            return oldItem.answerId == newItem.answerId
        }

        override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
            return oldItem == newItem
        }
    }
}
