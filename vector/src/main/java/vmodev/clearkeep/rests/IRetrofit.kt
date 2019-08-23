package vmodev.clearkeep.rests

import retrofit2.Retrofit

interface IRetrofit {
    fun getRetrofit(): Retrofit;

    companion object {
        const val BASE_URL_HOME_SERVER = "BASE_URL_HOME_SERVER";
        const val BASE_URL_CLEAR_KEEP_SERVER = "BASE_URL_CLEAR_KEEP_SERVER";
    }
}