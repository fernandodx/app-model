package br.com.diasdeseries.repository

import br.com.diasdeseries.data.db.dao.FavoriteSeriesDAO
import br.com.diasdeseries.data.db.entity.FavoriteSeriesEntity

class FavoriteSeriesRoomDataSource(
    private val favoriteSeriesDAO: FavoriteSeriesDAO
) : FavoriteSeriesRepository {

    override suspend fun insertFavoriteSeries(
        idSerieTv: Int,
        nameSerie: String,
        banner: String,
        thumb: String,
        rating: Double,
        countSeason: Int
    ): Long {
        val favoriteSeriesEntity = FavoriteSeriesEntity(
            idSerieTv = idSerieTv,
            nameSerie = nameSerie,
            banner = banner,
            thumb = thumb,
            rating = rating,
            countSeason = countSeason
        )
        return favoriteSeriesDAO.insert(favoriteSeriesEntity)
    }

    override suspend fun deleteAllFavoriteSeries() {
        favoriteSeriesDAO.deleteAll()
    }

    override suspend fun deleteFavoriteSeriesWithId(id: Long) {
        favoriteSeriesDAO.delete(id)
    }

    override suspend fun getAllFavoriteSeries(): List<FavoriteSeriesEntity> {
        return favoriteSeriesDAO.getAll()
    }
}