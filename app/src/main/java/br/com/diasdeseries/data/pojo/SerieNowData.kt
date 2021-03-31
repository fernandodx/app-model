package br.com.diasdeseries.data.pojo

import br.com.diasdeseries.data.pojo.SerieData.Show
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class SerieNowData {

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
    var image: Any? = null

    @JsonProperty("summary")
    var summary: Any? = null

    @JsonProperty("show")
    var show: Show? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SerieNowData

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }


}