package com.buckun.spacexproject.network.retrofit

import com.buckun.spacexproject.ui.model.LaunchDetailsResponse
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("launches")
    fun getLaunchList(): Call<ArrayList<LaunchDetailsResponse?>>

    @GET("launches/upcoming")
    fun getUpcomingLaunchList(): Call<ArrayList<LaunchDetailsResponse?>>
}