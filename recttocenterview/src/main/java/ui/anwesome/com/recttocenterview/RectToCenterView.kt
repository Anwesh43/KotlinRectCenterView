package ui.anwesome.com.recttocenterview

/**
 * Created by anweshmishra on 20/03/18.
 */
import android.app.Activity
import android.graphics.*
import android.content.*
import android.view.*
class RectToCenterView(ctx : Context) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {
        val scales : Array<Float> = arrayOf(0f, 0f)
        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if (animated) {
                try {
                    updatecb()
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex : Exception) {

                }
            }
        }
        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
    data class RectToCenter(var i : Int, val state : State = State()) {
        fun draw(canvas : Canvas, paint : Paint) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            val size = Math.min(w,h)/10
            canvas.save()
            canvas.translate(w/2, h/2)
            for(i in 0..1) {
                paint.color = Color.argb(100, 255 * i, 255 * (1 - i), 0)
                canvas.save()
                canvas.translate(size + (w - 2 * size) * i, size + (h - 2 * size) * i)
                canvas.scale(state.scales[0], state.scales[0])
                val x = ((w - 2 * size)/2) * (1 - 2 * i) * state.scales[1]
                val y = ((h - 2 * size)/2) * (1 - 2 * i) * state.scales[1]
                canvas.save()
                canvas.translate(x, y)
                canvas.drawRoundRect(RectF(-size/2, -size/2, size/2, size/2), size/4, size/4, paint)
                canvas.restore()
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view : RectToCenterView) {
        val rectToCenter = RectToCenter(0)
        val animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            rectToCenter.draw(canvas, paint)
            animator.animate {
                rectToCenter.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            rectToCenter.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity : Activity) : RectToCenterView {
            val view = RectToCenterView(activity)
            activity.setContentView(view)
            return view
        }
    }
}