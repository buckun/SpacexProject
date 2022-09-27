package com.buckun.spacexproject.ui.launchDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.buckun.spacexproject.R
import com.buckun.spacexproject.base.BaseActivity
import com.buckun.spacexproject.databinding.ActivityLaunchDetailsBinding
import com.buckun.spacexproject.ui.model.LaunchDetailsResponse
import com.buckun.spacexproject.util.BUNDLE_ROCKET_DETAILS
import com.buckun.spacexproject.util.isFavItem
import com.bumptech.glide.Glide


class LaunchDetailsActivity : BaseActivity<ActivityLaunchDetailsBinding, LaunchDetailsViewModel>(),
    View.OnClickListener {

    var launchDetails: LaunchDetailsResponse? = null
    var isFavorite: Boolean = false

    override fun getContentView(): Int = R.layout.activity_launch_details
    override fun setViewModelClass(): Class<LaunchDetailsViewModel> {
        return LaunchDetailsViewModel::class.java
    }

    override fun initViews(savedInstanceState: Bundle?) {
        getIntentData()
        setDataToViews()
        setClickListeners()
    }

    private fun setClickListeners() {
        viewDataBinding?.iconFav?.setOnClickListener(this)
        viewDataBinding?.imgBack?.setOnClickListener(this)
    }

    private fun setDataToViews() {
        viewDataBinding?.imgRocketThumbNail?.let {
            Glide.with(this)
                .load(launchDetails?.links?.patch?.large)
                .placeholder(R.drawable.no_image_16_9)
                .into(it)
        }
        viewDataBinding?.txtHeading?.text=launchDetails?.name
        viewDataBinding?.txtDescription?.text=launchDetails?.details
        setFavoriteIcon(launchDetails?.isFav ?: false)
    }

    fun setFavoriteIcon(isFav: Boolean) {
        isFavorite = isFav
        when (isFav) {
            true -> {
                viewDataBinding?.iconFav?.let {
                    Glide.with(this)
                        .load(R.drawable.ic_favorite)
                        .into(it)
                }
            }
            false -> {
                viewDataBinding?.iconFav?.let {
                    Glide.with(this)
                        .load(R.drawable.ic_un_favorite)
                        .into(it)
                }
            }
        }
    }

    private fun getIntentData() {
        launchDetails = intent.getSerializableExtra(BUNDLE_ROCKET_DETAILS) as? LaunchDetailsResponse
    }

    override fun onClick(view: View?) {
        when (view) {
            viewDataBinding?.iconFav -> {
                if (isNetworkAvailable()) {
                    showProgressBar(true)
                    //onFavoriteIconClicked()
                } else {
                    noInternetError()
                }
            }
            viewDataBinding?.imgBack -> {
                onBackPressed()
            }
        }
    }

    fun onFavoriteIconClicked() {
        if (isFavorite) {
            viewModel?.deleteFavoriteItem(launchDetails) { success ->
                if (success) {
                    getFavoriteList()
                } else {
                    showProgressBar(false)
                }
            }

        } else {
            viewModel?.addFavoriteItem(launchDetails) { success ->
                if (success) {
                    getFavoriteList()
                } else {
                    showProgressBar(false)
                }
            }
        }
    }

    fun showProgressBar(show: Boolean) {
        viewDataBinding?.progressLaunchDetails?.pbActiveMoments?.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun getFavoriteList() {
        viewModel?.getFavoriteList {
            showProgressBar(false)
            isFavItem(launchDetails?.id ?: "") {
                setFavoriteIcon(it)
            }
        }
    }
    override fun onBackPressed() {
        if (launchDetails?.isFav != isFavorite) {
            setResult(Activity.RESULT_OK, Intent())
        }
        super.onBackPressed()
    }
}