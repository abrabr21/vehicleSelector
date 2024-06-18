package com.vehicle.selector.presentation.configuration_flow

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.vehicle.selector.R
import com.vehicle.selector.domain.entity.BrandDetailEntity
import com.vehicle.selector.domain.entity.BrandEntity
import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.entity.ModelDetailEntity
import com.vehicle.selector.domain.entity.ModelEntity
import com.vehicle.selector.domain.entity.YearDetailEntity
import com.vehicle.selector.domain.entity.YearEntity
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyBrandController.Companion.FIRST_PAGE
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyBrandController.Companion.PAGE_STEP
import java.io.Serial

data class MainCarConfigurationState(
    val carEntity: CarEntity = CarEntity.getEmptyCarEntity(),
    val brandDetails: BrandDetailEntity? = null,
    val modelDetails: ModelDetailEntity? = null,
    val yearDetails: YearDetailEntity? = null,
    val filteredModel: List<ModelEntity> = emptyList(),
    val isLoading: Boolean = true,
    val configurationStep: CarConfigureStep = CarConfigureStep.BrandSelectStep,
    val selectedBrand: String = "",
    val selectedModel: String = "",
    val selectedYear: String = ""
)  : java.io.Serializable {
    val isNextBrandPageAvailable: Boolean
        get() = if (brandDetails != null) {
            (brandDetails.page + PAGE_STEP) in FIRST_PAGE until brandDetails.totalPageCount
        } else false

    val isPreviousBrandPageAvailable: Boolean
        get() = if (brandDetails != null) {
            (brandDetails.page - PAGE_STEP) in FIRST_PAGE until brandDetails.totalPageCount
        } else false

    val isNextModelPageAvailable: Boolean
        get() = if (modelDetails != null) {
            (modelDetails.page + PAGE_STEP) in FIRST_PAGE until modelDetails.totalPageCount
        } else false

    val isPreviousModelPageAvailable: Boolean
        get() = if (modelDetails != null) {
            (modelDetails.page + PAGE_STEP) in FIRST_PAGE until modelDetails.totalPageCount
        } else false

    val isContinueEnabled : Boolean
        get() = when(configurationStep) {
            is CarConfigureStep.BrandSelectStep -> carEntity.brand.isNotEmpty()
            is CarConfigureStep.CompleteStep -> true
            is CarConfigureStep.ModelSelectStep -> carEntity.model.isNotEmpty()
            is CarConfigureStep.YearConfigurationStep -> carEntity.year.isNotEmpty()
        }
}

sealed class CarConfigureStep(
    @StringRes open val titleId: Int,
    @StringRes open val buttonId: Int = R.string.common_continue,
    @IdRes open val screenId: Int
) {

    open fun screenDirection(): Int? = null
    abstract val nextStep: CarConfigureStep?
    abstract val previousStep: CarConfigureStep?

    data object BrandSelectStep : CarConfigureStep(
        titleId = R.string.vehicle_selector_brand,
        screenId = R.id.carBrandFragment
    ) {
        override val nextStep: CarConfigureStep
            get() = ModelSelectStep
        override val previousStep: CarConfigureStep?
            get() = null

        override fun screenDirection(): Int = R.id.action_carBrandFragment_to_carModelFragment
    }

    data object ModelSelectStep : CarConfigureStep(
        titleId = R.string.vehicle_selector_manufacturer_type,
        screenId = R.id.carModelFragment
    ) {
        override val nextStep: CarConfigureStep
            get() = YearConfigurationStep
        override val previousStep: CarConfigureStep
            get() = BrandSelectStep

        override fun screenDirection(): Int = R.id.action_carModelFragment_to_carYearFragment
    }

    data object YearConfigurationStep : CarConfigureStep(
        titleId = R.string.vehicle_selector_year,
        screenId = R.id.carYearFragment
    ) {
        override val nextStep: CarConfigureStep
            get() = CompleteStep
        override val previousStep: CarConfigureStep
            get() = ModelSelectStep

        override fun screenDirection(): Int =
            R.id.action_carYearFragment_to_summaryConfigurationFragment
    }

    data object CompleteStep : CarConfigureStep(
        titleId = R.string.common_summary,
        screenId = R.id.summaryConfigurationFragment
    ) {
        override val previousStep: CarConfigureStep
            get() = YearConfigurationStep
        override val nextStep: CarConfigureStep?
            get() = null
        override val buttonId: Int
            get() = R.string.common_save

        override fun screenDirection(): Int? = null
    }
}

sealed class CarConfigurationEvent {
    data object Load : CarConfigurationEvent()

    data class OnBrandSelected(val brandEntity: BrandEntity) : CarConfigurationEvent()
    data class OnModelSelected(val modelEntity: ModelEntity) : CarConfigurationEvent()
    data class OnYearSelected(val yearEntity: YearEntity) : CarConfigurationEvent()
    data class OnNamePrinted(val name: String) : CarConfigurationEvent()
    data class FilterDueToSearch(val name : String) : CarConfigurationEvent()

    sealed class BrandPagination : CarConfigurationEvent() {
        data object BrandNavigationNextPage : BrandPagination()
        data object BrandNavigationBackPage : BrandPagination()
    }

    sealed class ModelPagination : CarConfigurationEvent() {
        data object ModelNavigationNextPage : BrandPagination()
        data object ModelNavigationBackPage : BrandPagination()
    }

    data object OnNextStepClick : CarConfigurationEvent()
    data object OnPreviousStepClick : CarConfigurationEvent()

    data object Save : CarConfigurationEvent()
}

sealed class CarConfigurationSharedAction {
    data object OnGoBackClicked : CarConfigurationSharedAction()
    data object OnContinueClicked : CarConfigurationSharedAction()
}

sealed class CarConfigurationSingleAction {
    data object Finish : CarConfigurationSingleAction()
    data object OnCommonErrorShow : CarConfigurationSingleAction()
    data class MoveNext(@IdRes val direction: Int?) : CarConfigurationSingleAction()
    data class MoveBack(@IdRes val screenId: Int) : CarConfigurationSingleAction()
}