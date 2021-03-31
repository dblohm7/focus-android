package org.mozilla.focus.activity.robots

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mozilla.focus.helpers.TestHelper.appName
import org.mozilla.focus.helpers.TestHelper.isPackageInstalled
import org.mozilla.focus.helpers.TestHelper.mDevice
import org.mozilla.focus.helpers.TestHelper.waitingTime
import org.mozilla.focus.helpers.TestHelper.webPageLoadwaitingTime

class DownloadRobot {
    val downloadIconAsset: UiObject = mDevice.findObject(
        UiSelector()
            .resourceId("download")
            .enabled(true)
    )

    fun verifyDownloadDialog(fileName: String) {
        assertTrue(downloadDialogTitle.waitForExists(waitingTime))
        assertTrue(downloadCancelBtn.exists())
        assertTrue(downloadBtn.exists())
        assertEquals(downloadFileName.text, fileName)
    }

    fun verifyDownloadDialogGone() = assertTrue(downloadDialogTitle.waitUntilGone(waitingTime))

    fun verifyPhotosOpens() = assertPhotosOpens()

    fun clickDownloadButton() {
        downloadBtn.waitForExists(waitingTime)
        downloadBtn.click()
    }

    fun clickCancelDownloadButton() {
        downloadCancelBtn.waitForExists(waitingTime)
        downloadCancelBtn.click()
    }

    fun openDownloadedFile() {
        val snackBarButton = mDevice.findObject(UiSelector().resourceId("$appName:id/snackbar_action"))
        snackBarButton.waitForExists(waitingTime)
        snackBarButton.clickAndWaitForNewWindow(webPageLoadwaitingTime)
    }

    class Transition
}

fun downloadRobot(interact: DownloadRobot.() -> Unit): DownloadRobot.Transition {
    DownloadRobot().interact()
    return DownloadRobot.Transition()
}

private val downloadDialogTitle = mDevice.findObject(
    UiSelector()
        .resourceId("$appName:id/title")
        .enabled(true)
)

private val downloadFileName = mDevice.findObject(
    UiSelector()
        .resourceId("$appName:id/filename")
        .enabled(true)
)

private val downloadCancelBtn = mDevice.findObject(
    UiSelector()
        .resourceId("$appName:id/close_button")
        .enabled(true)
)

private val downloadBtn = mDevice.findObject(
    UiSelector()
        .resourceId("$appName:id/download_button")
        .enabled(true)
)

private fun assertPhotosOpens() {
    val GOOGLE_APPS_PHOTOS = "com.google.android.apps.photos"
    if (isPackageInstalled(GOOGLE_APPS_PHOTOS)) {
        Intents.intended(IntentMatchers.toPackage(GOOGLE_APPS_PHOTOS))
    } else {
        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        mDevice.findObject(UiSelector().textContains("No app found")).waitForExists(waitingTime)
    }
}
