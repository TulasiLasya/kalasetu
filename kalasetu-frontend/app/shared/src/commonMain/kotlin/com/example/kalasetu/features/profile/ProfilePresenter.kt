package com.example.kalasetu.features.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel
import kotlinx.coroutines.CancellationException

class ProfilePresenter(
    private val repository: ProfileRepositoryContract
) : ProfileContract.Presenter {

    private var view: ProfileContract.View? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun attachView(view: ProfileContract.View) {
        this.view = view
    }

    override fun loadProfile(userId: String) {
        view?.showLoading(true)
        scope.launch {
            try {
                val profile = repository.fetchProfile(userId)
                view?.showLoading(false)
                view?.showProfile(profile)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                view?.showLoading(false)
                view?.showError(e.message ?: "Failed to load profile")
            }
        }
    }
    override fun onEditProfileClicked() {}

    override fun onTabSelected(tab: ProfileTab) {
        // Implementation for tab selection
    }

    override fun detach() {
        view = null
        scope.cancel()
    }
}