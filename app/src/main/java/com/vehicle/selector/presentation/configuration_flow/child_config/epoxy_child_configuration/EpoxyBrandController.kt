package com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.vehicle.selector.R
import com.vehicle.selector.domain.entity.BrandDetailEntity
import com.vehicle.selector.domain.entity.BrandEntity
import com.vehicle.selector.domain.entity.CarEntity


class EpoxyBrandController(
    private val onItemClick: (BrandEntity) -> (Unit) = {},
    private val onNextPageClick: () -> (Unit) = {},
    private val onPreviousPageClick: () -> (Unit) = {}
) : Typed2EpoxyController<BrandDetailEntity, CarEntity>() {
    override fun buildModels(data: BrandDetailEntity, carEntity: CarEntity) {
        data.brandEntityList.forEach { brandItem ->
            epoxyBrandItem {
                id(data.brandEntityList.indexOf(brandItem))
                isSelected(brandItem.name == carEntity.brand)
                brandEntity(brandItem)
                onItemClick(onItemClick)
            }
        }
        data.takeIf { it.brandEntityList.isNotEmpty() }?.let {
            val pageRange = FIRST_PAGE until data.totalPageCount
            epoxyPaginationItem {
                id(data.brandEntityList.lastIndex + 1)
                pageNumber(it.page)
                onBackClick { onPreviousPageClick() }
                onNextClick { onNextPageClick() }
                isBackPageEnabled((it.page - PAGE_STEP) in pageRange)
                isNextPageEnabled((it.page + PAGE_STEP) in pageRange)
            }
        }
    }

    companion object {
        const val FIRST_PAGE = 0
        const val PAGE_STEP = 1
    }
}