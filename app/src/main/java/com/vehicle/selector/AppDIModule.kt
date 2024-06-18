package com.vehicle.selector

import com.vehicle.selector.data.dataModule
import com.vehicle.selector.domain.domainModule
import com.vehicle.selector.presentation.presentationModule
import org.koin.dsl.module

object AppDIModule {

    val appModule =
        module {
            includes(
                presentationModule,
                domainModule,
                dataModule
            )
        }
}