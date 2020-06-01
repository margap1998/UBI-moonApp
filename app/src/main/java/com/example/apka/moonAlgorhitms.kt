package com.example.apka

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Math.sin
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor


var alg: Int = 2
var halfSphere: Char ='n'

fun calcMoon(year: Int,month: Int,day: Int): Int {
    var r: Int = 1
    when(alg){
        0 -> r = moonSimple(year,month,day)
        1 -> r = moonTrig1(year,month,day)
        2 -> r = moonTrig2(year,month,day)
        3 -> r = moonConway(year,month,day)
    }

    if (r<0) r = 0 else if (r>29) r = 29
    return r
}

fun moonSimple(year: Int,month: Int,day: Int): Int {
    val lp = 2551443;
    val now = Date(year,month-1,day,20,35,0);
    val newMoon = Date(1970, 0, 7, 20, 35, 0);
    val phase = ((now.time - newMoon.time)/1000) % lp;
    return (Math.floor((phase /(24*3600)).toDouble()).toInt() + 1)
}

fun moonConway(year: Int,month: Int,day: Int):  Int {
    var r = year % 100
    r %= 19;
    if(r>9){ r -=19}
    r = ((r * 11) % 30) + month + day;
    if (month<3)
        r += 2
    var rd = r*1.0;
    if (year<2000) {
        rd -= 4
    }
    else
        rd-=8.3
    r = (Math.floor(rd+0.5).toInt())%30;
    if (r<0)
        return (r+30)
    else
        return r
}
fun moonTrig1(year: Int,month: Int,day: Int): Int {

    val thisJD = julday(year,month,day);
    val degToRad = 3.14159265 / 180;
    val K0 = floor((year-1900)*12.3685);
    val T = (year-1899.5) / 100;
    val T2 = T*T;
    val T3 = T*T*T;
    val J0 = 2415020 + 29*K0;
    val F0 = 0.0001178*T2 - 0.000000155*T3 + (0.75933 + 0.53058868*K0) - (0.000837*T + 0.000335*T2);
    val M0 = 360*(GetFrac(K0*0.08084821133)) + 359.2242 - 0.0000333*T2 - 0.00000347*T3;
    val M1 = 360*(GetFrac(K0*0.07171366128)) + 306.0253 + 0.0107306*T2 + 0.00001236*T3;
    val B1 = 360*(GetFrac(K0*0.08519585128)) + 21.2964 - (0.0016528*T2) - (0.00000239*T3);
    var phase = 0;
    var jday = 0;
    var oldJ = 0
    while (jday < thisJD) {
        var F = F0 + 1.530588*phase;
        val M5 = (M0 + phase*29.10535608)*degToRad;
        val M6 = (M1 + phase*385.81691806)*degToRad;
        val B6 = (B1 + phase*390.67050646)*degToRad;
        F = F.minus(0.4068* sin(M6) + (0.1734 - 0.000393*T)* sin(M5));
        F = F.plus(0.0161* sin(2*M6) + 0.0104* sin(2*B6));
        F = F.minus(0.0074* sin(M5 - M6) - 0.0051* sin(M5 + M6));
        F = F.plus(0.0021* sin(2*M5) + 0.0010* sin(2*B6-M6));
        F = F.plus(0.5 / 1440);
        oldJ = jday;
        jday = (J0 + 28*phase + floor(F)).toInt();
        phase++;
    }
    return (((thisJD-oldJ)%30).toInt());
}

fun moonTrig2(year: Int,month: Int,day: Int): Int {
    var n = floor(12.37 * (year -1900 + ((1.0 * month - 0.5)/12.0)));
    var RAD = 3.14159265/180.0;
    var t = n / 1236.85;
    var t2 = t * t;
    var asV = 359.2242 + 29.105356 * n;
    var am = 306.0253 + 385.816918 * n + 0.010730 * t2;
    var xtra = 0.75933 + 1.53058868 * n + ((1.178e-4) - (1.55e-7) * t) * t2;
    xtra += (0.1734 - 3.93e-4 * t) * sin(RAD * asV) - 0.4068 * sin(RAD * am);
    var i = 0.0
    if (xtra > 0.0) i = floor(xtra) else i = ceil(xtra - 1.0)
    var j1 = julday(year,month,day);
    var jd = (2415020 + 28 * n) + i;
    return ((j1-jd + 30)%30).toInt();
}


fun GetFrac(fr: Double): Double {	return (fr - Math.floor(fr));}

fun julday(Y: Int, M: Int, D: Int) : Double{
    var year = Y
    var month = M
    var day = D
    if (year < 0) { year ++; }
    month +=1
    if (month <= 2) {
        year--;
        month += 12;
    }
    var jul = Math.floor(365.25 *year) + floor(30.6001 * month) + day + 1720995;
    if (day+31*(month+12*year) >= (15+31*(10+12*1582))) {
        var ja = floor(0.01 * year);
        jul = jul + 2 - ja + floor(0.25 * ja);
    }
    return jul;
}

fun saveSetting(a:Context) {
    val filename = "calcMoonSettings"
    val sFM = "calcMoonSettings.dat"
    var f = File(a.filesDir, filename)
    if (!f.exists()) f.mkdir()
    try{
        val w = a.openFileOutput(sFM,Context.MODE_PRIVATE)
        w.write(halfSphere.toInt())
        w.write(alg)
        w.close()
    }catch (e:Exception){
        e.printStackTrace();
    }
}


fun checkSetting(a:Context){
    val filename ="calcMoonSettings"
    val sFM ="calcMoonSettings.dat"
    val f =File(a.filesDir,filename)
    if  (!f.exists()) f.mkdir()
    var rf = File(filename,sFM)
    try{
        val w = a.openFileInput(sFM)
        val a = arrayOf(w.read(),w.read())
        if (a[0] == 's'.toInt()) halfSphere = 's' else halfSphere = 'n'
        if (a[1]==1) alg = 1
        else if (a[1]==2) alg = 2
        else if (a[1]==3) alg = 3
        else alg = 0
        w.close()
    }catch (e:Exception){
        e.printStackTrace();
    }
}