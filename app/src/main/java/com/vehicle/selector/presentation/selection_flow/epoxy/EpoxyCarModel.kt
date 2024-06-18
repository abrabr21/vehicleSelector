package com.vehicle.selector.presentation.selection_flow.epoxy

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton
import com.vehicle.selector.R
import com.vehicle.selector.domain.entity.CarEntity

@EpoxyModelClass
abstract class EpoxyCarModel : EpoxyModelWithHolder<EpoxyCarModel.CarItemHolder>(){

    override fun getDefaultLayout(): Int = R.layout.item_car_view

    @EpoxyAttribute
    lateinit var carEntity: CarEntity

    @EpoxyAttribute(value = [EpoxyAttribute.Option.DoNotHash])
    var onItemClick: (CarEntity) -> Unit = {}

    @EpoxyAttribute(value = [EpoxyAttribute.Option.DoNotHash])
    var onRemoveClick: (CarEntity) -> Unit = {}

    override fun bind(holder: CarItemHolder) {
        super.bind(holder)
        with(holder) {
            rootContainer?.setOnClickListener {
                onItemClick(carEntity)
            }
            tvName?.text = carEntity.name
            tvBrand?.text = carEntity.brand
            tvYear?.text = carEntity.year
            tvModel?.text = carEntity.model
            btnRemove?.setOnClickListener {
                onRemoveClick(carEntity)
            }
            btnLookIt?.setOnClickListener {
                onItemClick(carEntity)
            }
        }
    }

    class CarItemHolder : EpoxyHolder() {
        var rootContainer: CardView? = null
        var clContainer: ConstraintLayout? = null
        var tvName: TextView? = null
        var tvBrand: TextView? = null
        var tvYear: TextView? = null
        var tvModel: TextView? = null
        var btnLookIt: MaterialButton? = null
        var btnRemove : MaterialButton? = null

        override fun bindView(view: View) {
            rootContainer = view.findViewById(R.id.root_container)
            clContainer = view.findViewById(R.id.cl_container)
            tvName = view.findViewById(R.id.tv_name)
            tvBrand = view.findViewById(R.id.tv_brand)
            tvYear = view.findViewById(R.id.tv_year)
            tvModel = view.findViewById(R.id.tv_model)
            btnLookIt = view.findViewById(R.id.btn_look_it)
            btnRemove = view.findViewById(R.id.btn_remove)
        }
    }
}