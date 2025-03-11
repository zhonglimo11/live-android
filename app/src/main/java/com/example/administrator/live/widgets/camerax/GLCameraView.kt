package com.example.administrator.live.widgets.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Process
import android.util.Size
import android.view.MotionEvent
import android.view.Surface
import androidx.annotation.WorkerThread
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.core.SurfaceRequest
import com.example.administrator.live.widgets.camerax.draw.CameraDrawer
import com.example.administrator.live.widgets.camerax.widget.SlideGpuFilterGroup
import jp.co.cyberagent.android.gpuimage.GLTextureView
import java.util.concurrent.Executors
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@SuppressLint("ViewConstructor")
class GLCameraView(context: Context, private val ratio: Float) : GLTextureView(context),
    GLTextureView.Renderer, SurfaceProvider, SurfaceTexture.OnFrameAvailableListener {

    // 创建一个单线程执行器，用于处理后台任务
    private val executor = Executors.newSingleThreadExecutor { r ->
        object : Thread(r, "GLCameraViewPool") {
            override fun run() {
                // 设置线程优先级为后台优先级，防止影响前台性能
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                super.run()
            }
        }
    }

    // CameraDrawer 对象，用于处理相机预览和绘制
    private val mCameraDrawer: CameraDrawer = CameraDrawer(context.resources)

    /**
     * 是否来自前后摄像头切换
     */
    private var fromCameraLensFacing: Boolean = false

    /**
     * 左右水平滑动切换滤镜功能是否可用
     */
    var slideGpuFilterGroupEnable: Boolean = true

    // 切换摄像头，处理前后摄像头切换时的画面颠倒问题
    fun switchCameraLensFacing(fromCameraLensFacing: Boolean, lensFacing: Int) {
        this.fromCameraLensFacing = fromCameraLensFacing
        if (fromCameraLensFacing) {
            mCameraDrawer.setCameraId(lensFacing)
        }
    }

    // 初始化 GLCameraView
    init {
        // 设置 OpenGL 版本为 2.0
        setEGLContextClientVersion(2)
        // 设置渲染器为当前对象
        setRenderer(this)
        // 设置渲染模式为手动调用
        renderMode = RENDERMODE_WHEN_DIRTY
        // 保存 EGL 上下文，防止在暂停时丢失
        preserveEGLContextOnPause = true
        // 设置相机距离
        cameraDistance = 100F
    }

    // 当 OpenGL 表面创建时调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mCameraDrawer.onSurfaceCreated(gl, config)
        // 设置预览大小
        mCameraDrawer.setPreviewSize(
            resources.displayMetrics.widthPixels,
            (resources.displayMetrics.widthPixels / ratio).toInt()
        )
    }

    // 当 OpenGL 表面大小改变时调用
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mCameraDrawer.onSurfaceChanged(gl, width, height)
    }

    // 当需要绘制帧时调用
    override fun onDrawFrame(gl: GL10?) {
        mCameraDrawer.onDrawFrame(gl)
    }

    // 当新的帧可用时调用
    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        requestRender()
    }

    // 当请求表面时调用
    override fun onSurfaceRequested(request: SurfaceRequest) {
        val resetTexture = resetPreviewTexture(request.resolution) ?: return
        val surface = Surface(resetTexture)
        request.provideSurface(surface, executor) {
            surface.release()
            if (!fromCameraLensFacing) {
                // 切换前后摄像头时，不能释放 surfaceTexture
                surfaceTexture?.release()
            }
        }
    }

    // 重置预览纹理
    @WorkerThread
    private fun resetPreviewTexture(size: Size): SurfaceTexture? {
        mCameraDrawer.texture?.setOnFrameAvailableListener(this)
        mCameraDrawer.texture?.setDefaultBufferSize(size.width, size.height)
        return mCameraDrawer.texture
    }

    // 获取美颜级别
    fun getBeautyLevel(): Float {
        return mCameraDrawer.beautyLevel
    }

    // 改变美颜级别
    fun changeBeautyLevel(level: Int) {
        queueEvent { mCameraDrawer.changeBeautyLevel(level) }
    }

    // 开始或停止录制视频
    fun takeVideo(path: String?) {
        if (mCameraDrawer.recordingEnabled) {
            stopRecord()
        } else {
            startRecord(path)
        }
    }

    // 拍照
    fun takePicture(path: String?) {
        if (!mCameraDrawer.takePictureEnabled) {
            mCameraDrawer.setSavePath(path)
            mCameraDrawer.startCapture()
        }
    }

    // 开始录制视频
    private fun startRecord(path: String?) {
        mCameraDrawer.setSavePath(path)
        mCameraDrawer.startRecord()
    }

    // 停止录制视频
    private fun stopRecord() {
        mCameraDrawer.stopRecord()
    }

    // 检查是否正在录制视频
    fun isRecording() = mCameraDrawer.isRecording()

    // 获取录制时间状态
    fun getRecordingTimeState() = mCameraDrawer.recordingTimeState

    // 处理触摸事件
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && !mCameraDrawer.recordingEnabled && slideGpuFilterGroupEnable) {
            queueEvent { mCameraDrawer.onTouch(event) }
        }
        return true
    }

    // 设置滤镜变化监听器
    fun setOnFilterChangeListener(listener: SlideGpuFilterGroup.OnFilterChangeListener?) {
        mCameraDrawer.setOnFilterChangeListener(listener)
    }

    // 当视图从窗口分离时调用
    override fun onDetachedFromWindow() {
        mCameraDrawer.release()
        super.onDetachedFromWindow()
    }
}
