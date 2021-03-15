package br.com.diasdeseries.data.pojo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty



@JsonIgnoreProperties(ignoreUnknown = true)
class EpisodesSerieData {

    @JsonProperty("id")
    var id: Int? = null

    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("season")
    var season: Int? = null

    @JsonProperty("number")
    var number: Int? = null

    @JsonProperty("type")
    var type: String? = null

    @JsonProperty("airdate")
    var airdate: String? = null

    @JsonProperty("airtime")
    var airtime: String? = null

    @JsonProperty("airstamp")
    var airstamp: String? = null

    @JsonProperty("runtime")
    var runtime: Int? = null

    @JsonProperty("image")
    var image: SerieData.Image? = null

    @JsonProperty("summary")
    var summary: String? = null

    @JsonIgnore
    var countEpisodes: Int? = null

}