package br.com.diasdeseries.data.pojo

import com.fasterxml.jackson.annotation.JsonProperty


class ImageSerieData {

    @JsonProperty("id")
    var id: Int? = null

    @JsonProperty("type")
    var type: String? = null

    @JsonProperty("main")
    var main: Boolean? = null

    @JsonProperty("resolutions")
    var resolutions: Resolutions? = null


    class Resolutions {
        @JsonProperty("original")
        var original: Original? = null

        @JsonProperty("medium")
        var medium: Medium? = null
    }

    class Original {
        @JsonProperty("url")
        var url: String? = null
    }

    class Medium {
        @JsonProperty("url")
        var url: String? = null
    }
}