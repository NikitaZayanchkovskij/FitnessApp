package com.example.fitnessappcourse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessappcourse.R
import com.example.fitnessappcourse.databinding.DaysListItemBinding

class DaysAdapter(
    var listener: Listener): ListAdapter<DayModel, DaysAdapter.DayHolder>(MyComparator()) {

    class DayHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = DaysListItemBinding.bind(view)

        fun setData(dayModel: DayModel, listener: Listener) = with(binding){
            val dayNumber = root.context.getString(R.string.day) + " ${adapterPosition + 1}"
            val exerciseCounter = dayModel.exercises.split(",")
            var exCounterWithoutNull = exerciseCounter.size

            for (index in exerciseCounter){
                if (index == "0"){
                    exCounterWithoutNull--
                }
            }

            val exercisesAmountToDo = root.context.getString(R.string.exercises_amount) + " " +
                    exCounterWithoutNull.toString()

            tvName.text = dayNumber
            tvExCounter.text = exercisesAmountToDo
            checkBox.isChecked = dayModel.isDone

            itemView.setOnClickListener {
                listener.onClick(dayModel.copy(dayNumber = adapterPosition + 1))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.days_list_item, parent, false)

        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }


    class MyComparator: DiffUtil.ItemCallback<DayModel>(){

        override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener{
        fun onClick(dayModel: DayModel)
    }

}