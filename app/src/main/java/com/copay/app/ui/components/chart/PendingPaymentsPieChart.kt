package com.copay.app.ui.components.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import io.github.dautovicharis.charts.PieChart
import io.github.dautovicharis.charts.model.toChartDataSet
import io.github.dautovicharis.charts.style.ChartViewDefaults
import io.github.dautovicharis.charts.style.PieChartDefaults


// TODO: Charts still in progress
@Composable
fun pendingPaymentsPieChart(
    pending: Int,
    total: Int = 10
) {
    val slice1 = pending.toFloat()
    val slice2 = (total - pending).coerceAtLeast(0).toFloat()

    val colors = listOf(CopayColors.primary, CopayColors.surface)

    // Chart size
    val chartViewStyle = ChartViewDefaults.style(
        width = 150.dp,
        outerPadding = 0.dp,
        innerPadding = 0.dp,
        cornerRadius = 0.dp,
        shadow = 0.dp,
        backgroundColor = Color.Transparent
    )

    val style = PieChartDefaults.style(
        pieColors = colors,
        donutPercentage = 60f,
        borderWidth = 4f,
        chartViewStyle = chartViewStyle
    )

    val dataSet = listOf(slice1, slice2).toChartDataSet(
        title = ""
    )

    PieChart(
        dataSet = dataSet,
        style = style
    )
}
