package com.buckun.spacexproject.network

import com.buckun.spacexproject.network.retrofit.API
import com.buckun.spacexproject.network.retrofit.BaseRetrofit


class ApiHelper {
    /**
     * getApi - To return interface of api
     * @param assets: assets
     * @since 1.0
     */
    fun getApi(): API {
        return BaseRetrofit.getMyRetrofit().create(API::class.java)
    }
}