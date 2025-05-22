package com.copay.app.ui.components.listitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.copay.app.R
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.model.Group

@Composable
fun registeredMemberItem(
    member: RegisteredMemberDTO,
    group: Group,
    isCurrentUser: Boolean,
    onDelete: () -> Unit,
    onLeave: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = member.username,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )

            if (isCurrentUser && group.isOwner == true) {
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_leave),
                        contentDescription = "Delete Group"
                    )
                }
            } else if (isCurrentUser) {
                IconButton(onClick = onLeave) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_leave),
                        contentDescription = "Leave Group"
                    )
                }
            } else if (group.isOwner == true) {
                IconButton(onClick = onRemove) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Remove Member"
                    )
                }
            }
        }
    }
}