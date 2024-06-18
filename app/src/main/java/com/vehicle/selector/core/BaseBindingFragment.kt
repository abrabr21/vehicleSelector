package com.vehicle.selector.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseBindingFragment<Binding : ViewBinding>(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId) {

    protected var binding: Binding? = null

    protected abstract fun bind(view: View): Binding

    protected fun requireBinding(): Binding = binding!!


    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.also {
            binding = bind(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialUISetup(requireBinding(), savedInstanceState)
        if (savedInstanceState == null) {
            initialAction()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearUI(requireBinding())
        binding = null
    }

    open fun initialUISetup(binding: Binding, savedInstanceState: Bundle?) {}

    open fun initialAction() {}

    open fun clearUI(binding: Binding) {}

    protected inline fun binding(block: Binding.() -> Unit) {
        binding?.let {
            it.block()
        }
    }

}