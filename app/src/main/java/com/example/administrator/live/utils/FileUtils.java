package com.example.administrator.live.utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * description:
 * Created by aserbao on 2018/5/14.
 */


public class FileUtils {

    private  static final String TAG = "FileUtils";
    private static String fileName = null;
    static MediaRecorder mMediaRecorder;

    public static String getStorageMp4(Object s){
        File file;
        String parent = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies";
        File file1 = new File(parent);
        if (!file1.exists()) {
            file1.mkdir();
        }
        file = new File(parent, s + ".mp4");

        return file.getPath();
    }

    public static void startRecording(Surface surface) {
        fileName = getOutputMediaFile().toString();
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setOutputFile(fileName);
        mMediaRecorder.setPreviewDisplay(surface);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            Timber.e("prepare() failed");
        }
        mMediaRecorder.start();
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MyVideos");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
    }

    public static String stopRecording() {
        if (null != mMediaRecorder) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            return fileName;
        }
        return null;
    }
}
