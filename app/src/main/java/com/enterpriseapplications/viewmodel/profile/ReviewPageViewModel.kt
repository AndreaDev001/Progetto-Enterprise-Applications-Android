package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ReviewPageViewModel(val application: CustomApplication): BaseViewModel(application) {


    var userID: UUID? = null
    private var _currentSelectedTab: MutableStateFlow<Int> = MutableStateFlow(0)
    private var _writtenReviews: MutableStateFlow<List<Review>> = MutableStateFlow(emptyList())
    private var _receivedReviews: MutableStateFlow<List<Review>> = MutableStateFlow(emptyList())
    private var _writtenReviewsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))
    private var _receivedReviewsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _writtenReviewsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _receivedReviewsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    fun initialize() {
        if(this.userID != null) {
            this.updateWrittenReviews(page = false)
            this.updateReceivedReviews(page = false)
        }
    }

    private fun updateWrittenReviews(page: Boolean) {
        this._writtenReviewsSearching.value = !page;
        this.makeRequest(this.retrofitConfig.reviewController.getWrittenReviews(this.userID!!,this._writtenReviewsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._writtenReviews.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Review> = mutableListOf()
                    mutableList.addAll(this._writtenReviews.value)
                    mutableList.addAll(it._embedded.content)
                    this._writtenReviews.value = mutableList
                }
            }
            this._writtenReviewsSearching.value = false;
            this._writtenReviewsPage.value = this._writtenReviewsPage.value.copy(size = it.page.size,number = it.page.number, totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }

    private fun updateReceivedReviews(page: Boolean) {
        this._receivedReviewsSearching.value = false;
        this.makeRequest(this.retrofitConfig.reviewController.getReceivedReviews(this.userID!!,this._receivedReviewsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._receivedReviews.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Review> = mutableListOf()
                    mutableList.addAll(this._receivedReviews.value)
                    mutableList.addAll(it._embedded.content)
                    this._receivedReviews.value = mutableList
                }
            }
            this._receivedReviewsSearching.value = false;
            this._receivedReviewsPage.value = this._receivedReviewsPage.value.copy(size = it.page.size,number = it.page.number,totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }

    fun updateSelectedTab(index: Int) {
        this._currentSelectedTab.value = index
        val currentReviewsPage: MutableStateFlow<Page> = if(index == 0) this._writtenReviewsPage else this._receivedReviewsPage
        currentReviewsPage.value = currentReviewsPage.value.copy(size = 20, totalElements = 0,totalPages = 0, number = 0)
        when(index) {
            0 -> this.updateWrittenReviews(false)
            1 -> this.updateReceivedReviews(false)
        }
    }
    fun resetTab(index: Int) {
        val currentReviewsPage: MutableStateFlow<Page> = if(index == 0) this._writtenReviewsPage else this._receivedReviewsPage
        currentReviewsPage.value = currentReviewsPage.value.copy(size = 20, totalElements = 0,totalPages = 0, number = 0)
        when(index) {
            0 -> this._writtenReviews.value = emptyList()
            1 -> this._receivedReviews.value = emptyList()
        }
    }

    fun updateCurrentPage(index: Int) {
        val currentReviewsPage: MutableStateFlow<Page> = if(index == 0) this._writtenReviewsPage else this._receivedReviewsPage
        if(currentReviewsPage.value.number + 1 >= currentReviewsPage.value.totalPages)
            return;
        currentReviewsPage.value = currentReviewsPage.value.copy(size = currentReviewsPage.value.size,totalElements = currentReviewsPage.value.totalElements,totalPages = currentReviewsPage.value.totalPages,number = currentReviewsPage.value.number + 1)
        when(index) {
            0 -> this.updateWrittenReviews(page = true)
            1 -> this.updateReceivedReviews(page = true)
        }
    }

    val currentSelectedTab: StateFlow<Int> = _currentSelectedTab.asStateFlow()
    val writtenReviews: StateFlow<List<Review>> = _writtenReviews.asStateFlow()
    val receivedReviews: StateFlow<List<Review>> = _receivedReviews.asStateFlow()
    val writtenReviewsPage: StateFlow<Page> = _writtenReviewsPage.asStateFlow()
    val receivedReviewsPage: StateFlow<Page> = _receivedReviewsPage.asStateFlow()
    val writtenReviewsSearching: StateFlow<Boolean> = _writtenReviewsSearching.asStateFlow()
    val receivedReviewsSearching: StateFlow<Boolean> = _receivedReviewsSearching.asStateFlow()
}