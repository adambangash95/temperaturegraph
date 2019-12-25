package com.example.fyp

import android.os.Build
import android.util.Log
import com.example.fyp.Model.PointValue
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by u.zafar
 * on 25 Dec, 2019
 * at 23:01
 */
class GraphHandler(private val series: LineGraphSeries<DataPointInterface>) {


    internal var sdf = SimpleDateFormat("hh:mm")
    internal var myList = ArrayList<DataPoint>()
    val minInADay = 1440


    fun updateGraph(celsiusReading: Int) {
        val x = Date().time

        if (firstPoint()) {
            copyToEntire24Hours(celsiusReading)
        }

        val pointValue = PointValue(x, celsiusReading) // Are we getting the data? is your arduino active? yes my arduino is active. I dont seem to  be getting any values

        myList.removeAt(0)
        addDataPoint(pointValue)

    }

    private fun firstPoint() = myList.isEmpty()

    private fun copyToEntire24Hours(celsiusReading: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (0 until minInADay).forEach {
                val x = (Date().time - minInADay.convertToMinFromMillis()) + it.convertToMinFromMillis()
                val y = celsiusReading
                addDataPoint(PointValue(x,y))
            }
        }
    }

    private fun addDataPoint(pointValue: PointValue) {
        val newDataPoint = DataPoint(pointValue.getxValue().toDouble(), pointValue.getyValue().toDouble())
        myList.add(newDataPoint)

        val array = myList.toTypedArray()

        logit("Adding point: x: " + pointValue.getxValue() + ", y: " + pointValue.getyValue())
        series.resetData(array)
    }

    private fun logit(s: String) {
        Log.d("GRAPH", s)
    }

    fun Int.convertToMinFromMillis() = this * 1000 * 60
}