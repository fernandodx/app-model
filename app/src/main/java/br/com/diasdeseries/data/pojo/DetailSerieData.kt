package br.com.diasdeseries.data.pojo

import br.com.diasdeseries.data.pojo.SerieData.Schedule
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class DetailSerieData {

    @JsonProperty("id")
    var id: Int? = null

    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("type")
    var type: String? = null

    @JsonProperty("language")
    var language: String? = null

    @JsonProperty("genres")
    var genres: List<String>? = null

    @JsonProperty("status")
    var status: String? = null

    @JsonProperty("runtime")
    var runtime: Int? = null

    @JsonProperty("premiered")
    var premiered: String? = null

    @JsonProperty("officialSite")
    var officialSite: String? = null

    @JsonProperty("schedule")
    var schedule: Schedule? = null

    @JsonProperty("rating")
    var rating: SerieData.Rating? = null

    @JsonProperty("weight")
    var weight: Int? = null

    @JsonProperty("network")
    var network: SerieData.Network? = null

    @JsonProperty("webChannel")
    var webChannel: Any? = null

    @JsonProperty("image")
    var image: SerieData.Image? = null

    @JsonProperty("summary")
    var summary: String? = null

    @JsonProperty("updated")
    var updated: Int? = null

    @JsonIgnore
    var banner: String? = null

    @JsonIgnore
    var listaCoverEpisodes: List<EpisodesSerieData>? = null


}