import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun MemberListDialog(
    onDismiss: () -> Unit, registeredMembers: List<String>, externalMembers: List<String>
) {
    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text("Close")
        }
    }, title = {
        Text("Group Members")
    }, text = {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (registeredMembers.isNotEmpty()) {
                Text(
                    text = "Registered Members",
                    style = CopayTypography.body,
                    color = CopayColors.primary
                )
                LazyColumn {
                    items(registeredMembers) { member ->
                        Text(text = member, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (externalMembers.isNotEmpty()) {
                Text(
                    text = "External Members",
                    style = CopayTypography.body,
                    color = CopayColors.primary
                )
                LazyColumn {
                    items(externalMembers) { member ->
                        Text(text = member, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    })
}
