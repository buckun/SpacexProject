package com.buckun.spacexproject.ui.favoriteRockets

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.buckun.spacexproject.R
import com.buckun.spacexproject.base.BaseFragment
import com.buckun.spacexproject.databinding.FragmentFavoriteRocketsBinding
import com.buckun.spacexproject.ui.adapter.RocketsAdapter
import com.buckun.spacexproject.ui.launchDetails.LaunchDetailsActivity
import com.buckun.spacexproject.ui.model.LaunchDetailsResponse
import com.buckun.spacexproject.util.BUNDLE_ROCKET_DETAILS


class FavoriteRocketFragment: BaseFragment<FragmentFavoriteRocketsBinding, FavoriteRocketViewModel>() {
    private var rocketsAdapter: RocketsAdapter? = null
    override fun getContentView(): Int = R.layout.fragment_favorite_rockets
    override fun setViewModelClass(): Class<FavoriteRocketViewModel> {
      return FavoriteRocketViewModel::class.java
    }
    override fun initViews(savedInstanceState: Bundle?) {
        setUpRecyclerView()
        addObservers()
        getFavoriteItemsList()
    }

    private fun getFavoriteItemsList() {
        viewModel?.showScreenLoading(true)
        viewModel?.setIsFav()
    }

    private fun addObservers() {
        viewModel?.getScreenLoading()?.observe(this, { show ->
            fragmentViewDataBinding?.progressFavRockets?.pbActiveMoments?.visibility =
                if (show) View.VISIBLE else View.GONE
        })
        viewModel?.getFavList()?.observe(this, { rocketsList ->
            rocketsList?.let {
                if (it.size>0){
                    rocketsAdapter?.addItems(it)
                }else{
                    rocketsAdapter?.clearList()
                    showErrorMessage()
                }

            }
        })
    }

    private fun setUpRecyclerView() {
        rocketsAdapter = RocketsAdapter()
        fragmentViewDataBinding?.rvFavoriteRockets?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rocketsAdapter
            itemAnimator?.changeDuration = 0
        }
        rocketsAdapter?.setClickListener(object : RocketsAdapter.ClickListener {
            override fun addFav(data: LaunchDetailsResponse) {
            }

            override fun removeFav(data: LaunchDetailsResponse) {
                removeFavItem(data)
            }

            override fun onItemClick(data: LaunchDetailsResponse) {
                val intent= Intent(context, LaunchDetailsActivity::class.java)
                intent.putExtra(BUNDLE_ROCKET_DETAILS,data)
                launchDetailsRequest.launch(intent)
            }

        })
    }
    val launchDetailsRequest =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel?.setIsFav()
            }
        }

    private fun removeFavItem(data: LaunchDetailsResponse) {
        if (isNetworkAvailable()) {
            viewModel?.showScreenLoading(true)
            viewModel?.deleteFavoriteItem(data) { success ->
                if (success) {
                    viewModel?.setIsFav()
                } else {
                    viewModel?.showScreenLoading(false)
                }
            }
        } else {
            noNetworkError()
        }
    }



    private fun showErrorMessage() {
        fragmentViewDataBinding?.errorViewFavRockets?.let {
            it.showMessages("No Data!","No favorites available to show")
            it.showImage(R.drawable.error)
            it.show()
            it.visibility = View.VISIBLE
        }
    }
}