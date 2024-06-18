package com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.vehicle.selector.R
import com.vehicle.selector.domain.entity.YearEntity

@EpoxyModelClass
abstract class EpoxyYearItem : EpoxyModelWithHolder<EpoxyYearItem.YearItemHolder>() {

    override fun getDefaultLayout(): Int = R.layout.item_year_view

    @EpoxyAttribute
    lateinit var yearEntity: YearEntity

    @EpoxyAttribute(value = [EpoxyAttribute.Option.DoNotHash])
    var onItemClick: (YearEntity) -> Unit = {}

    @EpoxyAttribute
    @JvmField
    var isSelected : Boolean = false

    override fun bind(holder: YearItemHolder) {
        super.bind(holder)
        with(holder) {
            rootContainer?.setOnClickListener {
                onItemClick(yearEntity)
            }
            tvName?.text = yearEntity.year
            rootContainer?.isSelected = isSelected
        }
    }

    class YearItemHolder : EpoxyHolder() {
        var rootContainer: CardView? = null
        var clContainer: ConstraintLayout? = null
        var tvName: TextView? = null

        override fun bindView(view: View) {
            rootContainer = view.findViewById(R.id.root_container)
            clContainer = view.findViewById(R.id.cl_container)
            tvName = view.findViewById(R.id.tv_name)
        }
    }
}