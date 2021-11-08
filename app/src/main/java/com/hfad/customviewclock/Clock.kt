package com.hfad.customviewclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class Clock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circle = Paint()
    private var point = Paint()
    private var line = Paint()

    private var degreeHour = 0
    private var degreeMin = 0
    private var degreeSec = 0

    private var calendar = Calendar.getInstance()

    private var centerX = 0F
    private var centerY = 0F
    private var radius = 0F


    private fun initdata() {

        getTime()

        circle = Paint().apply {
            color = BLACK
            style = STROKE
            strokeWidth = 10F
        }
        point = Paint().apply {
            strokeWidth = 30F
            strokeCap = Paint.Cap.ROUND
        }
        line = Paint().apply {
            color = BLACK
        }

        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        radius = centerX - 10 * circle.strokeWidth

        handler.sendEmptyMessageDelayed(1, 1000)
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            getTime()
            invalidate()
        }
    }

    private fun getTime() {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"))
        degreeSec = calendar.get(Calendar.SECOND) * 6
        degreeMin = calendar.get(Calendar.MINUTE) * 6

        degreeHour = if (calendar.get(Calendar.HOUR) > 12) {
            (calendar.get(Calendar.HOUR) - 12) * 30
        } else {
            calendar.get(Calendar.HOUR) * 30
        }
    }

    private fun drawCalibration(canvas: Canvas) {

        canvas.drawCircle(centerX, centerY, radius, circle)

        for (i in 1..12) {
            line.strokeWidth = 10F
            canvas.drawLine(
                centerX + (0.9 * radius * sin(Math.toRadians(i * 30.toDouble()))).toFloat(),
                centerY - (0.9 * radius * cos(Math.toRadians(i * 30.toDouble()))).toFloat(),
                centerX + (radius * sin(Math.toRadians(i * 30.toDouble()))).toFloat(),
                centerY - (radius * cos(Math.toRadians(i * 30.toDouble()))).toFloat(),
                line
            )
            line.textSize = 40F
            canvas.drawText("" + i,
                centerX + (0.7 * radius * sin(Math.toRadians(i * 30.toDouble()))).toFloat(),
                centerY - (0.7 * radius * cos(Math.toRadians(i * 30.toDouble()))).toFloat(),
                line
            )
        }

        for (i in 1..60) {
            line.strokeWidth = 5F
            canvas.drawLine(
                centerX + (0.95 * radius * sin(Math.toRadians(i * 6.toDouble()))).toFloat(),
                centerY - (0.95 * radius * cos(Math.toRadians(i * 6.toDouble()))).toFloat(),
                centerX + (radius * sin(Math.toRadians(i * 6.toDouble()))).toFloat(),
                centerY - (radius * cos(Math.toRadians(i * 6.toDouble()))).toFloat(),
                line
            )
        }
    }

    private fun drawLine(canvas: Canvas) {
        canvas.apply {
            line.strokeWidth = 20F
            line.color = BLUE
            canvas.drawLine(
                centerX,
                centerY,
                centerX + (0.5 * radius * sin(Math.toRadians(degreeHour.toDouble()))).toFloat(),
                centerY - (0.5 * radius * cos(Math.toRadians(degreeHour.toDouble()))).toFloat(),
                line
            )

            line.strokeWidth = 10F
            line.color = RED
            canvas.drawLine(
                centerX,
                centerY,
                centerX + (0.7 * radius * sin(Math.toRadians(degreeMin.toDouble()))).toFloat(),
                centerY - (0.7 * radius * cos(Math.toRadians(degreeMin.toDouble()))).toFloat(),
                line
            )

            line.strokeWidth = 5F
            line.color = GREEN
            canvas.drawLine(
                centerX,
                centerY,
                centerX + (radius * sin(Math.toRadians(degreeSec.toDouble()))).toFloat(),
                centerY - (radius * cos(Math.toRadians(degreeSec.toDouble()))).toFloat(),
                line
            )
        }
        canvas.drawPoint(centerX, centerY, point)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        initdata()
        drawCalibration(canvas)
        drawLine(canvas)
    }
}