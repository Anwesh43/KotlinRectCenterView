package ui.anwesome.com.kotlinrecttocenterview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.recttocenterview.RectToCenterView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RectToCenterView.create(this)
    }
}
