package com.example.roomdemo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application):ViewModel() {
    val allGrocery:LiveData<List<Grocery>>
    private val repository:GroceryRepository
    val searchResult:MutableLiveData<List<Grocery>>
    init {
        val groceryDb=GroceryRoomDatabase.getInstance(application)
        val groceryDao=groceryDb.groceryDao()
        repository=GroceryRepository(groceryDao)
        allGrocery=repository.allGrocery
        searchResult=repository.searchGrocery
    }
    fun addGrocery(grocery:Grocery){
        repository.addGrocery(grocery)
    }
    fun findGrocery(gName:String){
        repository.searchGrocery(gName)
    }
    fun deleteGrocery(gName:String){
        repository.deleteGrocery(gName)
    }

}