package com.xxmrk888ytxx.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalBaselineProfilesApi::class)
@RunWith(AndroidJUnit4::class)
class BaseProfileGenerator {
    @get:Rule
    val baseLineRule = BaselineProfileRule()

    @Test
    fun generateBaseProfile() = baseLineRule.collectBaselineProfile(
        packageName = "com.xxmrk888ytxx.privatenote",
    ) {
        pressHome()
        startActivityAndWait()
    }
}