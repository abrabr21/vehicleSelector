package com.vehicle.selector.presentation.configuration_flow

import androidx.fragment.app.Fragment
import com.vehicle.selector.core.StateOwner

interface MainCarConfigurationStateOwner : StateOwner {

    companion object {

        fun Fragment.findStateOwner(): MainCarConfigurationStateOwner {
            val owner = when (parentFragment) {
                null -> activity as? MainCarConfigurationStateOwner
                else -> (parentFragment as? MainCarConfigurationStateOwner)
                    ?: requireParentFragment().findStateOwner()
            }
            return owner ?: error("Can not find StateOwner from $this")
        }
    }
}