package com.ilhomjon.serviceworkmanager.Service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.ilhomjon.serviceworkmanager.Room.AppDatabase
import com.ilhomjon.serviceworkmanager.Room.MyTime
import java.text.SimpleDateFormat
import java.util.*

class UploadWork(val context: Context, workerParameters: WorkerParameters)
    :Worker(context, workerParameters){
    private val TAG = "UploadWork"
    override fun doWork(): Result {

//        uploadTime()

        locationUpdates()
        return Result.success()
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadTime() {
        val time = SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date())
        Log.d(TAG, "uploadTime: $time")
        val myTime = MyTime()
        myTime.timeMoment = time
        val appDatabase = AppDatabase.getInstance(context)
        appDatabase.timeDao().addTime(myTime)
    }


    //location
    private lateinit var geocoder: Geocoder


    fun locationUpdates(){
        geocoder = Geocoder(context, Locale.getDefault())

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            //permission denied
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        for (i in 0..15) {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
            locationTask.addOnSuccessListener { it: Location ->
                if (it != null) {
                    //We have a location
                    Log.d(TAG, "getLastLocation: ${it.toString()}")
                    Log.d(TAG, "getLastLocation: ${it.latitude}")
                    Log.d(TAG, "getLastLocation: ${it.longitude}")
                    val appDatabase = AppDatabase.getInstance(context)
                    val myTime = MyTime()
                    val time = SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date())
                    Log.d(TAG, "uploadTime: $time")
                    myTime.timeMoment = "${it.latitude}, ${it.longitude} \n $time"
                    appDatabase.timeDao().addTime(myTime)
                } else {
                    Log.d(
                        TAG,
                        "getLastLocation: location was null,,,,,,,,,,,,,,,,,,,..............."
                    )
                }
            }
            locationTask.addOnFailureListener {
                Log.d(TAG, "getLastLocation: ${it.message}")
            }
            Thread.sleep(60000)
        }
    }


}