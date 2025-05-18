import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.copay.app.ui.components.ScrollIndicator
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListDialog(
    onDismiss: () -> Unit,
    registeredMembers: List<String>,
    externalMembers: List<String>,
    onRemoveRegisteredMember: (String) -> Unit = {},
    onRemoveExternalMember: (String) -> Unit = {},
    currentUserPhone: String?
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.width(320.dp),
        title = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "View Members",
                        style = CopayTypography.title,
                        color = CopayColors.primary
                    )
                }
            }
        },
        text = {

            Column(modifier = Modifier.fillMaxWidth()) {

                if (registeredMembers.isNotEmpty()) {
                    MemberSection(
                        title = "Registered Members",
                        members = registeredMembers,
                        currentUserPhone = currentUserPhone,
                        onRemove = onRemoveRegisteredMember
                    )
                }

                if (externalMembers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    MemberSection(
                        title = "External Members",
                        members = externalMembers,
                        currentUserPhone = currentUserPhone,
                        onRemove = onRemoveExternalMember
                    )
                }

                if (registeredMembers.isEmpty() && externalMembers.isEmpty()) {
                    EmptyMembersList()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", style = CopayTypography.body)
            }
        }
    )
}

@Composable
private fun MemberSection(
    title: String,
    members: List<String>,
    currentUserPhone: String?,
    onRemove: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val maxVisibleItems = 2
    val itemHeightDp = 60.dp
    val listHeight = (maxVisibleItems * itemHeightDp.value).dp

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (title.contains("Registered"))
                CopayColors.primary.copy(alpha = 0.08f)
            else
                Color.LightGray.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "$title (${members.size})",
                    style = CopayTypography.body.copy(fontWeight = FontWeight.Bold),
                    color = CopayColors.primary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(listHeight)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    items(members) { member ->
                        MemberItem(
                            memberName = member,
                            currentUserPhone = currentUserPhone,
                            onRemove = { onRemove(member) }
                        )
                        if (member != members.last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = Color.Gray.copy(alpha = 0.2f),
                                thickness = 0.5.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                ScrollIndicator(
                    listState = listState,
                    totalItems = members.size,
                    visibleItemsCount = maxVisibleItems,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun MemberItem(
    memberName: String,
    currentUserPhone: String?,
    onRemove: () -> Unit
) {
    val isCurrentUser = memberName == currentUserPhone

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (isCurrentUser) CopayColors.primary
                    else CopayColors.primary.copy(alpha = 0.5f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = memberName.take(1).uppercase(),
                color = Color.White,
                style = CopayTypography.body.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isCurrentUser) "$memberName (You)" else memberName,
                style = if (isCurrentUser)
                    CopayTypography.body.copy(fontWeight = FontWeight.Bold)
                else
                    CopayTypography.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (!isCurrentUser) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove member",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyMembersList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.PersonOutline,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No members in the group",
                style = CopayTypography.body,
                color = Color.Gray
            )
        }
    }
}