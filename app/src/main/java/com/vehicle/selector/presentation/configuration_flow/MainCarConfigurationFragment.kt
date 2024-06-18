package com.vehicle.selector.presentation.configuration_flow

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.vehicle.selector.R
import com.vehicle.selector.core.BaseBindingFragment
import com.vehicle.selector.core.textChanges
import com.vehicle.selector.core.toast
import com.vehicle.selector.databinding.FragmentMainConfigurationBinding
import com.vehicle.selector.presentation.configuration_flow.child_config.CarModelFragment
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainCarConfigurationFragment :
    BaseBindingFragment<FragmentMainConfigurationBinding>(R.layout.fragment_main_configuration),
    MainCarConfigurationStateOwner {

    override fun bind(view: View): FragmentMainConfigurationBinding =
        FragmentMainConfigurationBinding.bind(view)

    override val stateViewModelStoreOwner: ViewModelStoreOwner
        get() = this

    override val creationExtras: CreationExtras
        get() = defaultViewModelCreationExtras

    private val innerNavController: NavController by lazy {
        (childFragmentManager
            .findFragmentById(R.id.fcv_vehicle_main_nav_host_controller) as NavHostFragment).navController
    }

    private val parentNavController: NavController by lazy {
        findNavController()
    }

    private val viewModel: MainCarConfigurationViewModel by viewModel()

    override fun initialUISetup(
        binding: FragmentMainConfigurationBinding,
        savedInstanceState: Bundle?
    ) {
        super.initialUISetup(binding, savedInstanceState)
        observeState()
        observeSingleActions()
        setUpSearchView(requireBinding().toolbar)
        viewModel.sendEvent(CarConfigurationEvent.Load)
        with(requireBinding()) {
            btnContinue.setOnClickListener {
                viewModel.sendSharedAction(CarConfigurationSharedAction.OnContinueClicked)
            }
            btnGoBack?.setOnClickListener {
                viewModel.sendSharedAction(CarConfigurationSharedAction.OnGoBackClicked)
            }
            toolbar.setNavigationOnClickListener {
                viewModel.sendSharedAction(CarConfigurationSharedAction.OnGoBackClicked)
            }
        }
    }

    private fun observeSingleActions() =
        viewModel.action
            .onEach { action -> handleSingleAction(action) }
            .launchIn(viewLifecycleOwner.lifecycleScope)


    private fun observeState() = viewModel.state
        .onEach { state -> render(state) }
        .launchIn(viewLifecycleOwner.lifecycleScope)

    private fun render(state: MainCarConfigurationState) {
        with(requireBinding()) {
            btnContinue.text = getString(state.configurationStep.buttonId)
            btnContinue.isEnabled = state.isContinueEnabled
            toolbar.title = getString(state.configurationStep.titleId)
            toolbar.menu.findItem(R.id.menu_search_item)?.isVisible =
                state.configurationStep == CarConfigureStep.ModelSelectStep
        }
    }

    private fun handleSingleAction(action: CarConfigurationSingleAction) {
        when (action) {
            is CarConfigurationSingleAction.Finish -> close()
            is CarConfigurationSingleAction.MoveNext -> moveNextStep(action.direction)
            is CarConfigurationSingleAction.OnCommonErrorShow -> requireContext().toast(getString(R.string.common_error_msg))
            is CarConfigurationSingleAction.MoveBack -> navigateBack(action.screenId)
        }
    }

    private fun moveNextStep(@IdRes direction: Int?) = if (direction != null) {
        innerNavController.navigate(direction)
    } else close()


    private fun close() {
        parentNavController.navigateUp()
    }

    private fun navigateBack(@IdRes popToId: Int) {
        innerNavController.popBackStack(
            popToId,
            false
        )
    }

    @OptIn(FlowPreview::class)
    private fun setUpSearchView(toolbar: MaterialToolbar) {
        toolbar.inflateMenu(R.menu.menu_search)
        val menu = toolbar.menu
        val searchItem: MenuItem = menu.findItem(R.id.menu_search_item)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = requireContext().getText(R.string.common_search)
        searchView
            .textChanges()
            .debounce(CarModelFragment.DEBOUNCE_VALUE)
            .onEach {
                viewModel.sendEvent(CarConfigurationEvent.FilterDueToSearch(it.lowercase()))
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}