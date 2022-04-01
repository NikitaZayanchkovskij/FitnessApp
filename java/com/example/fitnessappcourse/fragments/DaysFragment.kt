package com.example.fitnessappcourse.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessappcourse.R
import com.example.fitnessappcourse.adapters.DayModel
import com.example.fitnessappcourse.adapters.DaysAdapter
import com.example.fitnessappcourse.adapters.ExerciseModel
import com.example.fitnessappcourse.databinding.FragmentDaysBinding
import com.example.fitnessappcourse.utils.DialogManager
import com.example.fitnessappcourse.utils.FragmentManager
import com.example.fitnessappcourse.utils.MainViewModel

class DaysFragment : Fragment(), DaysAdapter.Listener {
    private lateinit var adapter: DaysAdapter
    private lateinit var binding: FragmentDaysBinding
    private var ab: ActionBar? = null
    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            binding = FragmentDaysBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentDay = 0

        initRecyclerView()
    }

    /**
     * We need this function to set our reset days menu to the upper right corner of the screen.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.main_menu, menu)
    }

    /**
     * This function is our on click listener of the reset days menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.reset){

            val listener = object : DialogManager.Listener{
                override fun onClick() {
                    model.pref?.edit()?.clear()?.apply()
                    adapter.submitList(fillDaysArray())
                }
            }

            DialogManager.showDialog(
                activity as AppCompatActivity,
                R.string.reset_days_message,
                listener
            )

        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() = with(binding){
        adapter = DaysAdapter(this@DaysFragment)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.days_list)

        rcViewDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rcViewDays.adapter = adapter
        adapter.submitList(fillDaysArray())
    }

    /** This function is filling our DayModel with the exercises and setting the boolean value
     * if our day is already completed or not.
     */
    private fun fillDaysArray(): List<DayModel>{
        val tempArray = ArrayList<DayModel>()
        var daysDoneCounter = 0

        resources.getStringArray(R.array.day_exercises).forEach {
            model.currentDay++

            val exCounter = it.split(",").size

            tempArray.add(DayModel(it, 0, model.getExerciseCount() == exCounter))
        }
        binding.progressBar.max = tempArray.size

        tempArray.forEach {
            if (it.isDone){
                daysDoneCounter++
            }
        }
        updateLeftDaysUI(tempArray.size - daysDoneCounter, tempArray.size)

        return tempArray
    }

    /** In this function we're updating progress bar at the main Fragment and our text, how much
     * is still left to do.
     */
    private fun updateLeftDaysUI(leftDays: Int, days: Int) = with(binding){
        val lDays = getString(R.string.left) + " $leftDays " + getString(R.string.days_left)
        tvDaysLeft.text = lDays
        progressBar.progress = days - leftDays
    }

    private fun fillExerciseList(dayModel: DayModel){
        val tempList = ArrayList<ExerciseModel>()
        dayModel.exercises.split(",").forEach{
            val exerciseList = resources.getStringArray(R.array.exercises)
            val exercise = exerciseList[it.toInt()]
            val exerciseArray = exercise.split("|")

            tempList.add(ExerciseModel(exerciseArray[0], exerciseArray[1], false, exerciseArray[2]))
        }
        model.mutableListExercise.value = tempList
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }

    /** This function is our on click listener for the day, that user pressed in our
     * Recycler View. If he pressed Day 1 - he will see exercises from Day 1, if it's Day 2 -
     * exercises from Day 2 etc.
     */
    override fun onClick(dayModel: DayModel) {

        if (!dayModel.isDone) {
            fillExerciseList(dayModel)

            model.currentDay = dayModel.dayNumber

            FragmentManager.setFragment(
                ExerciseListFragment.newInstance(), activity as AppCompatActivity)

        } else {

            val listener = object : DialogManager.Listener{
                override fun onClick() {
                    model.savePref(dayModel.dayNumber.toString(), 0)

                    fillExerciseList(dayModel)

                    model.currentDay = dayModel.dayNumber

                    FragmentManager.setFragment(
                        ExerciseListFragment.newInstance(), activity as AppCompatActivity)
                }
            }

            DialogManager.showDialog(
                activity as AppCompatActivity,
                R.string.reset_day_message,
                listener
            )
        }

    }

}