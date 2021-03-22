package br.com.diasdeseries.data

import android.content.Context
import br.com.diasdeseries.data.db.dao.FavoriteSeriesDAO
import br.com.diasdeseries.data.db.entity.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class RoomModule {

    @Provides
    fun provideFavoriteDao(context: Context) : FavoriteSeriesDAO {
        return AppDatabase.getInstance(context).favoriteSeriesDAO
    }

}