package br.com.diasdeseries.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "favorite_series")
data class FavoriteSeriesEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idSerieTv : Int?,
    val nameSerie : String?,
    val banner : String?,
    val thumb : String?,
    val rating : Double?,
    val countSeason : Int?

)