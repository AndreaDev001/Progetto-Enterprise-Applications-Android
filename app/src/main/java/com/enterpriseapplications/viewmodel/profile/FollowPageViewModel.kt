package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.model.Follow
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FollowPageViewModel(val application: CustomApplication): BaseViewModel(application) {

    private var userID: UUID? = null;
    private var _currentFollowers: MutableStateFlow<List<Follow>> = MutableStateFlow(emptyList())
    private var _currentFollows: MutableStateFlow<List<Follow>> = MutableStateFlow(emptyList())
    private var _currentFollowersPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))
    private var _currentFollowsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))

    fun initialize() {
        if(userID != null) {
            this.updateFollowers(page = false,first = true)
            this.updateFollowers(page = false,first = true)
        }
    }

    fun updateFollowers(page: Boolean,first: Boolean = false) {
        if(this.userID == null)
            return;
        if(page && !first)
        {
            if(this._currentFollowersPage.value.number + 1 >= this._currentFollowersPage.value.totalPages)
                return;
        }
        this.makeRequest(this.retrofitConfig.followController.getFollowers(this.userID!!),{
            if(it._embedded != null) {
                this._currentFollowers.value.toMutableList().addAll(it._embedded.content)
            }
            this._currentFollowersPage.value = this._currentFollowersPage.value.copy(size = it.page.size,number = it.page.number, totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }
    fun updateFollows(page: Boolean,first: Boolean = false) {
        if(page && !first) {
            if(this._currentFollowsPage.value.number + 1 >= this._currentFollowsPage.value.totalPages)
                return;
        }
        this.makeRequest(this.retrofitConfig.followController.getFollowed(this.userID!!),{
            if(it._embedded != null)
                this._currentFollows.value.toMutableList().addAll(it._embedded.content)
            this._currentFollowsPage.value = this._currentFollowsPage.value.copy(size = it.page.size,number = it.page.number,totalPages = it.page.totalPages,totalElements = it.page.totalElements)
        })
    }
    val currentFollowers: StateFlow<List<Follow>> = _currentFollowers.asStateFlow()
    val currentFollows: StateFlow<List<Follow>> = _currentFollows.asStateFlow()
    val currentFollowersPage: StateFlow<Page> = _currentFollowersPage.asStateFlow()
    val currentFollowsPage: StateFlow<Page> = _currentFollowsPage.asStateFlow()
}