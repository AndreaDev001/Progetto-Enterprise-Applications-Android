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

    var userID: UUID? = null;
    private var _currentSelectedTab: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentFollowers: MutableStateFlow<List<Follow>> = MutableStateFlow(emptyList())
    private var _currentFollows: MutableStateFlow<List<Follow>> = MutableStateFlow(emptyList())
    private var _currentFollowersPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))
    private var _currentFollowsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))
    private var _currentFollowersSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _currentFollowsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    fun initialize() {
        if(userID != null) {
            this.updateFollowers(page = false)
            this.updateFollows(page = false)
        }
    }

    private fun updateFollowers(page: Boolean) {
        this._currentFollowersSearching.value = !page;
        this.makeRequest(this.retrofitConfig.followController.getFollowers(this.userID!!,this._currentFollowersPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentFollowers.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Follow> = mutableListOf()
                    mutableList.addAll(this._currentFollowers.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentFollowers.value = mutableList
                }
            }
            this._currentFollowersSearching.value = false;
            this._currentFollowersPage.value = this._currentFollowersPage.value.copy(size = it.page.size,number = it.page.number, totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }
    private fun updateFollows(page: Boolean) {
        this._currentFollowsSearching.value = !page;
        this.makeRequest(this.retrofitConfig.followController.getFollowed(this.userID!!,this._currentFollowsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentFollows.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Follow> = mutableListOf()
                    mutableList.addAll(this._currentFollows.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentFollows.value = mutableList
                }
            }
            this._currentFollowsSearching.value = false;
            this._currentFollowsPage.value = this._currentFollowsPage.value.copy(size = it.page.size,number = it.page.number,totalPages = it.page.totalPages,totalElements = it.page.totalElements)
        })
    }
    fun updateCurrentSelectedTab(index: Int) {
        this._currentSelectedTab.value = index
        val currentFollows: MutableStateFlow<Page> = if(index == 0) _currentFollowersPage else _currentFollowsPage
        currentFollows.value = currentFollows.value.copy(size = 20,0,0,0)
        when(index) {
            0 -> this.updateFollowers(false)
            1 -> this.updateFollows(false)
        }
    }
    fun resetTab(index: Int) {
        when(index) {
            0 -> this._currentFollowers.value = emptyList()
            1 -> this._currentFollows.value = emptyList()
        }
    }
    fun updateCurrentPage(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _currentFollowersPage else _currentFollowsPage
        if(currentPage.value.number + 1 >= currentPage.value.totalPages)
            return;
        when(index) {
            0 -> this.updateFollowers(page = true)
            1 -> this.updateFollows(page = true)
        }
    }
    fun resetSearch(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _currentFollowersPage else _currentFollowsPage
        currentPage.value = currentPage.value.copy(size = 20,totalElements = 0,totalPages = 0,number = 0)
        when(index) {
            0 -> this.updateFollowers(page = false)
            1 -> this.updateFollows(page = true)
        }
    }

    val currentSelectedTab: StateFlow<Int> = _currentSelectedTab.asStateFlow()
    val currentFollowers: StateFlow<List<Follow>> = _currentFollowers.asStateFlow()
    val currentFollows: StateFlow<List<Follow>> = _currentFollows.asStateFlow()
    val currentFollowersPage: StateFlow<Page> = _currentFollowersPage.asStateFlow()
    val currentFollowsPage: StateFlow<Page> = _currentFollowsPage.asStateFlow()
    val currentFollowersSearching: StateFlow<Boolean> = _currentFollowersSearching.asStateFlow()
    val currentFollowsSearching: StateFlow<Boolean> = _currentFollowsSearching.asStateFlow()
}