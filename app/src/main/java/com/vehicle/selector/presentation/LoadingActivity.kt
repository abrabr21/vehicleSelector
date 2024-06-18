package com.vehicle.selector.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vehicle.selector.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        lifecycleScope.launch {
            delay(LOADING_DELAY)
            this@LoadingActivity.startActivity(Intent(this@LoadingActivity, VehicleActivity::class.java))
            finish()
        }
    }

    companion object {
        const val LOADING_DELAY = 2000L
    }
}