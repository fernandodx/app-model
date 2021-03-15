package br.com.diasdeseries.repository

import br.com.diasdeseries.data.db.entity.FavoriteSeriesEntity

interface FavoriteSeriesRepository {

    suspend fun insertFavoriteSeries(
        idSerieTv: Int,
        nameSerie: String,
        banner: String,
        thumb: String,
        rating: Double,
        countSeason: Int
    ): Long

    suspend fun deleteAllFavoriteSeries()

    suspend fun deleteFavoriteSeriesWithId(id: Long)

    suspend fun getAllFavoriteSeries(): List<FavoriteSeriesEntity>
}