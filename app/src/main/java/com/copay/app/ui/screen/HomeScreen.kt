package com.copay.app.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.model.Group
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    // Get the username value through the userViewModel.
    val user by userViewModel.user.collectAsState()
    Log.d("HomeScreen", "User in HomeScreen: $user")
    val username = user?.username ?: "Username"
    Log.d("HomeScreen", "User: $user, Username: $username")

    val context = LocalContext.current

    // Group state.
    val groupState by groupViewModel.groupState.collectAsState()
    Log.d("HomeScreen", "${groupState}")

    // Trigger group loading when the screen is first composed
    LaunchedEffect(Unit) {
        groupViewModel.getGroupsByUser(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (groupState) {
            is GroupState.Loading -> {
                Log.d("HomeScreen", "hola")
                Log.d("HomeScreen", "${groupState}")
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is GroupState.Error -> {
                Log.d("HomeScreen", "hola2")
                Text(
                    text = (groupState as GroupState.Error).message,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }

            is GroupState.Success.GroupsFetched -> {

                val response = (groupState as GroupState.Success.GroupsFetched).groupsData

                val groups = response.groups.map { groupDto ->
                    val registeredMembers = groupDto.registeredMembers.map { member ->
                        RegisteredMemberDTO(
                            registeredMemberId = member.registeredMemberId,
                            username = member.username,
                            phoneNumber = member.phoneNumber
                        )
                    }

                    val externalMembers = groupDto.externalMembers.map { member ->
                        ExternalMemberDTO(
                            externalMembersId = member.externalMembersId,
                            name = member.name
                        )
                    }

                    Group(
                        groupId = groupDto.groupId,
                        name = groupDto.name,
                        description = groupDto.description,
                        estimatedPrice = groupDto.estimatedPrice,
                        currency = groupDto.currency,
                        createdAt = groupDto.createdAt,
                        isOwner = groupDto.userIsOwner,
                        ownerId = groupDto.groupOwner.ownerId,
                        ownerName = groupDto.groupOwner.ownerName,
                        registeredMembers = registeredMembers,
                        externalMembers = externalMembers,
                        expenses = emptyList(), // TODO Empty for now (WIP)
                        imageUrl = null,
                        imageProvider = null
                    )
                }
                HomeContent(
                    onCreateClick = { navigationViewModel.navigateTo(SpaScreens.CreateGroup) },
                    onDetailClick = { group ->
                        groupViewModel.selectGroup(group)
                        navigationViewModel.navigateTo(SpaScreens.DetailGroup)
                                    },
                    username = username,
                    groups = groups
                )
            }

            else -> {
                HomeContent(
                    onCreateClick = { navigationViewModel.navigateTo(SpaScreens.CreateGroup) },
                    onDetailClick = { navigationViewModel.navigateTo(SpaScreens.DetailGroup) },
                    username = username,
                    groups = emptyList()
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    // Receives the callbacks from HomeViewModel.
    onCreateClick: () -> Unit,
    onDetailClick: (Group) -> Unit,
    username: String,
    groups: List<Group> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            // Shows the username of the logged user.
            text = "Welcome $username!",
            color = CopayColors.primary,
            style = CopayTypography.title,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Dashboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "1.250€",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "Total spent this month", color = Color.Gray, fontSize = 14.sp
                )

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (index == 1) Color.Black else Color.Gray)
                                .padding(end = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My groups", color = CopayColors.primary, style = CopayTypography.subtitle
            )
            Row {
                TextButton(onClick = onCreateClick) {
                    Text("Create")
                }
            }
        }

        // Display user's groups dynamically
        if (groups.isEmpty()) {
            Text("No tienes grupos aún", modifier = Modifier.padding(vertical = 16.dp))
        } else {
            groups.forEach { group ->
                GroupItem(
                    group = group,
                    onItemClick = { onDetailClick(group) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// TODO Bring this group item into a component
@Composable
private fun GroupItem(
    group: Group,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = group.imageUrl ?: "https://example.com/default-group-image.jpg",
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = group.name ?: "", fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${(group.registeredMembers?.size ?: 0) + (group.externalMembers?.size ?: 0)} members",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(
                text = group.estimatedPrice.toString() + group.currency,
                fontWeight = FontWeight.Bold
            )
        }
    }
}