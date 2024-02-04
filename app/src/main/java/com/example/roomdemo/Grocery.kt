package com.example.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groceries")
class Grocery {

    @PrimaryKey(autoGenerate = true)
    var id:Int=0

    @ColumnInfo(name = "GroceryName")
    var gName:String=""

    @ColumnInfo(name = "GroceryWt")
    var wt:Int=0

    constructor()
    constructor(gName: String, wt: Int){
        this.gName=gName
        this.wt=wt
    }
}