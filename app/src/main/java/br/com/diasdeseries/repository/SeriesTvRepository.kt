package br.com.diasdeseries.repository

import br.com.diasdeseries.data.pojo.*
import retrofit2.http.GET
import retrofit2.http.Path

interface SeriesTvRepository {

    suspend fun findSeriesWithName(name : String) : List<SerieData>

    suspend fun getSeriesNow() : List<SerieNowData>

    suspend fun getDetailSerieWithId(id : Int) : DetailSerieData

    suspend fun getImagesWithId(id: Int) : List<ImageSerieData>

    suspend fun getEpisodesWithId(id: Int) : List<EpisodesSerieData>

    suspend fun getSeasonsWithId(id: Int) : List<SeasonSerieData>

    suspend fun findAllSeriesWithPagination(page : Int) : List<SerieData.Show>


}