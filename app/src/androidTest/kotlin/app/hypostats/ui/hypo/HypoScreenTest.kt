package app.hypostats.ui.hypo

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.hypostats.R
import app.hypostats.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HypoScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun addingSugar_updatesSugarAmount() {
        val sugarButtonDesc = composeTestRule.activity.getString(R.string.eat_5g_sugar)

        composeTestRule.onNodeWithText("0g sugar").assertExists()

        composeTestRule.onNodeWithContentDescription(sugarButtonDesc).performClick()

        composeTestRule.onNodeWithText("5g sugar").assertExists()

        composeTestRule.onNodeWithContentDescription(sugarButtonDesc).performClick()

        composeTestRule.onNodeWithText("10g sugar").assertExists()
    }

    @Test
    fun submitButton_enabledOnlyWhenSugarAdded() {
        val sugarButtonDesc = composeTestRule.activity.getString(R.string.eat_5g_sugar)
        val submitButtonText = composeTestRule.activity.getString(R.string.submit_treatment)

        composeTestRule.onNodeWithText(submitButtonText).assertIsNotEnabled()

        composeTestRule.onNodeWithContentDescription(sugarButtonDesc).performClick()

        composeTestRule.onNodeWithText(submitButtonText).assertIsEnabled()
    }
}
