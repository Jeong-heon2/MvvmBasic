package com.example.mvvmex01

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
class Contact (
    @PrimaryKey(autoGenerate = true)
    var id : Long?,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "number")
    var number: String,

    @ColumnInfo(name = "initial")
    var initial: Char

){
    constructor() : this(null,"","",'\u0000')
}