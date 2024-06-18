package com.vehicle.selector

import android.app.Application
import com.vehicle.selector.AppDIModule.appModule
import com.vehicle.selector.data.db.VehicleDB
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class VehicleSelectorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VehicleSelectorApp)
            modules(appModule)
        }
    }
}