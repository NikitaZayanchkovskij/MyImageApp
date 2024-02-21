package com.example.testtaskpexelsapp.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testtaskpexelsapp.constants.MyConstants
import com.example.testtaskpexelsapp.interfaces.IDaoForRoom
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels


@Database (entities = [ImageResourceFromPexels::class], version = 1)
abstract class MyRoomDataBase: RoomDatabase() {


    abstract fun receiveDaoInterface(): IDaoForRoom


    companion object {

        fun getDataBase(context: Context): MyRoomDataBase {

            return Room.databaseBuilder(context.applicationContext,
                MyRoomDataBase::class.java, MyConstants.MY_ROOM_DATABASE_NAME).build()
        }

    }


}