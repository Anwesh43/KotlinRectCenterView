package ui.anwesome.com.recttocenterview

/**
 * Created by anweshmishra on 20/03/18.
 */
import android.graphics.*
import android.content.*
import android.view.*
class RectToCenterView(ctx : Context) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}