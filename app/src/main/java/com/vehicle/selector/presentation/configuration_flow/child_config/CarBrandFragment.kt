package com.vehicle.selector.presentation.configuration_flow.child_config

import android.os.Bundle
import android.view.View
import android.view.View.OnScrollChangeListener
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.vehicle.selector.R
import com.vehicle.selector.databinding.FragmentBrandConfigurationBinding
import com.vehicle.selector.presentation.configuration_flow.CarConfigurationEvent
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationState
import com.vehicle.selector.presentation.configuration_flow.base.ChildCarConfigurationFragment
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyBrandController

class CarBrandFragment :
    ChildCarConfigurationFragment<FragmentBrandConfigurationBinding>(R.layout.fragment_brand_configuration) {

    private val controller: EpoxyBrandController by lazy {
        EpoxyBrandController(
            onItemClick = {
                mainCarConfigurationViewModel.sendEvent(CarConfigurationEvent.OnBrandSelected(it))
            },
            onNextPageClick = {
                mainCarConfigurationViewModel.sendEvent(CarConfigurationEvent.BrandPagination.BrandNavigationNextPage)
            },
            onPreviousPageClick = {
                mainCarConfigurationViewModel.sendEvent(CarConfigurationEvent.BrandPagination.BrandNavigationBackPage)
            }
        )
    }

    override fun bind(view: View): FragmentBrandConfigurationBinding =
        FragmentBrandConfigurationBinding.bind(view)

    override fun initialUISetup(
        binding: FragmentBrandConfigurationBinding,
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
        state.brandDetails?.let {
            controller.setData(it, state.carEntity)
        }
    }
}