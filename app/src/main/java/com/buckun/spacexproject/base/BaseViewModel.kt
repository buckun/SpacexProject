package com.buckun.spacexproject.base

import androidx.lifecycle.ViewModel
import com.buckun.spacexproject.AppApplication
import com.buckun.spacexproject.ui.model.LaunchDetailsResponse
import com.buckun.spacexproject.util.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore

import retrofit2.Call


open class BaseViewModel :
    ViewModel() {

    private val callList: ArrayList<Call<*>> = ArrayList()
    val showSuccessMessage: SingleLiveEvent<String?> = SingleLiveEvent()
    val showErrorMessage: SingleLiveEvent<String?> = SingleLiveEvent()
    private val db = FirebaseFirestore.getInstance()
    fun clearCalls() {
        for (callItem in callList) {
            callItem.cancel()
        }
        callList.clear()
    }

    fun addCall(call: Call<*>) {
        callList.add(call)
    }

    fun getFavoriteList(callback: (success: Boolean) -> Unit) {
        db.collection(AppApplication.mCurrentUser.value?.uid ?: "").get()
            .addOnSuccessListener { querySnapShot ->
                val data =
                    querySnapShot.toObjects(LaunchDetailsResponse::class.java) as? ArrayList<LaunchDetailsResponse?>
                AppApplication.mFavoriteList.value = data
                callback(true)
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(false)
            }
    }

    fun addFavoriteItem(
        launchDetailsResponse: LaunchDetailsResponse?,
        callback: (success: Boolean) -> Unit
    ) {
        launchDetailsResponse?.let {
            db.collection(AppApplication.mCurrentUser.value?.uid ?: "")
                .document(launchDetailsResponse.id ?: "").set(
                    it
                ).addOnSuccessListener {
                    getFavoriteList { isSuccess ->
                        callback(isSuccess)
                    }
                }.addOnFailureListener {exception->
                    exception.printStackTrace()
                    callback(false)
                }
        }
    }

    fun deleteFavoriteItem(
        launchDetailsResponse: LaunchDetailsResponse?,
        callback: (success: Boolean) -> Unit
    ) {
        launchDetailsResponse?.let {
            db.collection(AppApplication.mCurrentUser.value?.uid ?: "")
                .document(launchDetailsResponse.id ?: "").delete().addOnSuccessListener {
                    getFavoriteList { isSuccess ->
                        callback(isSuccess)
                    }
                }.addOnFailureListener {exception->
                    exception.printStackTrace()
                    callback(false)
                }
        }
    }
}