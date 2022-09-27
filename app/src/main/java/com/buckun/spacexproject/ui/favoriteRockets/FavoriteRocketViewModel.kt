package com.buckun.spacexproject.ui.favoriteRockets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buckun.spacexproject.AppApplication
import com.buckun.spacexproject.base.BaseViewModel
import com.buckun.spacexproject.ui.model.LaunchDetailsResponse


class FavoriteRocketViewModel : BaseViewModel() {
    private val showScreenLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val favList: MutableLiveData<ArrayList<LaunchDetailsResponse?>> = MutableLiveData()
    fun getScreenLoading(): LiveData<Boolean> {
        return showScreenLoading
    }

     fun showScreenLoading(show: Boolean) {
        showScreenLoading.value = show
    }

    fun getFavList():LiveData<ArrayList<LaunchDetailsResponse?>> {
        return favList
    }

    private fun setFavList(favList:ArrayList<LaunchDetailsResponse?>){
        this.favList.value=favList
    }
    fun setIsFav(){
        val list= AppApplication.mFavoriteList.value
        if (list!= null){
            for (item in list) {
                item?.isFav = true
            }
            setFavList(list)
        }
        showScreenLoading(false)
    }
}