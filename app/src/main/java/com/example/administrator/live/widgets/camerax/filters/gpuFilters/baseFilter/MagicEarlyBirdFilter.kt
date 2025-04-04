package com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter

import com.example.administrator.live.widgets.camerax.filters.gpuFilters.utils.OpenGlUtils.readShaderFromRawResource
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.utils.OpenGlUtils.loadTexture
import com.example.administrator.live.widgets.camerax.utils.GLCameraxUtils.Companion.getApplicationContext
import com.example.administrator.live.R
import android.opengl.GLES20
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.utils.OpenGlUtils

class MagicEarlyBirdFilter :
    GPUImageFilter(NO_FILTER_VERTEX_SHADER, readShaderFromRawResource(R.raw.earlybird)) {
    private val inputTextureHandles = intArrayOf(-1, -1, -1, -1, -1)
    private val inputTextureUniformLocations = intArrayOf(-1, -1, -1, -1, -1)
    protected var mGLStrengthLocation = 0
    override fun onDestroy() {
        super.onDestroy()
        GLES20.glDeleteTextures(inputTextureHandles.size, inputTextureHandles, 0)
        for (i in inputTextureHandles.indices) inputTextureHandles[i] = -1
    }

    override fun onDrawArraysAfter() {
        var i = 0
        while (i < inputTextureHandles.size
            && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE
        ) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3))
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            i++
        }
    }

    override fun onDrawArraysPre() {
        var i = 0
        while (i < inputTextureHandles.size
            && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE
        ) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3))
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i])
            GLES20.glUniform1i(inputTextureUniformLocations[i], i + 3)
            i++
        }
    }

    override fun onInit() {
        super.onInit()
        for (i in inputTextureUniformLocations.indices) inputTextureUniformLocations[i] =
            GLES20.glGetUniformLocation(program, "inputImageTexture" + (2 + i))
        mGLStrengthLocation = GLES20.glGetUniformLocation(
            program,
            "strength"
        )
    }

    override fun onInitialized() {
        super.onInitialized()
        setFloat(mGLStrengthLocation, 1.0f)
        runOnDraw {
            inputTextureHandles[0] =
                loadTexture(getApplicationContext(), "filter/earlybirdcurves.png")
            inputTextureHandles[1] =
                loadTexture(getApplicationContext(), "filter/earlybirdoverlaymap_new.png")
            inputTextureHandles[2] =
                loadTexture(getApplicationContext(), "filter/vignettemap_new.png")
            inputTextureHandles[3] =
                loadTexture(getApplicationContext(), "filter/earlybirdblowout.png")
            inputTextureHandles[4] = loadTexture(getApplicationContext(), "filter/earlybirdmap.png")
        }
    }
}