package com.copay.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScrollIndicator(
    listState: LazyListState,
    totalItems: Int,
    visibleItemsCount: Int,
    modifier: Modifier = Modifier,
    indicatorWidth: Dp = 6.dp,
    trackColor: Color = Color.LightGray.copy(alpha = 0.3f),
    indicatorColor: Color = Color.Gray
) {
    if (totalItems <= visibleItemsCount) return

    val scrollProgress = remember(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val maxScrollIndex = (totalItems - visibleItemsCount).coerceAtLeast(1)
        val offsetFraction = listState.firstVisibleItemScrollOffset / 100f
        ((listState.firstVisibleItemIndex + offsetFraction) / maxScrollIndex.toFloat()).coerceIn(0f, 1f)
    }

    val indicatorHeightFraction = visibleItemsCount.toFloat() / totalItems.toFloat()

    BoxWithConstraints(
        modifier = modifier
            .width(indicatorWidth)
            .fillMaxHeight()
            .background(trackColor, RoundedCornerShape(3.dp))
    ) {
        val boxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val indicatorHeightPx = boxHeightPx * indicatorHeightFraction
        val offsetY = (boxHeightPx - indicatorHeightPx) * scrollProgress

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { indicatorHeightPx.toDp() })
                .offset(y = with(LocalDensity.current) { offsetY.toDp() })
                .background(indicatorColor, RoundedCornerShape(3.dp))
                .align(Alignment.TopStart)
        )
    }
}
