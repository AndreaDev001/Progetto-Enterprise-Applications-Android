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


    private var userID: UUID? = null;
    private var _writtenReviews: MutableStateFlow<List<Review>> = MutableStateFlow(emptyList())
    private var _receivedReviews: MutableStateFlow<List<Review>> = MutableStateFlow(emptyList())
    private var _writtenReviewsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))
    private var _receivedReviewsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));

    fun initialize() {
        this.updateWrittenReviews(page = false,first = true)
        this.updateReceivedReviews(page = false,first = true)
    }

    fun updateWrittenReviews(page: Boolean,first: Boolean = false) {
        if(page && !first && this._writtenReviewsPage.value.number + 1 >= this._writtenReviewsPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.reviewController.getWrittenReviews(this.userID!!),{
            if(it._embedded != null)
                this._writtenReviews.value.toMutableList().addAll(it._embedded.content)
            this._writtenReviewsPage.value = this._writtenReviewsPage.value.copy(size = it.page.size,number = it.page.number, totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }

    fun updateReceivedReviews(page: Boolean,first: Boolean = false) {
        if(page && !first && this._receivedReviewsPage.value.number + 1 >= this._receivedReviewsPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.reviewController.getReceivedReviews(this.userID!!),{
            if(it._embedded != null)
                this._receivedReviews.value.toMutableList().addAll(it._embedded.content)
            this._receivedReviewsPage.value = this._receivedReviewsPage.value.copy(size = it.page.size,number = it.page.number,totalPages = it.page.totalPages, totalElements = it.page.totalElements)
        })
    }


    val writtenReviews: StateFlow<List<Review>> = _writtenReviews.asStateFlow()
    val receivedReviews: StateFlow<List<Review>> = _receivedReviews.asStateFlow()
    val writtenReviewsPage: StateFlow<Page> = _writtenReviewsPage.asStateFlow()
    val receivedReviewsPage: StateFlow<Page> = _receivedReviewsPage.asStateFlow()
}