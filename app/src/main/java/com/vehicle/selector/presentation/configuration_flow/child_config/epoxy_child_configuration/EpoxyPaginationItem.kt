package com.vehicle.selector.presentation.configuration_flow.child_config.epoxy_child_configuration

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton
import com.vehicle.selector.R

@EpoxyModelClass
abstract class EpoxyPaginationItem: EpoxyModelWithHolder<EpoxyPaginationItem.PaginationHolder>() {

    override fun getDefaultLayout(): Int = R.layout.item_paginationxml

    @EpoxyAttribute(value = [EpoxyAttribute.Option.DoNotHash])
    var onNextClick: () -> Unit = {}

    @EpoxyAttribute(value = [EpoxyAttribute.Option.DoNotHash])
    var onBackClick: () -> Unit = {}

    @EpoxyAttribute
    @JvmField
    var isNextPageEnabled: Boolean = true

    @EpoxyAttribute
    @JvmField
    var isBackPageEnabled: Boolean = true

    @EpoxyAttribute
    var pageNumber: Int = 0

    override fun bind(holder: PaginationHolder) {
        super.bind(holder)
        with(holder) {
            btnNextPage?.isEnabled = isNextPageEnabled
            btnNextPage?.alpha = if(isNextPageEnabled) 1f else 0.5f
            btnBackPage?.isEnabled = isBackPageEnabled
            btnBackPage?.alpha = if(isBackPageEnabled) 1f else 0.5f
            tvPageNumber?.text = pageNumber.toString()
            btnNextPage?.setOnClickListener {
                onNextClick()
            }
            btnBackPage?.setOnClickListener {
                onBackClick()
            }
        }
    }

    class PaginationHolder : EpoxyHolder() {
        var rootContainer: ConstraintLayout? = null
        var tvPageNumber: TextView? = null
        var btnNextPage: MaterialButton? = null
        var btnBackPage: MaterialButton? = null

        override fun bindView(view: View) {
            rootContainer = view.findViewById(R.id.root_container)
            tvPageNumber = view.findViewById(R.id.tv_page_number)
            btnNextPage = view.findViewById(R.id.btn_next_page)
            btnBackPage = view.findViewById(R.id.btn_previous_page)
        }
    }
}