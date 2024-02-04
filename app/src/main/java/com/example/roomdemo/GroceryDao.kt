package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroceryDao {

    @Insert
    fun addGrocery(grocery: Grocery)

    @Query("SELECT * FROM groceries WHERE GroceryName LIKE :gName || '%'")
    fun findGrocery(gName: String):List<Grocery>

    @Query("SELECT * FROM groceries")
    fun getAllGrocery():LiveData<List<Grocery>>

    @Query("DELETE FROM groceries WHERE GroceryName=:gName")
    fun deleteGrocery(gName: String)


}