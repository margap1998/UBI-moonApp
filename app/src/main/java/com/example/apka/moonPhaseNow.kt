package com.example.apka

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_moon_phase_now.*
import java.lang.StringBuilder
import java.net.URI
import java.time.LocalDate
import kotlin.math.abs


class moonPhaseNow : AppCompatActivity() {

    var ph:Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNewMoonBefore(): LocalDate? {
        var date = LocalDate.now()
        var my_ph = ph
        if (ph == 0 || ph == 29) my_ph=15
        while (!(my_ph == 0 ||my_ph==29) ){
            date = date.minusDays(1)
            my_ph = calcMoon(date.year,date.month.value,date.dayOfMonth)
        }
        return date
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNewMoonAfter(): LocalDate? {
        var date = LocalDate.now()
        var my_ph = ph
        if (ph == 0 || ph == 29) my_ph=15
        while (!(my_ph == 0 ||my_ph==29) ){
            date = date.plusDays(1)
            my_ph = calcMoon(date.year,date.month.value,date.dayOfMonth)
        }
        return date
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun phaser(){
        val now = LocalDate.now()
        ph = calcMoon(now.year,now.month.value,now.dayOfMonth)

        val nameof = StringBuilder()
        nameof.append(halfSphere)
        nameof.append(ph)
        val obr = nameof.toString()
        val id = imageView.resources.getIdentifier(obr,"drawable",this.packageName)
        imageView.setImageResource(id)

        todayTV.text = "Dzisiaj: " + (ph *100/29).toString() + "%"
        beforeTV.text= "Poprzedni nów: "+ getNewMoonBefore().toString()
        nextTV.text = "Następny nów: "+ getNewMoonAfter().toString()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_moon_phase_now)
        checkSetting(this)
        phaser()
    }

    fun settings(view:View){
        val intent = Intent(this,algNSChoose::class.java)
        startActivity(intent)
    }

    fun calcs(view:View){
        val intent = Intent(this,fullMoonIn::class.java)
        startActivity(intent)
    }

}
