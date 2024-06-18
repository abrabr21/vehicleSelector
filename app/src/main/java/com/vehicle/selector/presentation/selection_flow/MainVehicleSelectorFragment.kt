package com.vehicle.selector.presentation.selection_flow

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.vehicle.selector.R
import com.vehicle.selector.core.BaseBindingFragment
import com.vehicle.selector.core.toast
import com.vehicle.selector.databinding.FragmentMainVehicleSelectionBinding
import com.vehicle.selector.presentation.selection_flow.epoxy.CarSelectorController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainVehicleSelectorFragment :
    BaseBindingFragment<FragmentMainVehicleSelectionBinding>(R.layout.fragment_main_vehicle_selection) {

    private val navController: NavController by lazy { findNavController() }

    private val controller: CarSelectorController by lazy {
        CarSelectorController(
            onItemClick = { car ->
                navController.navigate(
                    MainVehicleSelectorFragmentDirections.actionMainVehicleSelectorFragmentToVehicleDetailsFragment(
                        car.id
                    )
                )
            },
            onRemoveClick = { car ->
                viewModel.sendEvent(MainVehicleSelectorEvents.OnItemRemoveClick(car))
            }
        )
    }

    override fun bind(view: View): FragmentMainVehicleSelectionBinding =
        FragmentMainVehicleSelectionBinding.bind(view)

    private val viewModel: MainVehicleSelectorViewModel by viewModel()

    private fun observeSingleActions() =
        viewModel.action
            .onEach { action -> handleSingleAction(action) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeState() = viewModel.state
        .onEach { state -> render(state) }
        .launchIn(viewLifecycleOwner.lifecycleScope)

    override fun initialUISetup(
        binding: FragmentMainVehicleSelectionBinding,
        savedInstanceState: Bundle?
    ) {
        super.initialUISetup(binding, savedInstanceState)
        observeState()
        observeSingleActions()
        with(requireBinding()) {
            btnCreate.setOnClickListener {
                navController.navigate(R.id.action_mainVehicleSelectorFragment_to_mainCarConfigurationFragment)
            }

            epoxyRecycleView.setController(controller)
            epoxyRecycleView.addItemDecoration(
                EpoxyItemSpacingDecorator(
                    resources.getDimensionPixelSize(R.dimen.default_margin)
                )
            )
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.sendEvent(MainVehicleSelectorEvents.LoadData)
            }
        }
    }

    private fun render(state: MainVehicleSelectorState) {
        with(requireBinding()) {
            tvEmptyList.isVisible = state.carList.isEmpty() && !state.isLoading
            swipeRefreshLayout.isVisible = state.carList.isNotEmpty() && !state.isLoading
            swipeRefreshLayout.isRefreshing = state.isLoading
            cpiProgressIndicator.isVisible = state.isLoading
        }
        controller.setData(state.carList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.sendEvent(MainVehicleSelectorEvents.LoadData)
    }

    private fun handleSingleAction(action: MainVehicleSelectorSingleAction) {
        when (action) {
            is MainVehicleSelectorSingleAction.OnShowCommonError -> requireContext().toast(
                getString(
                    R.string.common_error_msg
                )
            )
        }
    }
}