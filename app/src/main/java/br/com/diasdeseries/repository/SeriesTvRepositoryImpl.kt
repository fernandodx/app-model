package br.com.diasdeseries.repository

import br.com.diasdeseries.data.network.RetrofitUtil
import br.com.diasdeseries.data.pojo.*

class SeriesTvRepositoryImpl : SeriesTvRepository {

    override suspend fun findSeriesWithName(name: String): List<SerieData> {
        return RetrofitUtil.getSeriesServices().findSeries(nameSerie = name)
    }

    override suspend fun getSeriesNow(): List<SerieNowData> {
       return RetrofitUtil.getSeriesServices().getSeriesNow()
    }

    override suspend fun getDetailSerieWithId(id: Int): DetailSerieData {
        return RetrofitUtil.getSeriesServices().getDetailSerieWithId(id)
    }

    override suspend fun getImagesWithId(id: Int): List<ImageSerieData> {
       return RetrofitUtil.getSeriesServices().getImagesSerieWithId(id)
    }

    override suspend fun getEpisodesWithId(id: Int): List<EpisodesSerieData> {
       return RetrofitUtil.getSeriesServices().getEpisodesWithId(id)
    }

    override suspend fun getSeasonsWithId(id: Int): List<SeasonSerieData> {
        return RetrofitUtil.getSeriesServices().getSeasonsWithId(id)
    }
}