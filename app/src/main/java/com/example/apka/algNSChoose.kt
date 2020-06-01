package com.example.apka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alg_n_s_choose.*
import java.io.File

class algNSChoose : AppCompatActivity() {
    fun check(){
        var algName =""
        when(alg){
            0 -> algName = "podstawowy"
            3 -> algName = "Conwaya"
            1 -> algName = "trygonometryczny 1"
            2 -> algName = "trygonometryczny 2"
        }
        algorithmTV.text = "Algorytm "+algName
        when(halfSphere){
            'n' -> sphereTV.text = "Pólkula północna"
            's' -> sphereTV.text = "Pólkula południowa"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alg_n_s_choose)
        saveButton.setOnClickListener { saveSetting(this)
            check() }
        trig1.setOnClickListener { alg = 1
            check()}
        trig2.setOnClickListener { alg = 2
            check()}
        conway.setOnClickListener { alg = 3
            check()}
        simple.setOnClickListener { alg = 0
            check()}
        halfSphereButton.setOnClickListener { if (halfSphere == 's') halfSphere='n' else halfSphere='s'
            check()}
        check()
    }

}
