package com.example.fitnessappcourse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessappcourse.R
import com.example.fitnessappcourse.adapters.ExerciseAdapter
import com.example.fitnessappcourse.databinding.ExerciseListFragmentBinding
import com.example.fitnessappcourse.utils.FragmentManager
import com.example.fitnessappcourse.utils.MainViewModel

class ExerciseListFragment : Fragment() {
    private lateinit var binding: ExerciseListFragmentBinding
    private lateinit var adapter: ExerciseAdapter
    private var ab: ActionBar? = null
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            binding = ExerciseListFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        model.mutableListExercise.observe(viewLifecycleOwner){
            for (index in 0 until model.getExerciseCount()){
                it[index] = it[index].copy(isDone = true)
        }
            adapter.submitList(it)
        }
    }

    private fun init() = with(binding){
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.exercises_to_do)

        adapter = ExerciseAdapter()
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter

        bStart.setOnClickListener {
            FragmentManager.setFragment(WaitingFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    override fun onDetach() {
        super.onDetach()

    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }

}