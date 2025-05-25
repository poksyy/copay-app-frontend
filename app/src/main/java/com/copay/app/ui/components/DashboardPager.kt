package com.copay.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.copay.app.ui.components.chart.pendingPaymentsPieChart
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun DashboardPager(
    totalSpent: String,
    pendingPayments: Int,
    groupsJoined: Int
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    val items = listOf(
        Triple("Total Spent", totalSpent, "This month"),
        Triple("Pending Payments", pendingPayments.toString(), "Awaiting confirmation"),
        Triple("Groups Joined", groupsJoined.toString(), "All time")
    )

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) { page ->
            val (title, value, subtitle) = items[page]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CopayColors.onPrimary
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                // TODO Charts for other slides are still missing.
                // Chart for pending payments.
                if (title == "Pending Payments") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = title, style = CopayTypography.subtitle)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = value, style = CopayTypography.title)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = subtitle,
                                style = CopayTypography.footer,
                                color = CopayColors.onSecondary
                            )
                        }

                        pendingPaymentsPieChart(
                            pending = pendingPayments
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = title, style = CopayTypography.subtitle)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = value, style = CopayTypography.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = subtitle, style = CopayTypography.footer, color = CopayColors.onSecondary)
                    }
                }
            }
        }

        // Page indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) CopayColors.primary else CopayColors.surface)
                )
                if (index < 2) Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
