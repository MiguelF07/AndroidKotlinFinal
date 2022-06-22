package com.example.androidkotlinfinal.data.model

class Stopwatch (val milliseconds : Long) {
    var seconds:Long = milliseconds / 1000 % 60
    var minutes:Long = milliseconds / 60000 % 60
    var hours:Long = milliseconds / 3600000 % 24

    fun setNewParameters(newMilliseconds:Long){
        seconds = (newMilliseconds / 1000) % 60
        minutes = (newMilliseconds / 60000) % 60
        hours = (newMilliseconds / 3600000) % 24
    }
}