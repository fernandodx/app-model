package br.com.diasdeseries.data.pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty



@JsonIgnoreProperties(ignoreUnknown = true)
class SeasonSerieData {

    @JsonProperty("id")
    var id: Int? = null

    @JsonProperty("number")
    var number: Int? = null

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("episodeOrder")
    var episodeOrder: Int? = null

    @JsonProperty("premiereDate")
    var premiereDate: String? = null

    @JsonProperty("endDate")
    var endDate: String? = null

}