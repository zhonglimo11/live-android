package com.example.administrator.live.core.activitys;

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.administrator.live.core.ui.main.MainScreen
import com.example.administrator.live.core.viewmodels.MainViewModel
import org.opencv.BuildConfig
import org.opencv.android.OpenCVLoader
import timber.log.Timber

class MainActivity : AppCompatActivity(){
    companion object {
        private val BASIC_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startRequestPermission()
        logStart()
        checkOpenCV()
        setContent {
            MainScreen(viewModel)
        }
    }

    private fun checkOpenCV(){
        if (OpenCVLoader.initLocal()) {
            Timber.d("OpenCV loaded")
        }else{
            Timber.d("OpenCV not loaded")
        }
    }

    private fun logStart() {
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }else{
            Timber.plant(CrashReportingTree())
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        }
    }

    private fun startRequestPermission() {
        val permissionsToRequest = BASIC_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, 123)
        }
    }
}