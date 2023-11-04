package com.enterpriseapplications

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState


fun LazyListState.isScrolledToEnd() = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?:0) + 1 > layoutInfo.totalItemsCount - 2
fun LazyGridState.isScrolledToEnd() = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?:0) + 1 > layoutInfo.totalItemsCount - 2
