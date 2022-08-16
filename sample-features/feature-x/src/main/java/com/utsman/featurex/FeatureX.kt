package com.utsman.featurex

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.moriatsushi.launcher.Entry

@Entry
@Composable
fun FeatureX() {
    MaterialTheme {
        Text(text = "Halo from Feature X module")
    }
}
