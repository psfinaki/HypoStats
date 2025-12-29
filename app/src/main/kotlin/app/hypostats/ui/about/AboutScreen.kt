package app.hypostats.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.hypostats.BuildConfig
import app.hypostats.R

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AboutHeader()

        HorizontalDivider()

        AboutLinkItem(
            text = stringResource(R.string.about_source_code),
            onClick = {
                uriHandler.openUri("https://github.com/psfinaki/HypoStats")
            },
        )

        HorizontalDivider()

        AboutLinkItem(
            text = stringResource(R.string.about_report_issue),
            onClick = {
                uriHandler.openUri("https://github.com/psfinaki/HypoStats/issues")
            },
        )

        HorizontalDivider()

        AboutLinkItem(
            text = stringResource(R.string.about_contact_dev),
            onClick = {
                uriHandler.openUri("mailto:info@psfinaki.dev")
            },
        )

        HorizontalDivider()

        AboutLinkItem(
            text = stringResource(R.string.about_privacy_policy),
            onClick = {
                uriHandler.openUri("https://github.com/psfinaki/HypoStats/blob/main/PRIVACY.md")
            },
        )

        HorizontalDivider()

        AboutFooter()
    }
}

@Composable
private fun AboutHeader() {
    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.headlineMedium,
    )

    Text(
        text = stringResource(R.string.about_version, BuildConfig.VERSION_NAME),
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Text(
        text = stringResource(R.string.about_desc),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 8.dp),
    )

    Spacer(modifier = Modifier.height(48.dp))
}

@Composable
private fun AboutFooter() {
    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = stringResource(R.string.about_dev),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 16.dp),
    )
}

@Preview(name = "Portrait")
@Preview(name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun AboutScreenPreview() {
    MaterialTheme {
        AboutScreen()
    }
}

@Preview
@Composable
private fun AboutHeaderPreview() {
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AboutHeader()
        }
    }
}

@Preview
@Composable
private fun AboutFooterPreview() {
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AboutFooter()
        }
    }
}
