package com.example.testproject.model


import android.os.StrictMode
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainModel : BaseObservable() {

    var out : PrintWriter? = null
    var executor :ExecutorService? = null
    lateinit var socket :Socket

    @get:Bindable
    var connectButton = "connect in model"
        set(value) {
            println("connected flaggggggggggggggggggggg")
            field = value
            notifyPropertyChanged(BR.loginviewmodel)
            notifyChange()
        }


//    private val _isConnectedModel = MutableLiveData<String>()
//    val isConnectModel : LiveData<String>
//        get() = _isConnectedModel




    // 127.0.0.1
    // 6400
    // 192.168.83.1

    init {
        executor = Executors.newSingleThreadExecutor()
    }

    fun disconnectMe() {
        socket.close()
    }

    fun updateThrottleModel(value :Int) {
        executor?.execute {
            val v = (value / 10000f)
            println(v)
            out?.print("set /controls/engines/current-engine/throttle $v\r\n")
            out?.flush()
        }
    }

    fun updateRudderModel(value :Int) {
        executor?.execute {
            val v = (value / 10000f) - 1
            println(v)
            out?.print("set /controls/flight/rudder $v\r\n")
            out?.flush()
        }
    }

    fun updateAileronAndElevatorModel(x: Float, y: Float, radius: Float, width: Int, height: Int) {
        executor?.execute {
            var aileron = (x - (width / 2)) / radius
            var elevator = (((height / 2) - y) / radius)
            if (aileron > 1)
                aileron = 1.0f
            else if (aileron < -1)
                aileron = -1.0f
            if (elevator > 1)
                elevator = 1.0f
            else if (elevator < -1)
                elevator = -1.0f
            println(aileron)
            println(elevator)
            out?.print("set /controls/flight/aileron $aileron\r\n")
            out?.flush()
            out?.print("set /controls/flight/elevator $elevator\r\n")
            out?.flush()
        }
    }


    fun connectClickedModel(ip: String, port: Int) {
        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(threadPolicy)
        socket = Socket(ip, port)
        out = PrintWriter(socket.getOutputStream(), true)
    }


}