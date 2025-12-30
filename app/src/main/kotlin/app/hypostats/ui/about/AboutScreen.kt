package app.hypostats.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.hypostats.BuildConfig
import app.hypostats.R

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = screenHeight)
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

                AboutLinkItem(
                    text = stringResource(R.string.about_support_app),
                    onClick = {
                        uriHandler.openUri("https://buymeacoffee.com/psfinaki")
                    },
                )

                HorizontalDivider()

                Spacer(modifier = Modifier.weight(1f))

                AboutFooter()
            }
        }
    }
}

@Composable
private fun AboutHeader() {
    Spacer(modifier = Modifier.height(32.dp))

    Image(
        painter = painterResource(R.drawable.about_logo),
        contentDescription = null, // decorative and duplicates nearby text, no need for a desc
        modifier = Modifier.size(120.dp),
    )

    Spacer(modifier = Modifier.height(16.dp))

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
    val uriHandler = LocalUriHandler.current

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = "psfinaki.dev",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier =
            Modifier
                .padding(vertical = 16.dp)
                .clickable(role = Role.Button) { uriHandler.openUri("https://psfinaki.dev") },
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
