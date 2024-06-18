package com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration

import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.vehicle.selector.domain.entity.BrandDetailEntity
import com.vehicle.selector.domain.entity.BrandEntity
import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.entity.ModelDetailEntity
import com.vehicle.selector.domain.entity.ModelEntity
import com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration.EpoxyBrandController.Companion.PAGE_STEP

class EpoxyModelController(
    private val onItemClick: (ModelEntity) -> (Unit) = {},
    private val onNextPageClick: () -> (Unit) = {},
    private val onPreviousPageClick: () -> (Unit) = {}
) : Typed2EpoxyController<ModelDetailEntity, CarEntity>() {

    override fun buildModels(data: ModelDetailEntity, carEntity: CarEntity) {
        data.modelEntityList.forEach { modelEntity ->
            epoxyModelItem {
                id(data.modelEntityList.indexOf(modelEntity))
                isSelected(modelEntity.name == carEntity.model)
                modelEntity(modelEntity)
                onItemClick(onItemClick)
            }
        }
        data.takeIf { it.modelEntityList.isNotEmpty() }?.let {
            val pageRange = EpoxyBrandController.FIRST_PAGE until data.totalPageCount
            epoxyPaginationItem {
                id(data.modelEntityList.lastIndex + 1)
                pageNumber(it.page)
                onBackClick { onPreviousPageClick() }
                onNextClick { onNextPageClick() }
                isBackPageEnabled((it.page - PAGE_STEP) in pageRange)
                isNextPageEnabled((it.page + PAGE_STEP) in pageRange)
            }
        }
    }
}