import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

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
        title = {
            Column {
                Text("Group Members", style = CopayTypography.title)
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    thickness = 2.dp
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (registeredMembers.isNotEmpty()) {
                    Text(
                        text = "Registered Members",
                        style = CopayTypography.body,
                        color = CopayColors.primary,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 150.dp)
                    ) {
                        items(registeredMembers) { member ->
                            MemberItem(
                                memberName = member,
                                currentUserPhone = currentUserPhone,
                                onRemove = { onRemoveRegisteredMember(member) }
                            )
                        }
                    }
                }

                if (externalMembers.isNotEmpty()) {
                    Text(
                        text = "External Members",
                        style = CopayTypography.body,
                        color = CopayColors.primary,
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 150.dp)
                    ) {
                        items(externalMembers) { member ->
                            MemberItem(
                                memberName = member,
                                currentUserPhone = currentUserPhone,
                                onRemove = { onRemoveExternalMember(member) }
                            )
                        }
                    }
                }

                if (registeredMembers.isEmpty() && externalMembers.isEmpty()) {
                    Text(
                        text = "No members in the group.",
                        style = CopayTypography.body,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
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
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isCurrentUser) "$memberName (You)" else memberName,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (!isCurrentUser) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove member",
                    tint = Color.Red
                )
            }
        }
    }
}
