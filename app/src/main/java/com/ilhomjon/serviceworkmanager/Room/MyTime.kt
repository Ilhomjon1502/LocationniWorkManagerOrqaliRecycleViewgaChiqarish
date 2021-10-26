package com.ilhomjon.serviceworkmanager.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MyTime {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    var timeMoment:String? = null
}