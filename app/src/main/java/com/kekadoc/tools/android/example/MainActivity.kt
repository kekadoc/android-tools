package com.kekadoc.tools.android.example

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.kekadoc.tools.android.dpToPx
import com.kekadoc.tools.android.lifecycle.onLifecycle
import com.kekadoc.tools.android.view.*
import com.kekadoc.tools.android.view.ViewUtils.findAllViews
import com.kekadoc.tools.android.view.recyclerview.SpacesItemDecoration
import com.kekadoc.tools.data.state.StateKeeper
import com.kekadoc.tools.data.state.dataStatesCollector
import com.kekadoc.tools.observer.Observing

@SuppressLint("NonConstantResourceId")
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainActivity-TAG"
    }

    @ViewById(id = R.id.view_0)
    lateinit var view0: View
    @ViewById(id = R.id.view_1)
    lateinit var view1: View
    @ViewById(id = R.id.view_2)
    lateinit var view2: View

    @ViewById(id = R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findAllViews()

        view0.background = RippleDrawable(ColorStateList.valueOf(Color.GREEN), ColorDrawable(Color.BLUE), null)
        view1.background = RippleDrawable(ColorStateList.valueOf(Color.GREEN), ColorDrawable(Color.BLUE), null)
        view2.background = RippleDrawable(ColorStateList.valueOf(Color.GREEN), ColorDrawable(Color.BLUE), null)

        val focusability = ViewFocusability.Auto.onClick<View>().apply {
            onLifecycle(
                addObserver(
                    onFocus = {
                        it.animate()
                            .scaleY(1.2f)
                            .scaleX(1.2f)
                            .alpha(1f)
                            .setDuration(200L)
                            .start()
                    },
                    onNormal = {
                        it.animate()
                            .scaleY(1.0f)
                            .scaleX(1.0f)
                            .alpha(1f)
                            .setDuration(200L)
                            .start()
                    },
                    onHide = {
                        it.animate()
                            .scaleY(0.8f)
                            .scaleX(0.8f)
                            .alpha(0.6f)
                            .setDuration(200L)
                            .start()
                    }
                )
            )
            add(view0)
            add(view1)
            add(view2)
        }

        val adapter = Adapter()
        val data = arrayListOf<Message>()
        for (i in 0..20) data.add(Message("Message #$i"))

        adapter.submitList(data)

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            SpacesItemDecoration(
                left = dpToPx(16f).toInt(),
                top = dpToPx(16f).toInt(),
                right = dpToPx(16f).toInt(),
                bottom = dpToPx(16f).toInt(),
            )
        )

    }

    private class VH(itemView: TextView) : RecyclerView.ViewHolder(itemView) {



        var data: StateKeeper<Message, Colors>? = null
        var observing: Observing? = null

        val rippleDrawable: RippleDrawable
        val backgroundDrawable: MaterialShapeDrawable
        val maskDrawable: MaterialShapeDrawable

        init {
            val context = itemView.context
            val shape = ShapeAppearanceModel.builder().setAllCorners(
                CornerFamily.ROUNDED, context.dpToPx(16f)
            ).build()
            backgroundDrawable = MaterialShapeDrawable(shape).apply {
                elevation = context.dpToPx(8f)
                shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
                setTint(Color.WHITE)
            }
            maskDrawable = MaterialShapeDrawable(shape)
            rippleDrawable = RippleDrawable(ColorStateList.valueOf(Color.RED), null, maskDrawable)
            val layerDrawable = LayerDrawable(arrayOf(backgroundDrawable, rippleDrawable))
            itemView.background = layerDrawable
        }

        fun invokeAction() {
            Log.e(TAG, "invokeAction: $data")
            data?.let {
                it.state = it.state.getNext()
            }
        }

        fun inject(data: StateKeeper<Message, Colors>) {
            this.data = data.apply {
                (itemView as TextView).text = this.data.message
                observing?.remove()
                observing = observe { oldState, newState ->
                    backgroundDrawable.setTint(newState.color)
                }
            }
        }

        fun focus() {
            itemView.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(1f)
                .setDuration(200L)
                .start()
        }
        fun hide() {
            itemView.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .alpha(0.6f)
                .setDuration(200L)
                .start()
        }
        fun reset() {
            itemView.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1f)
                .setDuration(200L)
                .start()
        }

    }

    private class Adapter : ListAdapter<Message, VH>(DiffCallback()) {

        companion object {
            const val STATE_NULL = -1
            const val STATE_NORMAL = 0
            const val STATE_FOCUS = 1
            const val STATE_HIDE = 2
        }

        private val messageStates = dataStatesCollector<Message, Colors>() { keeper, oldState, newState ->
            Log.e(TAG, "onStateChange $keeper old: $oldState new: $newState")
        }

        override fun onCurrentListChanged(
            previousList: MutableList<Message>, currentList: MutableList<Message>
        ) {

            messageStates.clear()
            currentList.forEach { messageStates.add(it, Colors.WHITE) }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val context = parent.context
            val view = TextView(context).apply {
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                isClickable = true
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    focusable = View.FOCUSABLE
                }
            }
            val lp = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, context.dpToPx(
                    56f
                ).toInt()
            )
            view.layoutParams = lp

            val vh = VH(view)

            view.setOnClickListener {
                vh.invokeAction()
            }

            return vh
        }
        override fun onBindViewHolder(holder: VH, position: Int) {
            val message = getItem(position)
            holder.inject(messageStates.getStateKeeper(message)!!)
        }

    }

    private class DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

}

data class Message(val message: String)
enum class Colors(val color: Int) {

    RED(Color.RED),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    BLACK(Color.BLACK),
    WHITE(Color.WHITE);

    fun getNext(): Colors {
        return when (this) {
            RED -> GREEN
            GREEN -> BLUE
            BLUE -> BLACK
            BLACK -> WHITE
            WHITE -> RED
        }
    }

}