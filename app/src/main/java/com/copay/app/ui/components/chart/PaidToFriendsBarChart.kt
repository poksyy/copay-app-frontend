package com.copay.app.ui.components.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import io.github.dautovicharis.charts.BarChart
import io.github.dautovicharis.charts.model.toChartDataSet
import io.github.dautovicharis.charts.style.BarChartDefaults
import io.github.dautovicharis.charts.style.ChartViewDefaults

@Composable
fun paidToFriendsBarChart(
    payments: List<Float>
) {

    // Chart size
    val chartViewStyle = ChartViewDefaults.style(
        width = 300.dp,
        outerPadding = 0.dp,
        innerPadding = 0.dp,
        cornerRadius = 0.dp,
        shadow = 0.dp,
        backgroundColor = Color.Transparent,
    )


    val style = BarChartDefaults.style(
        barColor = CopayColors.primary,
        space = 8.dp,
        chartViewStyle = chartViewStyle
    )

    BarChart(
        dataSet = payments.toChartDataSet(title = "+"),
        style = style
    )
}
