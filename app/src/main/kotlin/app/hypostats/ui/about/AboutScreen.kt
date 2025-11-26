package app.hypostats.ui.about

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import app.hypostats.R

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.privacy_policy),
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                ),
            modifier =
                Modifier
                    .clickable {
                        val intent =
                            Intent(
                                Intent.ACTION_VIEW,
                                "https://github.com/psfinaki/HypoStats/blob/main/PRIVACY.md".toUri(),
                            )
                        context.startActivity(intent)
                    }.padding(vertical = 8.dp),
        )
    }
}

@Preview(showBackground = true, name = "Portrait")
@Preview(showBackground = true, name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun AboutScreenPreview() {
    MaterialTheme {
        AboutScreen()
    }
}
