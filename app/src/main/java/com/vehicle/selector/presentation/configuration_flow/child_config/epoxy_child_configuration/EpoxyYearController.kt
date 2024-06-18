package com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration

import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.entity.YearDetailEntity
import com.vehicle.selector.domain.entity.YearEntity

class EpoxyYearController(
    private val onItemClick: (YearEntity) -> (Unit) = {}
) : Typed2EpoxyController<YearDetailEntity, CarEntity>() {

    override fun buildModels(data: YearDetailEntity, carEntity: CarEntity) {
        data.yearEntityList.forEach { yearEntity ->
            epoxyYearItem {
                id(data.yearEntityList.indexOf(yearEntity))
                isSelected(yearEntity.year == carEntity.year)
                yearEntity(yearEntity)
                onItemClick(onItemClick)
            }
        }
    }
}