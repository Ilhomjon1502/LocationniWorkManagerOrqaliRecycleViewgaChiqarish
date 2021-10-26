package com.ilhomjon.serviceworkmanager.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyTimeDao {
    @Query("select * from mytime")
    fun getAllTime():List<MyTime>

    @Insert
    fun addTime(myTime: MyTime)
}