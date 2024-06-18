package com.vehicle.selector.presentation.configuration_flow.child_config

import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.vehicle.selector.R
import com.vehicle.selector.databinding.FragmentModelConfigurationBinding
import com.vehicle.selector.presentation.configuration_flow.CarConfigurationEvent
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationState
import com.vehicle.selector.presentation.configuration_flow.base.ChildCarConfigurationFragment
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyModelController

class CarModelFragment :
    ChildCarConfigurationFragment<FragmentModelConfigurationBinding>(R.layout.fragment_model_configuration) {

    private val controller: EpoxyModelController by lazy {
        EpoxyModelController(
            onItemClick = {
                mainCarConfigurationViewModel.sendEvent(CarConfigurationEvent.OnModelSelected(it))
            },
            onNextPageClick = {
                mainCarConfigurationViewModel.sendEvent(CarConfigurationEvent.ModelPagination.ModelNavigationNextPage)
            },
            onPreviousPageClick = {
                mainCarConfigurationViewModel.sendEvent(CarConfigurationEvent.ModelPagination.ModelNavigationBackPage)
            }
        )
    }

    override fun bind(view: View): FragmentModelConfigurationBinding =
        FragmentModelConfigurationBinding.bind(view)

    override fun initialUISetup(
        binding: FragmentModelConfigurationBinding,
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
        val modelDetailEntity = state.modelDetails
        if (modelDetailEntity != null) {
            if (state.filteredModel.isNotEmpty()) {
                controller.setData(
                    modelDetailEntity.copy(modelEntityList = state.filteredModel),
                    state.carEntity
                )
            } else {
                controller.setData(modelDetailEntity, state.carEntity)
            }
        }
        requireBinding().tvBrandSelected.text =
            getString(R.string.brand_placeholder, state.carEntity.brand)
    }

    companion object {
        const val DEBOUNCE_VALUE = 300L
    }
}