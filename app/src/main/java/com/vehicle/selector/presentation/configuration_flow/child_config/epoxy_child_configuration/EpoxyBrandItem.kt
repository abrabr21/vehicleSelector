package com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton
import com.vehicle.selector.R
import com.vehicle.selector.domain.entity.BrandEntity
import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.presentation.selection_flow.epoxy.EpoxyCarModel

@EpoxyModelClass
abstract class EpoxyBrandItem : EpoxyModelWithHolder<EpoxyBrandItem.BrandItemHolder>() {

    override fun getDefaultLayout(): Int = R.layout.item_brand_view

    @EpoxyAttribute
    lateinit var brandEntity: BrandEntity

    @EpoxyAttribute(value = [EpoxyAttribute.Option.DoNotHash])
    var onItemClick: (BrandEntity) -> Unit = {}

    @EpoxyAttribute
    @JvmField
    var isSelected : Boolean = false

    override fun bind(holder: BrandItemHolder) {
        super.bind(holder)
        with(holder) {
            rootContainer?.setOnClickListener {
                onItemClick(brandEntity)
            }
            tvName?.text = brandEntity.name
            rootContainer?.isSelected = isSelected
        }
    }

    class BrandItemHolder : EpoxyHolder() {
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