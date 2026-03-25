package com.TEST.utils;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class screenRecorderUtility {

    private FFmpegFrameRecorder recorder;
    private OpenCVFrameGrabber grabber;
    private String outputPath;

    public void startRecording(String testName) {
        try {
            outputPath = getRecordingFileName(testName);
            setupGrabber();
            setupRecorder();
            startFrameCapture();
            System.out.println("Recording started for " + testName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(boolean testPassed) {
        try {
            stopAndReleaseResources();
            manageRecordingFile(testPassed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRecordingFileName(String testName) {
        // Todo 1: use datetime in the title (testName + testType + todayDateTime)
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(dtf);
        return "recordings/" + testName + "_" + timestamp + ".mp4";
    }

    private void setupGrabber() throws Exception {
        // Todo 1: grab the primary display (Index 0)
        grabber = new OpenCVFrameGrabber(0);
        grabber.start();
    }

    private void setupRecorder() throws Exception {
        // Todo 1: define frameRate, codec and then start
        recorder = new FFmpegFrameRecorder(outputPath, grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFrameRate(20);
        recorder.start();
    }

    private void startFrameCapture() {
        // Todo 1: use thread to capture frames
        new Thread(() -> {
            try {
                while (grabber.grab() != null) {
                    Frame frame = grabber.grab();
                    if (frame != null) {
                        recorder.record(frame);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void stopAndReleaseResources() throws Exception {
        // Todo 1: stop recording, grabber and release resources
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        if (grabber != null) {
            grabber.stop();
            grabber.release();
            grabber = null;
        }
        System.out.println("Recording stopped.");
    }

    private void manageRecordingFile(boolean testPassed) {
        // Todo 1: delete file if passed, keep if failed
        if (testPassed) {
            deleteRecording();
        } else {
            System.out.println("Recording saved at: " + outputPath);
        }
    }

    private void deleteRecording() {
        // delete
        File file = new File(outputPath);
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                System.out.println("Recording deleted (test passed).");
            } else {
                System.out.println("Couldn't delete the file.");
            }
        }
    }
}
