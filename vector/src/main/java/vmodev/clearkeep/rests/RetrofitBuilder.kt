package vmodev.clearkeep.rests

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder constructor(baseUrl: String) : IRetrofit {
    private var retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    override fun getRetrofit(): Retrofit {
        return retrofit;
    }
}