package com.example.administrator.live.utils
import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import timber.log.Timber
import java.io.File

@UnstableApi
class ExoPlayerManager private constructor(context: Context) {
    private val exoPlayer: ExoPlayer
    private val cache: Cache

    init {
        // 设置缓存目录和大小
        val cacheDir = File(context.cacheDir, "media_cache")
        val cacheSize = 100 * 1024 * 1024L // 100 MB

        cache = SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(cacheSize))

        // 配置缓存数据源并允许重定向
        val httpDataSourceFactory = DefaultHttpDataSource.Factory().apply {
            setAllowCrossProtocolRedirects(true) // 允许重定向
        }
        val dataSourceFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(dataSourceFactory)
            .setFlags(CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        // 初始化 ExoPlayer
        exoPlayer = ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build()

        exoPlayer.volume = 1.0f // 设置音量为最大
    }

    fun playMusic(url: String, context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioFocusResult = audioManager.requestAudioFocus(
            { }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN
        )

        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            exoPlayer.stop()  // 停止当前音乐
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        } else {
            Timber.e("Audio focus not granted")
        }
    }

    fun stopMusic() {
        exoPlayer.stop()
    }

    fun release() {
        exoPlayer.release()
        cache.release()
    }

    companion object {
        @Volatile
        private var instance: ExoPlayerManager? = null

        fun getInstance(context: Context): ExoPlayerManager {
            return instance ?: synchronized(this) {
                instance ?: ExoPlayerManager(context).also { instance = it }
            }
        }
    }
}