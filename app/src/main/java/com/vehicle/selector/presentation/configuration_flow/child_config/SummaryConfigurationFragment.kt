package com.vehicle.selector.presentation.configuration_flow.child_config

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.vehicle.selector.R
import com.vehicle.selector.databinding.FragmentSummaryConfigurationBinding
import com.vehicle.selector.presentation.configuration_flow.CarConfigurationEvent
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationState
import com.vehicle.selector.presentation.configuration_flow.base.ChildCarConfigurationFragment

class SummaryConfigurationFragment :
    ChildCarConfigurationFragment<FragmentSummaryConfigurationBinding>(R.layout.fragment_summary_configuration) {

    private val requiredField: String by lazy { getString(R.string.common_required_field) }

    override fun bind(view: View): FragmentSummaryConfigurationBinding =
        FragmentSummaryConfigurationBinding.bind(view)

    override fun initialUISetup(
        binding: FragmentSummaryConfigurationBinding,
        savedInstanceState: Bundle?
    ) {
        super.initialUISetup(binding, savedInstanceState)
        with(requireBinding()) {
            etName.addTextChangedListener {
                sendEvent(CarConfigurationEvent.OnNamePrinted(it.toString()))
            }
        }
    }

    private fun validateRequiredField(): Boolean =
        with(requireBinding()) {
            val isNameValid = (etName.text?.toString() ?: "").isNotBlank()
            tilName.error = if (isNameValid) null else requiredField
            return isNameValid
        }

    override fun onContinueClick() {
        if (validateRequiredField()) {
            view?.apply {
                val imm: InputMethodManager? =
                    this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(this.windowToken, 0)
            }
            sendEvent(CarConfigurationEvent.Save)
        }
    }

    override fun onSharedStateChanged(state: MainCarConfigurationState) {
        with(requireBinding()) {
            tvBrand.text = state.carEntity.brand
            tvModel.text = state.carEntity.model
            tvYear.text = state.carEntity.year
        }
    }
}