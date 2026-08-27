package com.example.kalasetu.features.profile

interface ProfileContract {

    interface View {
        fun showLoading(isLoading: Boolean)
        fun showProfile(profile: Profile)
        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun loadProfile(userId: String)
        fun onEditProfileClicked()
        fun onTabSelected(tab: ProfileTab)
        fun detach()

    }
}