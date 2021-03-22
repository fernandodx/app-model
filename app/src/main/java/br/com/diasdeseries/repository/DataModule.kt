package br.com.diasdeseries.repository

import br.com.diasdeseries.data.db.dao.FavoriteSeriesDAO
import br.com.diasdeseries.data.db.entity.AppDatabase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun providerFavoriteSerieDataSource(repository : FavoriteSeriesRoomDataSource) : FavoriteSeriesRepository

    @Singleton
    @Binds
    abstract fun providerSerieTvRepository(repository : SeriesTvRepositoryImpl) : SeriesTvRepository



}