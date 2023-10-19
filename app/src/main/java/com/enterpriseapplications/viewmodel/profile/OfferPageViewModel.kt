package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class OfferPageViewModel(val application: CustomApplication): BaseViewModel(application) {

    var userID: UUID? = null;
    private var _currentSelectedTab: MutableStateFlow<Int> = MutableStateFlow(0)
    private var _currentCreatedOffers: MutableStateFlow<List<Offer>> = MutableStateFlow(emptyList())
    private var _currentReceivedOffers: MutableStateFlow<List<Offer>> = MutableStateFlow(emptyList())
    private var _currentCreatedOffersPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentReceivedOffersPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentCreatedOffersSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _currentReceivedOffersSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun initialize() {
        if(this.userID != null) {
            this.updateCreatedOffers(false)
            this.updateReceivedOffers(false)
        }
    }

    private fun updateCreatedOffers(page: Boolean) {
        this._currentCreatedOffersSearching.value = true
        this.makeRequest(this.retrofitConfig.offerController.getCreatedOffers(userID!!,this._currentCreatedOffersPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentCreatedOffers.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Offer> = mutableListOf()
                    mutableList.addAll(this._currentCreatedOffers.value)
                    mutableList.addAll(mutableList)
                    this._currentCreatedOffers.value = mutableList
                }
            }
            this._currentCreatedOffersPage.value = this._currentCreatedOffersPage.value.copy(size = it.page.size, totalPages = it.page.totalPages, totalElements =  it.page.totalElements, number = it.page.number)
            this._currentCreatedOffersSearching.value = false
        })
    }
    private fun updateReceivedOffers(page: Boolean) {
        this._currentReceivedOffersSearching.value = true
        this.makeRequest(this.retrofitConfig.offerController.getReceivedOffers(userID!!,this._currentReceivedOffersPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentReceivedOffers.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Offer> = mutableListOf()
                    mutableList.addAll(this._currentReceivedOffers.value)
                    mutableList.addAll(mutableList)
                    this._currentReceivedOffers.value = mutableList
                }
            }
            this._currentReceivedOffersPage.value = this._currentReceivedOffersPage.value.copy(size = it.page.size, totalPages = it.page.totalPages, totalElements = it.page.totalElements,number = it.page.number)
            this._currentReceivedOffersSearching.value = false
        })
    }

    fun updateCurrentPage(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _currentCreatedOffersPage else _currentReceivedOffersPage
        if(currentPage.value.number + 1 >= currentPage.value.totalPages)
            return;
        this.handlePage(index,true)
    }

    fun resetTab(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _currentCreatedOffersPage else _currentReceivedOffersPage
        val currentItems: MutableStateFlow<List<Offer>> = if(index == 0) _currentCreatedOffers else _currentReceivedOffers
        currentItems.value = emptyList()
        currentPage.value = Page(20,0,0,0)
    }

    fun updateCurrentSelectedTab(index: Int) {
        this._currentSelectedTab.value = index
        this.handlePage(index,false)
    }

    private fun handlePage(index: Int,value: Boolean) {
        when(index) {
            0 -> this.updateCreatedOffers(value)
            1 -> this.updateReceivedOffers(value)
        }
    }

    val currentSelectedTab: StateFlow<Int> = _currentSelectedTab.asStateFlow()
    val currentCreatedOffers: StateFlow<List<Offer>> = _currentCreatedOffers.asStateFlow()
    val currentReceivedOffers: StateFlow<List<Offer>> = _currentReceivedOffers.asStateFlow()
    val currentCreatedOffersPage: StateFlow<Page> = _currentCreatedOffersPage.asStateFlow()
    val currentReceivedOffersPage: StateFlow<Page> = _currentReceivedOffersPage.asStateFlow()
    val currentCreatedOffersSearching: StateFlow<Boolean> = _currentCreatedOffersSearching.asStateFlow()
    val currentReceivedOffersSearching: StateFlow<Boolean> = _currentReceivedOffersSearching.asStateFlow()
}