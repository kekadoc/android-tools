package com.qeapp.tools.android.view.content

abstract class AbstractContentService(adapter: AbstractServiceAdapter? = null) : Content {

    enum class State {
        NULL, ERROR, CONTENT, LOADING
    }

    var state = State.NULL
        private set

    var adapter: AbstractServiceAdapter? = adapter
        set(value) {
            field?.onDetach(this)
            field = value
            value?.onAttach(this)
        }

    final override fun showContent(code: Int) {
        state = State.CONTENT
        adapter?.onShowContent(code)
    }
    final override fun errorContent(code: Int) {
        state = State.ERROR
        adapter?.onError(code)
    }
    final override fun loadingContent(code: Int): Content.Loading {
        state = State.LOADING
        return if (adapter == null) DefLoading.instance else adapter!!.onLoading(code)
    }

    fun prepare() {
        adapter?.onContentPrepare()
    }

    fun detach() {
        adapter = null
    }

    abstract class AbstractServiceAdapter {

        abstract fun onContentPrepare()

        abstract fun onShowContent(code: Int)
        abstract fun onError(code: Int)
        abstract fun onLoading(code: Int): Content.Loading

        open fun onAttach(service: AbstractContentService) {}
        open fun onDetach(service: AbstractContentService) {}

    }

    private class DefLoading : Content.Loading {
        companion object {
            @JvmStatic val instance = DefLoading()
        }
        override fun complete() {}
        override fun update(fraction: Float, updateData: String?) {}
    }

}