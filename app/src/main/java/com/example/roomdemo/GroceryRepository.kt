package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GroceryRepository(private val groceryDao: GroceryDao) {
    val allGrocery:LiveData<List<Grocery>> = groceryDao.getAllGrocery()
    val searchGrocery=MutableLiveData<List<Grocery>>()
    private val coroutineScope= CoroutineScope(Dispatchers.Main)

    fun addGrocery(grocery: Grocery){
        coroutineScope.launch(Dispatchers.IO) {
            groceryDao.addGrocery(grocery)
        }
    }
    fun deleteGrocery(gName: String){
        coroutineScope.launch(Dispatchers.IO) {
            groceryDao.deleteGrocery(gName)
        }
    }

    fun searchGrocery(gName: String){
        coroutineScope.launch(Dispatchers.Main){
            searchGrocery.value=asyncFind(gName).await()
        }
    }

    private fun asyncFind(gName: String):Deferred<List<Grocery>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async groceryDao.findGrocery(gName)
        }

}