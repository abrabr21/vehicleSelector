package com.vehicle.selector.presentation.selection_flow

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.usecase.DeleteCarEntityUseCase
import com.vehicle.selector.domain.usecase.GetBrandDetailsUseCase
import com.vehicle.selector.domain.usecase.GetCarEntityListUseCase
import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainVehicleSelectorViewModel(
    private val savedStateHandle: SavedStateHandle? = null,
    private val getCarEntityListUseCase: GetCarEntityListUseCase,
    private val deleteCarEntityUseCase: DeleteCarEntityUseCase
) : ViewModel() {

    private val keyState = javaClass.name

    private val _state: MutableStateFlow<MainVehicleSelectorState> =
        MutableStateFlow(
            savedStateHandle?.get<MainVehicleSelectorState>(keyState)
                ?: MainVehicleSelectorState(emptyList(), true)
        )
    val state: StateFlow<MainVehicleSelectorState> = _state

    private val currentState: MainVehicleSelectorState
        get() = _state.value

    private val event = MutableSharedFlow<MainVehicleSelectorEvents>()

    private val _action: Channel<MainVehicleSelectorSingleAction> = Channel()
    val action: Flow<MainVehicleSelectorSingleAction> = _action.receiveAsFlow()

    private fun sendSingleAction(builder: () -> MainVehicleSelectorSingleAction) =
        viewModelScope.launch { _action.send(builder()) }

    private fun subscribeEvents() {
        event.mapNotNull { it }.onEach { handleEvent(it) }
            .launchIn(viewModelScope)
    }

    init {
        subscribeEvents()
    }

    private fun handleEvent(event: MainVehicleSelectorEvents) {
        when (event) {
            is MainVehicleSelectorEvents.LoadData -> fetchDataFromInternalDataSource()
            is MainVehicleSelectorEvents.OnItemRemoveClick -> removeClick(event.carEntity)
        }
    }

    private fun removeClick(carEntity: CarEntity) {
        viewModelScope.launch {
            deleteCarEntityUseCase(carEntityId = carEntity.id)
                .onSuccess {
                    fetchDataFromInternalDataSource()
                }.onFailure {
                    sendSingleAction { MainVehicleSelectorSingleAction.OnShowCommonError }
                }
        }
    }

    private fun fetchDataFromInternalDataSource() {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            getCarEntityListUseCase(Unit)
                .onSuccess { carList ->
                    setState {
                        copy(
                            carList = carList,
                            isLoading = false
                        )
                    }
                }.onFailure {
                    setState { copy(isLoading = false) }
                    sendSingleAction { MainVehicleSelectorSingleAction.OnShowCommonError }
                }
        }
    }

    fun sendEvent(event: MainVehicleSelectorEvents) = viewModelScope.launch {
        this@MainVehicleSelectorViewModel.event.emit(event)
    }

    private fun setState(reduce: MainVehicleSelectorState.() -> MainVehicleSelectorState) =
        viewModelScope.launch {
            val newState = currentState.reduce()
            _state.update { newState }
            savedStateHandle?.set(keyState, newState)
        }
}