package com.ilhomjon.serviceworkmanager

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.ilhomjon.serviceworkmanager.Adapters.MyTimeAdapter
import com.ilhomjon.serviceworkmanager.Room.AppDatabase
import com.ilhomjon.serviceworkmanager.Room.MyTime
import com.ilhomjon.serviceworkmanager.Service.UploadWork
import com.ilhomjon.serviceworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var myTimeAdapter: MyTimeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //minutiga bir marta bersam o'rtacha bir soatda backgrounda ishladi
        binding.btnStart.setOnClickListener {
            askPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION){
                //all permissions already granted or just granted

                val workRequest : WorkRequest = PeriodicWorkRequestBuilder<UploadWork>(15, TimeUnit.MINUTES)
//                .setInitialDelay(10, TimeUnit.SECONDS)
                    .build()
                WorkManager.getInstance(this)
                    .enqueue(workRequest)
                Toast.makeText(this, "Location aniqlash yoqildi gps ni o'chirmang orqa fonda ham ishlaydi", Toast.LENGTH_LONG).show()
            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(this)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if (e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }

        }

        val appDatabase = AppDatabase.getInstance(this)
        val list = ArrayList<MyTime>()
        list.addAll(appDatabase.timeDao().getAllTime())
        myTimeAdapter = MyTimeAdapter(list)
        binding.rv.adapter = myTimeAdapter
    }
}