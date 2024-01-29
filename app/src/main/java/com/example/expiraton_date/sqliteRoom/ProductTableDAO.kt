package com.example.expiraton_date.sqliteRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductTableDAO {
    //Insert Product Query
    @Insert
    fun saveProduct(product : ProductTable)

    //Edit Product Query
    //Update annotation is updated column by primary key
    @Update
    fun editProduct(product : ProductTable)

    @Query("SELECT * FROM productTable")
    fun getAllItem():MutableList<ProductTable>

    @Query("DELETE FROM productTable where id = :id")
    fun deleteItem(id : Int)

    @Query("SELECT * FROM productTable WHERE productName = :productName")
    fun getItemWithName(productName : String) : ProductTable

    @Query("SELECT * FROM productTable WHERE productExpirationDate = :date")
    fun getItemWithExpirationDate(date : String) : MutableList<ProductTable>
}