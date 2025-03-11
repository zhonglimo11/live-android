package com.example.administrator.live.core.ui.customui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.administrator.live.R

@Composable
fun ShowFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    val tag = fragment::class.java.name
    val existingFragment = fragmentManager.findFragmentByTag(tag)

    LaunchedEffect(Unit) {
        if (existingFragment == null) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .show(existingFragment)
                .commit()
        }
    }
}