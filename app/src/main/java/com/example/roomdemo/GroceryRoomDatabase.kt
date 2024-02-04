package com.example.roomdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Grocery::class], version = 1)
abstract class GroceryRoomDatabase : RoomDatabase() {
    abstract fun groceryDao(): GroceryDao
    companion object {
        private var INSTANCE: GroceryRoomDatabase? = null
        fun getInstance(context: Context): GroceryRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance =
                        Room.databaseBuilder(context.applicationContext, GroceryRoomDatabase::class.java, name = "grocery_db")
                            .fallbackToDestructiveMigration().build()
                    INSTANCE=instance

                }
                return instance
            }
        }
    }

}