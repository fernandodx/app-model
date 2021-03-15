package br.com.diasdeseries.data.network

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitUtil {

    companion object {

        @Volatile
        private var INSTANCE: Retrofit? = null

        private fun getInstanceApiTv() : Retrofit {
            synchronized(this) {
                var instance: Retrofit? = INSTANCE
                if(instance == null){
                    instance = Retrofit.Builder()
                        .baseUrl(" http://api.tvmaze.com/")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build()
                }
                return instance!!
            }
        }

        fun getSeriesServices() : SeriesService{
            return getInstanceApiTv().create(SeriesService::class.java)
        }

    }





}