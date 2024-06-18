package com.vehicle.selector.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.vehicle.selector.R

class VehicleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val host = NavHostFragment.create(R.navigation.vehicle_chosser_grapgh)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_parent_host_fragment, host).setPrimaryNavigationFragment(host)
                .commit()
        }
    }
}