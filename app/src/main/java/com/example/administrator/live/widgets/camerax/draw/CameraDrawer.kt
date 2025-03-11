package com.example.administrator.live.widgets.camerax.draw

import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.view.MotionEvent
import com.example.administrator.live.widgets.camerax.encoder.TextureMovieEncoder
import com.example.administrator.live.widgets.camerax.encoder.TexturePictureEncoder
import com.example.administrator.live.widgets.camerax.filters.BaseFilter
import com.example.administrator.live.widgets.camerax.filters.CameraDrawProcessFilter
import com.example.administrator.live.widgets.camerax.filters.CameraFilter
import com.example.administrator.live.widgets.camerax.filters.GroupFilter
import com.example.administrator.live.widgets.camerax.filters.NoneFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicBeautyFilter2
import com.example.administrator.live.widgets.camerax.model.IListenerRecordTime
import com.example.administrator.live.widgets.camerax.utils.EasyGlUtils.bindFrameTexture
import com.example.administrator.live.widgets.camerax.utils.EasyGlUtils.unBindFrameBuffer
import com.example.administrator.live.widgets.camerax.utils.GLCameraxUtils
import com.example.administrator.live.widgets.camerax.utils.MatrixUtils.flip
import com.example.administrator.live.widgets.camerax.utils.MatrixUtils.getShowMatrix
import com.example.administrator.live.widgets.camerax.utils.MatrixUtils.originalMatrix
import com.example.administrator.live.widgets.camerax.widget.SlideGpuFilterGroup
import com.example.administrator.live.widgets.camerax.widget.SlideGpuFilterGroup.OnFilterChangeListener
import jp.co.cyberagent.android.gpuimage.GLTextureView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraDrawer(resources: Resources) : GLTextureView.Renderer, IListenerRecordTime {
    private val OM: FloatArray

    /**显示画面的filter */
    private val showFilter: BaseFilter

    /**后台绘制的filter */
    private val drawFilter: BaseFilter

    /**绘制水印的filter组 */
    private val mBeFilter: GroupFilter
    private val mAfFilter: GroupFilter

    /**用于绘制美白效果的filter */
    private val mProcessFilter: BaseFilter

    /**美白的filter */
    private val mBeautyFilter: MagicBeautyFilter2?

    /**多种滤镜切换 */
    private val mSlideFilterGroup: SlideGpuFilterGroup

    /**
     * 视频录制显示当前的时间
     */
    private val _recordingTimeState = MutableStateFlow("00:00:00")
    val recordingTimeState: StateFlow<String>
        get() = _recordingTimeState.asStateFlow()

    var texture: SurfaceTexture? = null
        private set

    /**预览数据的宽高 */
    private var mPreviewWidth = 0
    private var mPreviewHeight = 0

    /**控件的宽高 */
    private var width = 0
    private var height = 0

    /**
     * 视频编码器
     */
    private var videoEncoder: TextureMovieEncoder? = null

    /**
     * 照片编码器
     */
    private var pictureEncoder: TexturePictureEncoder? = null

    /**
     * 点击拍视频
     */
    var recordingEnabled = false
        private set

    /**
     * 点击拍照片
     */
    var takePictureEnabled = false
        private set

    private var recordingStatus = 0
    private var savePath: String? = null
    private var textureID = 0
    private val fFrame = IntArray(1)
    private val fTexture = IntArray(1)
    private val SM = FloatArray(16) //用于显示的变换矩阵
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        textureID = createTextureID()
        texture = SurfaceTexture(textureID)
        drawFilter.create()
        drawFilter.textureId = textureID
        mProcessFilter.create()
        showFilter.create()
        mBeFilter.create()
        mAfFilter.create()
        mBeautyFilter?.init()
        mSlideFilterGroup.init()
        recordingStatus = if (recordingEnabled) {
            RECORDING_RESUMED
        } else {
            RECORDING_OFF
        }
    }

    /**创建显示的texture */
    private fun createTextureID(): Int {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0) //第一个参数表示创建几个纹理对象，并将创建好的纹理对象放置到第二个参数中去
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE
        )
        return texture[0]
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width  // 更新当前视图的宽度
        this.height = height  // 更新当前视图的高度

        // 清除遗留的帧缓冲区和纹理
        GLES20.glDeleteFramebuffers(1, fFrame, 0)  // 删除帧缓冲区
        GLES20.glDeleteTextures(1, fTexture, 0)  // 删除纹理

        /** 创建一个帧缓冲区对象 */
        GLES20.glGenFramebuffers(1, fFrame, 0)  // 生成一个帧缓冲区对象

        /** 根据纹理数量返回的纹理索引 */
        GLES20.glGenTextures(1, fTexture, 0)  // 生成一个纹理对象

        /** 将生成的纹理名称和对应纹理进行绑定 */
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[0])  // 绑定2D纹理

        /** 根据指定的参数生成一个2D的纹理，在调用此函数前必须调用glBindTexture以指定要操作的纹理 */
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,  // 目标纹理类型
            0,  // 纹理层次级别
            GLES20.GL_RGBA,  // 内部格式
            mPreviewWidth,  // 纹理宽度
            mPreviewHeight,  // 纹理高度
            0,  // 边框宽度
            GLES20.GL_RGBA,  // 数据格式
            GLES20.GL_UNSIGNED_BYTE,  // 数据类型
            null  // 纹理数据
        )

        useTexParameter()  // 设置纹理参数
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)  // 解绑2D纹理

        // 设置滤镜的尺寸
        mProcessFilter.setSize(mPreviewWidth, mPreviewHeight)  // 设置处理滤镜的尺寸
        mBeFilter.setSize(mPreviewWidth, mPreviewHeight)  // 设置美颜滤镜的尺寸
        mAfFilter.setSize(mPreviewWidth, mPreviewHeight)  // 设置自动对焦滤镜的尺寸
        drawFilter.setSize(mPreviewWidth, mPreviewHeight)  // 设置绘制滤镜的尺寸

        // 通知美颜滤镜显示尺寸和输入尺寸的变化
        mBeautyFilter?.onDisplaySizeChanged(mPreviewWidth, mPreviewHeight)  // 通知美颜滤镜显示尺寸变化
        mBeautyFilter?.onInputSizeChanged(mPreviewWidth, mPreviewHeight)  // 通知美颜滤镜输入尺寸变化

        // 通知滑动滤镜组尺寸的变化
        mSlideFilterGroup.onSizeChanged(mPreviewWidth, mPreviewHeight)  // 通知滑动滤镜组尺寸变化

        // 计算显示矩阵
        getShowMatrix(SM, mPreviewWidth, mPreviewHeight, width, height)  // 计算显示矩阵
        showFilter.matrix = SM  // 设置显示滤镜的矩阵
    }


    override fun onDrawFrame(gl: GL10?) {
        /** 更新界面中的数据 */
        texture?.updateTexImage()  // 更新纹理图像
        bindFrameTexture(fFrame[0], fTexture[0])  // 绑定帧缓冲区纹理
        GLES20.glViewport(0, 0, mPreviewWidth, mPreviewHeight)  // 设置视口大小
        drawFilter.draw()  // 绘制滤镜
        unBindFrameBuffer()  // 解绑帧缓冲区
        mBeFilter.textureId = fTexture[0]  // 设置美颜滤镜的纹理ID
        mBeFilter.draw()  // 绘制美颜滤镜
        if (mBeautyFilter != null && mBeautyFilter.beautyFeatureEnable) {
            mBeautyFilter.onDrawFrame(mBeFilter.outputTexture)  // 如果美颜功能启用，绘制美颜滤镜
            mProcessFilter.textureId = fTexture[0]  // 设置处理滤镜的纹理ID
        } else {
            mProcessFilter.textureId = mBeFilter.outputTexture  // 设置处理滤镜的纹理ID为美颜滤镜的输出纹理
        }
        mProcessFilter.draw()  // 绘制处理滤镜
        mSlideFilterGroup.onDrawFrame(mProcessFilter.outputTexture)  // 绘制滑动滤镜组
        mAfFilter.textureId = mSlideFilterGroup.outputTexture  // 设置自动对焦滤镜的纹理ID
        mAfFilter.draw()  // 绘制自动对焦滤镜
        recording()  // 处理录制逻辑
        /* 绘制显示的滤镜 */
        GLES20.glViewport(0, 0, width, height)  // 设置视口大小
        showFilter.textureId = mAfFilter.outputTexture  // 设置显示滤镜的纹理ID
        showFilter.draw()  // 绘制显示滤镜
        if (recordingEnabled && recordingStatus == RECORDING_ON) {
            videoEncoder?.setTextureId(mAfFilter.outputTexture)  // 设置视频编码器的纹理ID
            videoEncoder?.frameAvailable(texture)  // 通知视频编码器帧可用
        } else if (takePictureEnabled) {
            takePictureEnabled = false  // 重置拍照标志
            pictureEncoder?.setTextureId(mAfFilter.outputTexture)  // 设置图片编码器的纹理ID
            pictureEncoder?.frameAvailable(texture)  // 通知图片编码器帧可用
        }
    }

    /**
     * 处理录制逻辑
     */
    private fun recording() {
        if (takePictureEnabled) {
            pictureEncoder = TexturePictureEncoder()  // 创建图片编码器
            pictureEncoder?.setPreviewSize(mPreviewWidth, mPreviewHeight)  // 设置预览大小
            pictureEncoder?.startCapture(
                TexturePictureEncoder.EncoderConfig(
                    path = savePath ?: "",  // 设置保存路径
                    mWidth = mPreviewWidth,  // 设置宽度
                    mHeight = mPreviewHeight,  // 设置高度
                    mBitRate = -1,  // 设置比特率
                    mEglContext = EGL14.eglGetCurrentContext()  // 获取当前EGL上下文
                )
            )
        } else {
            if (recordingEnabled) {
                /* 说明是录制状态 */
                when (recordingStatus) {
                    RECORDING_OFF -> {
                        videoEncoder = TextureMovieEncoder()  // 创建视频编码器
                        videoEncoder?.setRecordTimeListener(this)  // 设置录制时间监听器
                        videoEncoder?.setPreviewSize(mPreviewWidth, mPreviewHeight)  // 设置预览大小
                        videoEncoder?.startRecording(
                            TextureMovieEncoder.EncoderConfig(
                                savePath ?: "",  // 设置保存路径
                                mPreviewWidth,  // 设置宽度
                                mPreviewHeight,  // 设置高度
                                3500000,  // 设置比特率
                                EGL14.eglGetCurrentContext()  // 获取当前EGL上下文
                            )
                        )
                        recordingStatus = RECORDING_ON  // 设置录制状态为开启
                    }

                    RECORDING_RESUMED -> {
                        videoEncoder?.updateSharedContext(EGL14.eglGetCurrentContext())  // 更新共享上下文
                        videoEncoder?.resumeRecording()  // 恢复录制
                        recordingStatus = RECORDING_ON  // 设置录制状态为开启
                    }

                    RECORDING_ON, RECORDING_PAUSED -> {}  // 录制状态为开启或暂停时不做处理
                    RECORDING_PAUSE -> {
                        videoEncoder?.pauseRecording()  // 暂停录制
                        recordingStatus = RECORDING_PAUSED  // 设置录制状态为暂停
                    }

                    RECORDING_RESUME -> {
                        videoEncoder?.resumeRecording()  // 恢复录制
                        recordingStatus = RECORDING_ON  // 设置录制状态为开启
                    }

                    else -> throw RuntimeException("unknown recording status $recordingStatus")  // 抛出未知录制状态异常
                }
            } else {
                when (recordingStatus) {
                    RECORDING_ON, RECORDING_RESUMED, RECORDING_PAUSE, RECORDING_RESUME, RECORDING_PAUSED -> {
                        videoEncoder?.stopRecording()  // 停止录制
                        recordingStatus = RECORDING_OFF  // 设置录制状态为关闭
                        videoEncoder = null  // 释放视频编码器
                    }

                    RECORDING_OFF -> {}  // 录制状态为关闭时不做处理
                    else -> throw RuntimeException("unknown recording status $recordingStatus")  // 抛出未知录制状态异常
                }
            }
        }
    }


    /**设置预览效果的size */
    fun setPreviewSize(width: Int, height: Int) {
        if (mPreviewWidth != width || mPreviewHeight != height) {
            mPreviewWidth = width
            mPreviewHeight = height
        }
    }

    /**提供修改美白等级的接口 */
    fun changeBeautyLevel(level: Int) {
        mBeautyFilter?.beautyLevel = level.toFloat()
    }

    val beautyLevel: Float
        get() = mBeautyFilter?.beautyLevel ?: 0F

    /**根据摄像头设置纹理映射坐标 */
    fun setCameraId(id: Int) {
        drawFilter.flag = id
    }

    fun isRecording() = videoEncoder?.isRecording ?: false

    fun startRecord() {
        recordingEnabled = true
    }

    fun stopRecord() {
        recordingEnabled = false
    }

    fun startCapture() {
        takePictureEnabled = true
    }

    fun setSavePath(path: String?) {
        savePath = path
    }

    fun onPause(auto: Boolean) {
        if (auto) {
            videoEncoder?.pauseRecording()
            if (recordingStatus == RECORDING_ON) {
                recordingStatus = RECORDING_PAUSED
            }
            return
        }
        if (recordingStatus == RECORDING_ON) {
            recordingStatus = RECORDING_PAUSE
        }
    }

    fun onResume(auto: Boolean) {
        if (auto) {
            if (recordingStatus == RECORDING_PAUSED) {
                recordingStatus = RECORDING_RESUME
            }
            return
        }
        if (recordingStatus == RECORDING_PAUSED) {
            recordingStatus = RECORDING_RESUME
        }
    }

    private fun useTexParameter() {
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST.toFloat()
        )
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE.toFloat()
        )
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE.toFloat()
        )
    }

    /**
     * 触摸事件的传递
     */
    fun onTouch(event: MotionEvent) {
        mSlideFilterGroup.onTouchEvent(event)
    }

    /**
     * 滤镜切换的事件监听
     */
    fun setOnFilterChangeListener(listener: OnFilterChangeListener?) {
        mSlideFilterGroup.setOnFilterChangeListener(listener)
    }

    companion object {
        private const val RECORDING_OFF = 0
        private const val RECORDING_ON = 1
        private const val RECORDING_RESUMED = 2
        private const val RECORDING_PAUSE = 3
        private const val RECORDING_RESUME = 4
        private const val RECORDING_PAUSED = 5
    }

    init {
        // 初始化一个滤镜控制器
        showFilter = NoneFilter(resources)
        drawFilter = CameraFilter(resources)
        mProcessFilter = CameraDrawProcessFilter(resources)
        mBeFilter = GroupFilter(resources)
        mAfFilter = GroupFilter(resources)
        mBeautyFilter = MagicBeautyFilter2()
        mSlideFilterGroup = SlideGpuFilterGroup()
        OM = originalMatrix
        // 矩阵上下翻转
        flip(OM, false, false)
        showFilter.matrix = OM
    }

    /**
     * 通过回调获取TextureMovieEncoder里面当前视频拍摄的时间
     */
    override fun onUpdateRecordTime(time: Long) {
        _recordingTimeState.value = GLCameraxUtils.getTimeInfoFromNanoTime(time)
    }

    fun release() {
        setOnFilterChangeListener(null)
        videoEncoder?.setRecordTimeListener(null)
        pictureEncoder?.releaseEncoder()
        pictureEncoder = null
        videoEncoder = null
    }
}