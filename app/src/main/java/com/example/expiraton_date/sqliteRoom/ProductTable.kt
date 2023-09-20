package com.example.expiraton_date.sqliteRoom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "productTable")
data class ProductTable(
        @PrimaryKey(autoGenerate = true)
        val id : Int = 0,
        @ColumnInfo(name = "productName")
        val productName : String,
        @ColumnInfo(name = "productExpirationDate")
        val productExpirationDate : String,
        @ColumnInfo(name = "category")
        val category : String,
        @ColumnInfo(name = "savePlace")
        val savePlace : String,
        @ColumnInfo(name = "productImage")
        val productImage : Int,
        
    )