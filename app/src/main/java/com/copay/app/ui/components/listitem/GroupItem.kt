package com.copay.app.ui.components.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.copay.app.R
import com.copay.app.model.Group

@Composable
fun GroupItem(
    group: Group,
    onItemClick: () -> Unit,
    onEditClick: (Group) -> Unit = {},
    onDeleteClick: (Group) -> Unit = {},
    onLeaveClick: (Group) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main content.
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onItemClick() }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                // Group image.
                AsyncImage(
                    model = group.imageUrl ?: R.drawable.chinese_buffet,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Name and members.
                Column(
                    modifier = Modifier.weight(1f)
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

                // Estimated price.
                Text(
                    text = group.estimatedPrice.toString() + " " + group.currency,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Buttons in the bottom-right.
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.End
            ) {
                if (group.isOwner == true) {
                    TextButton(
                        onClick = { onEditClick(group) },
                        modifier = Modifier.padding(start = 1.dp)
                    ) {
                        Text("Edit")
                    }
                    TextButton(
                        onClick = { onDeleteClick(group) },
                        modifier = Modifier.padding(start = 1.dp)
                    ) {
                        Text("Delete")
                    }
                } else {
                    TextButton(
                        onClick = { onLeaveClick(group) },
                        modifier = Modifier.padding(start = 1.dp)
                    ) {
                        Text("Leave")
                    }
                }
            }
        }
    }
}