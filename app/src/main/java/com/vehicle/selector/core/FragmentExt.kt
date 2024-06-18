package com.vehicle.selector.core

import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

@MainThread
inline fun <reified V : ViewModel> Fragment.parentViewModel(
    qualifier: Qualifier? = null,
    noinline stateOwnerProvider: () -> StateOwner,
): Lazy<V> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val stateOwner = stateOwnerProvider()
        getViewModel(
            qualifier,
            { stateOwner.stateViewModelStoreOwner },
            { stateOwner.creationExtras },
            null
        )
    }
}

interface StateOwner {
    val stateViewModelStoreOwner: ViewModelStoreOwner
    val creationExtras: CreationExtras
}