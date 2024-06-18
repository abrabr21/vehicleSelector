package com.vehicle.selector.presentation.selection_flow.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vehicle.selector.R
import com.vehicle.selector.core.BaseBindingFragment
import com.vehicle.selector.core.setTextIfNotEquals
import com.vehicle.selector.core.toast
import com.vehicle.selector.databinding.FragmentVehicleDetailsBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class VehicleDetailsFragment :
    BaseBindingFragment<FragmentVehicleDetailsBinding>(R.layout.fragment_vehicle_details) {

    private val carId: Int by lazy {
        runCatching { VehicleDetailsFragmentArgs.fromBundle(requireArguments()).carId }.getOrElse { -1 }
    }

    private val requiredField: String by lazy { getString(R.string.common_required_field) }

    override fun bind(view: View): FragmentVehicleDetailsBinding =
        FragmentVehicleDetailsBinding.bind(view)

    private val viewModel: VehicleDetailsViewModel by viewModel()

    private fun observeSingleActions() =
        viewModel.action
            .onEach { action -> handleSingleAction(action) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeState() = viewModel.state
        .onEach { state -> render(state) }
        .launchIn(viewLifecycleOwner.lifecycleScope)

    override fun initialUISetup(
        binding: FragmentVehicleDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.initialUISetup(binding, savedInstanceState)
        observeState()
        observeSingleActions()
        viewModel.sendEvent(VehicleDetailsEvent.LoadDetailsInfo(carId))
        with(requireBinding()) {
            btnSave.setOnClickListener {
                if (validateRequiredField()) {
                    viewModel.sendEvent(VehicleDetailsEvent.Save)
                }
            }
            etName.addTextChangedListener {
                viewModel.sendEvent(VehicleDetailsEvent.OnNameUpdated(it.toString()))
            }
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun render(state: VehicleDetailsState) {
        with(requireBinding()) {
            cpiProgressIndicator.isVisible = state.isLoading
            scrollView.isVisible = !state.isLoading
            tvBrand.text = state.carEntity.brand
            tvModel.text = state.carEntity.model
            tvYear.text = state.carEntity.year
            etName.setTextIfNotEquals(state.carEntity.name)
        }
    }

    private fun handleSingleAction(action: VehicleDetailsSingleAction) {
        when (action) {
            is VehicleDetailsSingleAction.Close -> findNavController().navigateUp()
            is VehicleDetailsSingleAction.ShowUnknownError -> requireContext().toast(getString(R.string.common_error_msg))
        }
    }

    private fun validateRequiredField(): Boolean =
        with(requireBinding()) {
            val isNameValid = (etName.text?.toString() ?: "").isNotBlank()
            tilName.error = if (isNameValid) null else requiredField
            return isNameValid
        }
}