package com.example.fitnessappcourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.fitnessappcourse.fragments.DaysFragment
import com.example.fitnessappcourse.utils.FragmentManager
import com.example.fitnessappcourse.utils.MainViewModel

/**
 * To run the app just press Shift + F10 or Play icon in the upper right corner.
 */
class MainActivity : AppCompatActivity() {
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        model.pref = getSharedPreferences("main", MODE_PRIVATE)

        FragmentManager.setFragment(DaysFragment.newInstance(), this)
    }

    /** We need this function for the next purpose:
     * If user presses Back button on his smartphone - application will close current Fragment,
     * not the whole Activity.
     *
     * For example:
     * User opened one of the days from the list to see exercises of this day.
     * Then decides to close the list - our application will return to the main fragment,
     * where user can see all days to do.
     */
    override fun onBackPressed() {
        if (FragmentManager.currentFragment is DaysFragment) {
            super.onBackPressed()
        } else {
            FragmentManager.setFragment(DaysFragment.newInstance(), this)
        }
    }

}