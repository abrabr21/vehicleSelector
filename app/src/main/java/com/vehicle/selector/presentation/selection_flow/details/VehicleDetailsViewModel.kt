package com.vehicle.selector.presentation.selection_flow.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vehicle.selector.domain.usecase.GetCarEntityByIdUseCase
import com.vehicle.selector.domain.usecase.InsertCarEntityUseCase
import com.vehicle.selector.domain.usecase.UpdateCarEntityUseCase
import com.vehicle.selector.presentation.selection_flow.MainVehicleSelectorEvents
import com.vehicle.selector.presentation.selection_flow.MainVehicleSelectorSingleAction
import com.vehicle.selector.presentation.selection_flow.MainVehicleSelectorState
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

class VehicleDetailsViewModel(
    private val savedStateHandle: SavedStateHandle? = null,
    private val getCarEntityByIdUseCase: GetCarEntityByIdUseCase,
    private val updateCarEntityUseCase: UpdateCarEntityUseCase,
) : ViewModel() {

    private val keyState = javaClass.name

    private val _state: MutableStateFlow<VehicleDetailsState> =
        MutableStateFlow(
            savedStateHandle?.get<VehicleDetailsState>(keyState)
                ?: VehicleDetailsState()
        )
    val state: StateFlow<VehicleDetailsState> = _state

    private val currentState: VehicleDetailsState
        get() = _state.value

    private val event = MutableSharedFlow<VehicleDetailsEvent>()

    private val _action: Channel<VehicleDetailsSingleAction> = Channel()
    val action: Flow<VehicleDetailsSingleAction> = _action.receiveAsFlow()

    private fun sendSingleAction(builder: () -> VehicleDetailsSingleAction) =
        viewModelScope.launch { _action.send(builder()) }

    private fun subscribeEvents() {
        event.mapNotNull { it }.onEach { handleEvent(it) }
            .launchIn(viewModelScope)
    }

    init {
        subscribeEvents()
    }

    private fun handleEvent(event: VehicleDetailsEvent) {
        when (event) {
            is VehicleDetailsEvent.LoadDetailsInfo -> loadDetails(event.id)
            is VehicleDetailsEvent.OnNameUpdated -> updateName(event.name)
            is VehicleDetailsEvent.Save -> save()
        }
    }

    private fun save() {
        viewModelScope.launch {
            updateCarEntityUseCase(currentState.carEntity)
                .onSuccess {
                    sendSingleAction { VehicleDetailsSingleAction.Close }
                }.onFailure {
                    sendSingleAction { VehicleDetailsSingleAction.ShowUnknownError }
                }
        }
    }

    private fun loadDetails(id: Int) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            getCarEntityByIdUseCase(id)
                .onSuccess {
                    setState { copy(carEntity = it, isLoading = false) }
                }.onFailure {
                    setState { copy(isLoading = false) }
                    sendSingleAction { VehicleDetailsSingleAction.ShowUnknownError }
                }
        }
    }

    private fun updateName(name: String) {
        setState { copy(carEntity = carEntity.copy(name = name)) }
    }

    fun sendEvent(event: VehicleDetailsEvent) = viewModelScope.launch {
        this@VehicleDetailsViewModel.event.emit(event)
    }

    private fun setState(reduce: VehicleDetailsState.() -> VehicleDetailsState) =
        viewModelScope.launch {
            val newState = currentState.reduce()
            _state.update { newState }
            savedStateHandle?.set(keyState, newState)
        }
}