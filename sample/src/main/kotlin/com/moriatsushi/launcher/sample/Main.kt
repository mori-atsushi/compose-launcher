package com.moriatsushi.launcher.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moriatsushi.launcher.Entry
import com.utsman.featurex.rememberFeatureXLauncher

@Entry(default = true)
@Composable
fun Main() {
    MaterialTheme {
        val otherLauncher = rememberOtherLauncher()
        val featureXLauncher = rememberFeatureXLauncher()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    otherLauncher.launch()
                },
            ) {
                Text(text = "launch Other page")
            }

            Button(
                onClick = {
                    featureXLauncher.launch()
                },
            ) {
                Text(text = "launch Feature x page")
            }
        }
    }
}
