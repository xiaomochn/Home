package com.xiaomo.home.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.Touch
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var heigth = 0
    var width = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello.text = "nononasd"
        hello.setBackgroundColor(Color.RED)
//        hello.setOnClickListener { start3() }
        hello.setOnTouchListener(View.OnTouchListener { v, event -> start3() })

        val dm = resources.displayMetrics
        hello.width = 100
        hello.height = 100
//        heigth-=100
//        width-=100
        heigth = dm.heightPixels - 300
        width = dm.widthPixels - 100
    }
    fun startYourShow(){
        val anim = ValueAnimator.ofInt(0,10)
        anim.duration = 500
        anim.startDelay = 500
        anim.repeatCount = 0
        anim.repeatMode = ValueAnimator.RESTART
        anim.addUpdateListener { animation->
            animation.animatedValue
            Log.d("animation.animatedValue",""+animation.animatedValue )
            hello.layoutParams.height = hello.layoutParams.height + animation.animatedValue as Int
            hello.requestLayout()
        }
        anim.start()


    }
    fun startYourShow2(){
        hello.text="asdfasdf"

        hello.setBackgroundColor(Color.RED)
        hello.layoutParams.height
        val oa = ObjectAnimator.ofInt(hello,"height",10,200)

        oa.duration = 1000

        oa.start()

    }
    fun start3(){

        Log.d("heigth",heigth.toString())
        Log.d("width",width.toString())
        Log.d("xxxxx",(Math.random()*width).toString())
        hello.animate()
                .setDuration(100)
//                .translationX(width.toFloat())
//                .translationY(heigth.toFloat())
                .translationX((Math.random()*width).toFloat())
                .translationY((Math.random()*heigth).toFloat())
                .start()
        return true
    }

}
