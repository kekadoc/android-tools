package com.kekadoc.tools.android.view.listeners

import android.view.View
import android.widget.CompoundButton

abstract class GroupListener<L> {

    protected var listeners: MutableCollection<L>? = null

    fun addListener(listener: L) {
        if (listeners == null) listeners = linkedSetOf()
        listeners!!.add(listener)
    }
    fun removeListener(listener: L?) {
        listeners?.remove(listener)
    }

}

class GroupOnCheckedChangeListener : GroupListener<CompoundButton.OnCheckedChangeListener>(), CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        listeners?.forEach {
            it.onCheckedChanged(buttonView, isChecked)
        }
    }

}
class GroupOnClickListener : GroupListener<View.OnClickListener>(), View.OnClickListener {

    override fun onClick(v: View?) {
        listeners?.forEach {
            it.onClick(v)
        }
    }

}
class GroupOnLongClickListener : GroupListener<View.OnLongClickListener>(), View.OnLongClickListener {

    override fun onLongClick(v: View?): Boolean {
        listeners?.forEach {
            if (it.onLongClick(v)) return true
        }
        return false
    }

}