package br.com.diasdeseries.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.diasdeseries.data.db.entity.FavoriteSeriesEntity

@Dao
interface FavoriteSeriesDAO {

    @Insert
    suspend fun insert(favorite : FavoriteSeriesEntity) : Long

    @Query("DELETE FROM favorite_series WHERE id = :id")
    suspend fun delete(id : Long)

    @Query("DELETE FROM favorite_series")
    suspend fun deleteAll()

    @Query("SELECT * FROM favorite_series")
    suspend fun getAll() : List<FavoriteSeriesEntity>?

    @Query("SELECT * FROM favorite_series WHERE idSerieTv = :idSerieTv")
    suspend fun getFavoriteSerieWithIdSerieTv(idSerieTv: Int): FavoriteSeriesEntity?

    @Query("DELETE FROM favorite_series WHERE idSerieTv = :idSerieTv")
    suspend fun deleteFavoriteSeriesWithIdSerieTv(idSerieTv: Int)


}