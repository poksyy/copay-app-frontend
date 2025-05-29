package com.copay.app.ui.components.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.copay.app.R
import com.copay.app.model.Group
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.copay.app.ui.theme.adaptiveGray

@Composable
fun groupItem(
    group: Group,
    onItemClick: () -> Unit,
    onEditClick: (Group) -> Unit = {},
    onDeleteClick: (Group) -> Unit = {},
    onLeaveClick: (Group) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = CopayColors.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display group image (fallback to default if null)
            AsyncImage(
                model = group.imageUrl ?: R.drawable.group_default_image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Group details: name, member count, price
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name.orEmpty(),
                    style = CopayTypography.body.copy(fontWeight = FontWeight.SemiBold),
                    color = CopayColors.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(group.registeredMembers?.size ?: 0) + (group.externalMembers?.size ?: 0)} members",
                    style = CopayTypography.footer,
                    color = CopayColors.onSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${group.estimatedPrice} ${group.currency}",
                    style = CopayTypography.body,
                    color = CopayColors.primary
                )
            }

            // Action buttons (Edit/Delete if owner, Leave if not)
            ActionButtons(
                isOwner = group.isOwner == true,
                onEdit = { onEditClick(group) },
                onDelete = { onDeleteClick(group) },
                onLeave = { onLeaveClick(group) }
            )
        }
    }
}

@Composable
private fun ActionButtons(
    isOwner: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onLeave: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isOwner) {
            // Show edit and delete icons for owners
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = adaptiveGray(),
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = "Delete",
                    tint = adaptiveGray(),
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            // Show leave icon for non-owners
            IconButton(onClick = onLeave) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_leave),
                    contentDescription = "Leave",
                    tint = adaptiveGray(),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}