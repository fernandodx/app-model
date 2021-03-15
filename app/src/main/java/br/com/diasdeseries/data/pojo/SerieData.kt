package br.com.diasdeseries.data.pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
class SerieData {

    @JsonProperty("score")
    var score: Double? = null

    @JsonProperty("show")
    var show: Show? = null


    @JsonIgnoreProperties(ignoreUnknown = true)
    class Show {
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
        var rating: Rating? = null

         @JsonProperty("weight")
        var weight: Int? = null

         @JsonProperty("webChannel")
        var webChannel: WebChannel? = null

        @JsonProperty("network")
        var network: Network? = null

         @JsonProperty("image")
        var image: Image? = null

         @JsonProperty("summary")
        var summary: String? = null

         @JsonProperty("updated")
        var updated: Int? = null

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Country {
        @JsonProperty("name")
        var name: String? = null

        @JsonProperty("code")
        var code: String? = null

        @JsonProperty("timezone")
        var timezone: String? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Image {
        @JsonProperty("medium")
        var medium: String? = null

        @JsonProperty("original")
        var original: String? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
   class Rating {
        @JsonProperty("average")
        var average: Double? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Schedule {
        @JsonProperty("time")
        var time: String? = null

        @JsonProperty("days")
        var days: List<String>? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class WebChannel {
        @JsonProperty("id")
        var id: Int? = null

        @JsonProperty("name")
        var name: String? = null

        @JsonProperty("country")
        var country: Country? = null
    }

    class Network {
        @JsonProperty("id")
        var id: Int? = null

        @JsonProperty("name")
        var name: String? = null

        @JsonProperty("country")
        var country: Country? = null
    }

}
