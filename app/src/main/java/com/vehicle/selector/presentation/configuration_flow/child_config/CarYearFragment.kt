package com.vehicle.selector.presentation.configuration_flow.child_config

import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.vehicle.selector.R
import com.vehicle.selector.databinding.FragmentYearConfigurationBinding
import com.vehicle.selector.presentation.configuration_flow.CarConfigurationEvent
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationState
import com.vehicle.selector.presentation.configuration_flow.base.ChildCarConfigurationFragment
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyModelController
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyYearController

class CarYearFragment :
    ChildCarConfigurationFragment<FragmentYearConfigurationBinding>(R.layout.fragment_year_configuration) {

    private val controller: EpoxyYearController by lazy {
        EpoxyYearController(
            onItemClick = {
                mainCarConfigurationViewModel.sendEvent(CarConfigurationEvent.OnYearSelected(it))
            }
        )
    }

    override fun bind(view: View): FragmentYearConfigurationBinding =
        FragmentYearConfigurationBinding.bind(view)

    override fun initialUISetup(
        binding: FragmentYearConfigurationBinding,
        savedInstanceState: Bundle?
    ) {
        super.initialUISetup(binding, savedInstanceState)
        with(requireBinding()) {
            epoxyRecycleView.setController(controller)
            epoxyRecycleView.addItemDecoration(
                EpoxyItemSpacingDecorator(
                    resources.getDimensionPixelSize(R.dimen.default_margin)
                )
            )
        }
    }

    override fun onSharedStateChanged(state: MainCarConfigurationState) {
        with(requireBinding()) {
            tvBrandSelected.text = getString(R.string.brand_placeholder, state.carEntity.brand)
            tvModelSelected.text = getString(R.string.model_placeholder, state.carEntity.model)
        }
        state.yearDetails?.let {
            controller.setData(it, state.carEntity)
        }
    }
}