package com.qeapp.tools.android.view.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.qeapp.tools.android.R

class FragmentContentService : AbstractContentService() {

    private var textViewTitle: TextView? = null
    private var textViewDescription: TextView? = null
    private var frameLayoutHolder: FrameLayout? = null
    private var frameLayoutContent: FrameLayout? = null
    private var frameLayoutShutter: FrameLayout? = null

    private var descriptionShown = false

    fun showDescription() {
        if (descriptionShown) return
        textViewDescription?.let {
            if (it.text == null || it.text.isEmpty()) return
            val transition: Transition = AutoTransition()
            transition.duration = 100L
            TransitionManager.beginDelayedTransition((it.rootView as ViewGroup), transition)
            it.visibility = View.VISIBLE
            descriptionShown = true
        }
    }

    fun hideDescription() {
        if (!descriptionShown) return
        textViewDescription?.let {
            val transition: Transition = AutoTransition()
            transition.duration = 100L
            TransitionManager.beginDelayedTransition((it.rootView as ViewGroup), transition)
            it.visibility = View.GONE
            descriptionShown = false
        }
    }

    fun createContent(inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = inflater.inflate(R.layout.content_service, container, false)
        this.textViewTitle = view.findViewById(R.id.textView_title)
        this.textViewDescription = view.findViewById(R.id.textView_description)
        this.frameLayoutHolder = view.findViewById(R.id.frameLayout_holder)
        this.frameLayoutContent = view.findViewById(R.id.frameLayout_content)
        this.frameLayoutShutter = view.findViewById(R.id.frameLayout_shutter)

        textViewTitle!!.setOnClickListener { if (descriptionShown) hideDescription() else showDescription() }
        return view
    }

    abstract class ServiceAdapter(private val fragment: Fragment) : AbstractServiceAdapter() {

        var fragmentContentService: FragmentContentService? = null

        abstract fun getContentFragment(code: Int): Fragment
        abstract fun getErrorFragment(code: Int): Fragment
        abstract fun getLoadingFragment(code: Int): Fragment
        abstract fun getLoading(code: Int, fragment: Fragment): Content.Loading

        override fun onAttach(service: AbstractContentService) {
            this.fragmentContentService = service as FragmentContentService
        }
        override fun onDetach(service: AbstractContentService) {
            this.fragmentContentService = null
        }

        override fun onShowContent(code: Int) {
            this.fragmentContentService!!.frameLayoutContent!!.visibility = View.VISIBLE
            this.fragmentContentService!!.frameLayoutShutter!!.visibility = View.GONE
            fragment.childFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout_content, getContentFragment(code))
                    .commit()
        }
        override fun onError(code: Int) {
            this.fragmentContentService!!.frameLayoutContent!!.visibility = View.GONE
            this.fragmentContentService!!.frameLayoutShutter!!.visibility = View.VISIBLE
            fragment.childFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout_shutter, getErrorFragment(code))
                    .commit()
        }
        override fun onLoading(code: Int): Content.Loading {
            this.fragmentContentService!!.frameLayoutContent!!.visibility = View.GONE
            this.fragmentContentService!!.frameLayoutShutter!!.visibility = View.VISIBLE
            val fragment = getLoadingFragment(code)
            val loading = getLoading(code, fragment)
            fragment.childFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout_shutter, fragment)
                    .commit()
            return loading
        }

        companion object {
            protected fun inflate(container: ViewGroup, @LayoutRes res: Int): View {
                return LayoutInflater.from(container.context).inflate(res, container, false)
            }
        }

    }

}