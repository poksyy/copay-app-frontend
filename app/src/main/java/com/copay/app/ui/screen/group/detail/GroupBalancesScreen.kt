package com.copay.app.ui.screen.group.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.R
import com.copay.app.utils.ExpenseUtils
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.components.memberItem
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.ui.components.button.manageDebtsButton
import com.copay.app.ui.components.pillTabRow
import com.copay.app.viewmodel.ExpenseViewModel
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel

@Composable
fun groupBalancesScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel(),
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    // Values from Group Session.
    val group by groupViewModel.group.collectAsState()

    // Expense state,
    val expensesState by expenseViewModel.expenseState.collectAsState()
    val expenses by expenseViewModel.expenses.collectAsState()

    // Get current user
    val currentUserId = groupViewModel.getCurrentUserId()
    val isCreditor = expenses.any { it.creditorUserId == currentUserId }
    val isCreator = group?.isOwner == true

    // Load groups when entering the screen
    LaunchedEffect(Unit) {
        groupViewModel.getGroupsByUser(context)
        group?.groupId?.let { expenseViewModel.getExpensesByGroup(context, it) }
    }

    // Detects if banner image color is dark or not
    fun isBackgroundDark(imageUrl: String?): Boolean {
        return imageUrl == null || imageUrl.contains("dark", ignoreCase = true)
    }

    val iconColor = if (isBackgroundDark(group?.imageUrl)) {
        Color.White
    } else {
        Color.Black
    }

    // Show the screen content
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            AsyncImage(
                model = group?.imageUrl ?: R.drawable.chinese_buffet,
                contentDescription = "Group background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, Color.Black.copy(alpha = 3f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Text(
                text = group?.name ?: "Group",
                color = Color.White,
                style = CopayTypography.title,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            )

            backButtonTop(
                onBackClick = {
                    navigationViewModel.navigateBack()
                    groupViewModel.resetGroupSession()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                iconColor = iconColor
            )
        }

        // Screen content
        if (group != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 170.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = group?.description ?: "Description",
                        style = CopayTypography.body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Members: ${(group?.registeredMembers?.size ?: 0) + (group?.externalMembers?.size ?: 0)}",
                        style = CopayTypography.body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Total ${group!!.estimatedPrice} ${group?.currency}",
                        style = CopayTypography.subtitle,
                        fontWeight = FontWeight.Bold,
                        color = CopayColors.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val buttonModifier = Modifier.weight(1f)

                        if (!isCreditor) {
                            manageDebtsButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (isCreator) {
                            Button(
                                onClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditMembers) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = buttonModifier,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(text = "Manage members", maxLines = 1, softWrap = false)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Tabs
                    var selectedTabIndex by remember { mutableStateOf(0) }
                    val tabs = listOf("Members", "Balances")

                    pillTabRow(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        when (selectedTabIndex) {
                            0 -> {
                                val registered = group?.registeredMembers.orEmpty()
                                val external = group?.externalMembers.orEmpty()

                                items(registered) { member ->
                                    val expense = ExpenseUtils.calculateMemberExpense(member, expenses)
                                    memberItem(
                                        member = member,
                                        expense = expense,
                                        currency = group?.currency,
                                        currentUserId = currentUserId
                                    )
                                }

                                items(external) { member ->
                                    val expense = ExpenseUtils.calculateMemberExpense(member, expenses)
                                    memberItem(
                                        member = member,
                                        expense = expense,
                                        currency = group?.currency,
                                        currentUserId = currentUserId
                                    )
                                }
                            }

                            1 -> item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Balances content will be implemented later",
                                        style = CopayTypography.body,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
