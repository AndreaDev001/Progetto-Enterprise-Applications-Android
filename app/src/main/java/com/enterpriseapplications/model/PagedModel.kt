package com.enterpriseapplications.model

data class PagedModel<T>(val _embedded: EmbeddedList<T> ,val page: Page);
data class EmbeddedList<T>(val content: List<T>);
data class Page(val size: Int,val totalElements: Int,val totalPages: Int,val number: Int)