package br.com.diasdeseries.data.network

import br.com.diasdeseries.data.pojo.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesService {

    @GET("/search/shows")
    suspend fun findSeries(@Query("q") nameSerie : String) : List<SerieData>

    @GET("/schedule")
    suspend fun getSeriesNow() : List<SerieNowData>

    @GET("/shows/{id}")
    suspend fun getDetailSerieWithId(@Path("id") id : Int) : DetailSerieData

    @GET("/shows/{id}/images")
    suspend fun getImagesSerieWithId(@Path("id") id: Int) : List<ImageSerieData>

    @GET("/shows/{id}/episodes")
    suspend fun getEpisodesWithId(@Path("id") id: Int) : List<EpisodesSerieData>

    @GET("/shows/{id}/seasons")
    suspend fun getSeasonsWithId(@Path("id") id: Int) : List<SeasonSerieData>


}