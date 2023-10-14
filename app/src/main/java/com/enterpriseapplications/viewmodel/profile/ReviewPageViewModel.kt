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

    fun initialize() {
        if(this.userID != null) {
            this.updateWrittenReviews(page = false,first = true)
            this.updateReceivedReviews(page = false,first = true)
        }
    }

    private fun updateWrittenReviews(page: Boolean,first: Boolean = false) {
        if(page && !first && this._writtenReviewsPage.value.number + 1 >= this._writtenReviewsPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.reviewController.getWrittenReviews(this.userID!!,this._writtenReviewsPage.value.number,20),{
            if(it._embedded != null) {
                if(page) {
                    val mutableList: MutableList<Review> = mutableListOf()
                    mutableList.addAll(this._writtenReviews.value)
                    mutableList.addAll(it._embedded.content)
                    this._writtenReviews.value = mutableList
                }
                else
                    this._writtenReviews.value = it._embedded.content
            }
            this._writtenReviewsPage.value = this._writtenReviewsPage.value.copy(size = it.page.size,number = it.page.number, totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }

    private fun updateReceivedReviews(page: Boolean,first: Boolean = false) {
        if(page && !first && this._receivedReviewsPage.value.number + 1 >= this._receivedReviewsPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.reviewController.getReceivedReviews(this.userID!!,this._receivedReviewsPage.value.number,20),{
            if(it._embedded != null) {
                if(page) {
                    val mutableList: MutableList<Review> = mutableListOf()
                    mutableList.addAll(this._receivedReviews.value)
                    mutableList.addAll(it._embedded.content)
                    this._receivedReviews.value = mutableList
                }
                else
                    this._receivedReviews.value = it._embedded.content
            }
            this._receivedReviewsPage.value = this._receivedReviewsPage.value.copy(size = it.page.size,number = it.page.number,totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }

    fun updateSelectedTab(index: Int) {
        this._currentSelectedTab.value = index
        when(index) {
            0 -> this.updateWrittenReviews(page = false,first = true)
            1 -> this.updateReceivedReviews(page = false,first = true)
        }
    }

    fun resetTab(index: Int) {
        when(index) {
            0 -> {
                this._writtenReviews.value = emptyList()
                this._writtenReviewsPage.value = Page(20,0,0,0)
            }
            1 -> {
                this._receivedReviews.value = emptyList()
                this._receivedReviewsPage.value = Page(20,0,0,0);
            }
        }
    }
    fun updateCurrentPage(index: Int) {
        when(index) {
            0 -> this.updateWrittenReviews(page = true,first = false)
            1 -> this.updateReceivedReviews(page = true,first = false)
        }
    }

    val currentSelectedTab: StateFlow<Int> = _currentSelectedTab.asStateFlow()
    val writtenReviews: StateFlow<List<Review>> = _writtenReviews.asStateFlow()
    val receivedReviews: StateFlow<List<Review>> = _receivedReviews.asStateFlow()
    val writtenReviewsPage: StateFlow<Page> = _writtenReviewsPage.asStateFlow()
    val receivedReviewsPage: StateFlow<Page> = _receivedReviewsPage.asStateFlow()
}