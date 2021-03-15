package br.com.diasdeseries.data.db.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.diasdeseries.data.db.dao.FavoriteSeriesDAO


@Database(entities = [FavoriteSeriesEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            synchronized(this) {
                var instance: AppDatabase? = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context,
                                                    AppDatabase::class.java,
                                                    "br.com.diasdeseries_database")
                                                    .build()
                }
                return instance
            }
        }
    }

    abstract val favoriteSeriesDAO : FavoriteSeriesDAO





}