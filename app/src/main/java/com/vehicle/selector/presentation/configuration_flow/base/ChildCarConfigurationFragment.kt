package com.vehicle.selector.presentation.configuration_flow.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.vehicle.selector.core.BaseBindingFragment
import com.vehicle.selector.core.parentViewModel
import com.vehicle.selector.presentation.configuration_flow.CarConfigurationEvent
import com.vehicle.selector.presentation.configuration_flow.CarConfigurationSharedAction
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationState
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationStateOwner.Companion.findStateOwner
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class ChildCarConfigurationFragment<Binding : ViewBinding>(@LayoutRes contentLayoutId: Int = 0) :
    BaseBindingFragment<Binding>(contentLayoutId) {

    val mainCarConfigurationViewModel: MainCarConfigurationViewModel by
    parentViewModel<MainCarConfigurationViewModel> {
        findStateOwner()
    }

    protected abstract fun onSharedStateChanged(state: MainCarConfigurationState)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSharedState()
        observeMainVehicleSharedAction()
    }

    private fun observeMainVehicleSharedAction() {
        mainCarConfigurationViewModel.sharedAction
            .onEach { action ->
                when (action) {
                    is CarConfigurationSharedAction.OnContinueClicked -> onContinueClick()
                    is CarConfigurationSharedAction.OnGoBackClicked -> onGoBackClick()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    protected open fun onGoBackClick() {
        sendEvent(CarConfigurationEvent.OnPreviousStepClick)
    }

    protected open fun onContinueClick() {
        sendEvent(CarConfigurationEvent.OnNextStepClick)
    }

    private fun observeSharedState() = mainCarConfigurationViewModel.state
        .onEach { state -> onSharedStateChanged(state) }
        .launchIn(viewLifecycleOwner.lifecycleScope)


    protected open fun sendEvent(event: CarConfigurationEvent) =
        mainCarConfigurationViewModel.sendEvent(event)
}