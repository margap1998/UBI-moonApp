package com.example.apka

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.Month
import java.time.Year

class fullMoonIn : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun count(){
        val k = arrayOf<TextView>(d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12)
        val year = change(0)
        var month = 1
        while (month<13){
            var day = 0
            val num = Month.of(month).length(Year.of(year).isLeap)
            var ph = 0
            while (day<=num && ph!=15){
                day+=1
                ph = calcMoon(year,month,day)
            }
            k[month-1].text=LocalDate.of(year,month,day).toString()
            month+=1
        }
    }
    fun change(q:Int): Int {
        var year = yearField.text.toString().toInt()
        year += q
        if (year>2200) year = 2200
        else if (year<1900) year = 1900
        yearField.text.clear()
        yearField.text.append(year.toString())
        return year
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        countButton.setOnClickListener { count() }
        plus_year.setOnClickListener { change(1) }
        minus_year.setOnClickListener { change(-1) }
        count()
    }
}
