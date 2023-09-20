package com.example.expiraton_date.sqliteRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expiraton_date.domain.ProductInfoDTO

@Database(entities = [ProductTable::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productTableDao():ProductTableDAO

    //Database 객체를 하나만 사용하기 위해 싱글톤으로 생성
    companion object{
        private var instance : ProductDatabase? = null
        fun getInstance(context : Context):ProductDatabase?{
            if(instance == null){
                instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        "product_database"
                ).build()
            }
            return instance
        }
    }

}