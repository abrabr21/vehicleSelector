package com.vehicle.selector.presentation.configuration_flow

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vehicle.selector.domain.entity.BrandEntity
import com.vehicle.selector.domain.entity.ModelEntity
import com.vehicle.selector.domain.entity.YearEntity
import com.vehicle.selector.domain.usecase.GetBrandDetailsUseCase
import com.vehicle.selector.domain.usecase.GetModelDetailsUseCase
import com.vehicle.selector.domain.usecase.GetYearDetailsUseCase
import com.vehicle.selector.domain.usecase.InsertCarEntityUseCase
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyBrandController.Companion.PAGE_STEP
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainCarConfigurationViewModel(
    private val savedStateHandle: SavedStateHandle? = null,
    private val getBrandDetailsUseCase: GetBrandDetailsUseCase,
    private val getModelDetailsUseCase: GetModelDetailsUseCase,
    private val getYearDetailsUseCase: GetYearDetailsUseCase,
    private val insertCarEntityUseCase: InsertCarEntityUseCase,
) : ViewModel() {

    private val keyState = javaClass.name

    private val _sharedAction: MutableSharedFlow<CarConfigurationSharedAction> = MutableSharedFlow()
    val sharedAction: SharedFlow<CarConfigurationSharedAction> = _sharedAction

    private val _state: MutableStateFlow<MainCarConfigurationState> =
        MutableStateFlow(
            savedStateHandle?.get<MainCarConfigurationState>(keyState)
                ?: MainCarConfigurationState()
        )
    val state: StateFlow<MainCarConfigurationState> = _state

    private val currentState: MainCarConfigurationState
        get() = _state.value

    private val event = MutableSharedFlow<CarConfigurationEvent>()

    private val _action: Channel<CarConfigurationSingleAction> = Channel()
    val action: Flow<CarConfigurationSingleAction> = _action.receiveAsFlow()

    fun sendSharedAction(action: CarConfigurationSharedAction) {
        viewModelScope.launch { _sharedAction.emit(action) }
    }

    private fun sendSingleAction(builder: () -> CarConfigurationSingleAction) =
        viewModelScope.launch { _action.send(builder()) }

    private fun subscribeEvents() {
        event.onEach { handleEvent(it) }
            .launchIn(viewModelScope)
    }

    init {
        subscribeEvents()
    }

    private fun handleEvent(event: CarConfigurationEvent) {
        when (event) {
            is CarConfigurationEvent.Load -> loadBrandPage()
            is CarConfigurationEvent.OnBrandSelected -> onBrandSelected(event.brandEntity)
            is CarConfigurationEvent.OnModelSelected -> onModelSelected(event.modelEntity)
            is CarConfigurationEvent.OnNextStepClick -> moveNext()
            is CarConfigurationEvent.OnPreviousStepClick -> movePrevious()
            is CarConfigurationEvent.OnYearSelected -> onYearSelected(event.yearEntity)
            is CarConfigurationEvent.BrandPagination.BrandNavigationBackPage -> backBrandPageLoad()
            is CarConfigurationEvent.BrandPagination.BrandNavigationNextPage -> nextBrandPageLoad()
            is CarConfigurationEvent.ModelPagination.ModelNavigationBackPage -> backModelPageLoad()
            is CarConfigurationEvent.ModelPagination.ModelNavigationNextPage -> nextModelPageLoad()
            is CarConfigurationEvent.FilterDueToSearch -> filterDueToSearch(event.name)
            is CarConfigurationEvent.OnNamePrinted -> onNameInput(event.name)
            is CarConfigurationEvent.Save -> upsertCarEntity()
        }
    }

    private fun onNameInput(name: String) {
        setState { copy(carEntity = carEntity.copy(name = name)) }
    }

    private fun movePrevious() {
        val previousStep = currentState.configurationStep.previousStep
        if (previousStep != null) {
            setState { copy(configurationStep = previousStep) }
            sendSingleAction { CarConfigurationSingleAction.MoveBack(previousStep.screenId) }
        } else {
            sendSingleAction { CarConfigurationSingleAction.Finish }
        }
    }

    private fun moveNext() {
        val currentStep = currentState.configurationStep
        val nextStep = currentState.configurationStep.nextStep
        if (currentStep.screenDirection() != null && nextStep != null) {
            setState { copy(configurationStep = nextStep) }
            sendSingleAction { CarConfigurationSingleAction.MoveNext(currentStep.screenDirection()) }
        } else {
            sendSingleAction { CarConfigurationSingleAction.Finish }
        }
    }

    private fun upsertCarEntity() {
        viewModelScope.launch {
            insertCarEntityUseCase(currentState.carEntity)
                .onSuccess {
                    sendSingleAction { CarConfigurationSingleAction.Finish }
                }.onFailure {
                    sendSingleAction { CarConfigurationSingleAction.OnCommonErrorShow }
                }
        }
    }

    private fun filterDueToSearch(name: String) {
        val updatedState = when (currentState.configurationStep) {
            is CarConfigureStep.ModelSelectStep -> currentState.copy(
                filteredModel = currentState.modelDetails?.modelEntityList?.filter {
                    it.name.lowercase().contains(name)
                } ?: emptyList()
            )

            else -> currentState
        }
        setState { updatedState }
    }

    private fun backModelPageLoad() {
        val modelDetails = currentState.modelDetails
        if (modelDetails != null && currentState.isPreviousModelPageAvailable) {
            loadModelDetails(currentState.carEntity.brandKey, modelDetails.page - PAGE_STEP)
        }
    }

    private fun nextModelPageLoad() {
        val modelDetails = currentState.modelDetails
        if (modelDetails != null && currentState.isNextModelPageAvailable) {
            loadModelDetails(currentState.carEntity.brandKey, modelDetails.page + PAGE_STEP)
        }
    }

    private fun backBrandPageLoad() {
        val brandDetails = currentState.brandDetails
        if (brandDetails != null && currentState.isPreviousBrandPageAvailable) {
            loadBrandPage(brandDetails.page - PAGE_STEP)
        }
    }

    private fun nextBrandPageLoad() {
        val brandDetails = currentState.brandDetails
        if (brandDetails != null && currentState.isNextBrandPageAvailable) {
            loadBrandPage(brandDetails.page + PAGE_STEP)
        }
    }

    private fun onYearSelected(yearEntity: YearEntity) {
        setState {
            copy(
                carEntity = carEntity.copy(
                    year = yearEntity.year
                ),
            )
        }
    }

    private fun onModelSelected(modelEntity: ModelEntity) {
        setState {
            copy(
                carEntity = currentState.carEntity.copy(
                    model = modelEntity.name
                )
            )
        }
        viewModelScope.launch {
            getYearDetailsUseCase(currentState.carEntity.brandKey, modelEntity.modelKey)
                .onSuccess { result ->
                    setState {
                        copy(
                            yearDetails = result,
                        )
                    }
                }.onFailure {
                    sendSingleAction { CarConfigurationSingleAction.OnCommonErrorShow }
                }
        }
    }

    private fun onBrandSelected(brandEntity: BrandEntity) {
        setState {
            copy(
                carEntity = currentState.carEntity.copy(
                    brand = brandEntity.name,
                    brandKey = brandEntity.brandKey
                )
            )
        }
        loadModelDetails(brandEntity.brandKey)
    }

    private fun loadModelDetails(
        brandKey: String,
        pageNumber: Int = 0,
    ) = viewModelScope.launch {
        getModelDetailsUseCase(params = Pair(pageNumber, brandKey))
            .onSuccess { result ->
                setState {
                    copy(
                        modelDetails = result,
                    )
                }
            }.onFailure {
                sendSingleAction { CarConfigurationSingleAction.OnCommonErrorShow }
            }
    }


    private fun loadBrandPage(pageNumber: Int = 0) {
        viewModelScope.launch {
            getBrandDetailsUseCase(pageNumber)
                .onSuccess { result ->
                    setState { copy(brandDetails = result) }
                }.onFailure {
                    sendSingleAction { CarConfigurationSingleAction.OnCommonErrorShow }
                }
        }
    }


    fun sendEvent(event: CarConfigurationEvent) = viewModelScope.launch {
        this@MainCarConfigurationViewModel.event.emit(event)
    }

    private fun setState(reduce: MainCarConfigurationState.() -> MainCarConfigurationState) =
        viewModelScope.launch {
            val newState = currentState.reduce()
            _state.emit(newState)
            savedStateHandle?.set(keyState, newState)
        }
}