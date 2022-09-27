package com.buckun.spacexproject.ui.upcomingRockets
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buckun.spacexproject.base.BaseViewModel
import com.buckun.spacexproject.callBacks.RetroCallback
import com.buckun.spacexproject.network.ApiHelper
import com.buckun.spacexproject.ui.model.ErrorViewModel
import com.buckun.spacexproject.ui.model.LaunchDetailsResponse
import com.buckun.spacexproject.util.isFavItem
import retrofit2.Call

class UpcomingRocketViewModel: BaseViewModel() {
    private val showScreenLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val upcomingRocketsList: MutableLiveData<ArrayList<LaunchDetailsResponse?>?> = MutableLiveData()
    private val errorView: MutableLiveData<ErrorViewModel> = MutableLiveData()
    private val responseList: MutableLiveData<ArrayList<LaunchDetailsResponse?>?> = MutableLiveData()

    fun getScreenLoading(): LiveData<Boolean> {
        return showScreenLoading
    }

     fun showScreenLoading(show: Boolean) {
        showScreenLoading.value = show
    }

    fun getUpcomingRocketsList(): LiveData<ArrayList<LaunchDetailsResponse?>?> {
        return upcomingRocketsList
    }

    private fun setUpcomingRocketsList(upcomingRocketsList:ArrayList<LaunchDetailsResponse?>?){
        this.upcomingRocketsList.value=upcomingRocketsList
    }
     fun showErrorView(errorView: ErrorViewModel) {
        this.errorView.value = errorView
    }

    fun getErrorView(): LiveData<ErrorViewModel> {
        return errorView
    }

    fun setIsFav(){
        if (responseList.value != null){
            for (item in responseList.value!!) {
                isFavItem(item?.id ?: "") { isFav ->
                    item?.isFav = isFav
                }
            }
            setUpcomingRocketsList(responseList.value)
        }
        showScreenLoading(false)
    }
    fun callUpcomingRocketsApi() {
        showScreenLoading(true)
        val call = ApiHelper().getApi().getUpcomingLaunchList()
        addCall(call)
        call.enqueue(RetroCallback(object :
            RetroCallback.MyCallback<ArrayList<LaunchDetailsResponse?>?> {
            override fun onResponse(
                call: Call<ArrayList<LaunchDetailsResponse?>?>,
                response: ArrayList<LaunchDetailsResponse?>?
            ) {
                if (response != null) {
                    responseList.value=response
                    setIsFav()
                } else {
                    showScreenLoading(false)
                    showErrorView(ErrorViewModel("Error","No data available!"))
                }
            }

            override fun onTimeout(icon: Int?, heading: String?, desc: String?) {
                showScreenLoading(false)
                showErrorView(ErrorViewModel(heading,desc))
            }

            override fun onError(icon: Int?, heading: String?, desc: String?, statusCode: Int?) {
                showScreenLoading(false)
                showErrorView(ErrorViewModel(heading,desc))
            }

            override fun onApiCanceled() {
                showScreenLoading(false)
            }

        }))
    }
}