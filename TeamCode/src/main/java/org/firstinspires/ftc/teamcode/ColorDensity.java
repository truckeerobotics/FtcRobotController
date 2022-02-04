package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.tensorflow.lite.Interpreter;

@Autonomous(name = "Color Density", group = "Concept")

public class ColorDensity extends LinearOpMode {

    int level = 0;
    ColorDensityPipeline pipeline;

    @Override
    public void runOpMode() {
        // Get the camera and configure it
        OpenCvCamera camera = getExternalCamera();

        // Create the pipeline and give it access to debugging
        pipeline = new ColorDensityPipeline(telemetry);

        // Give the camera the pipeline.
        camera.setPipeline(pipeline);

        // Open up the camera. Send to inCameraOpenSuccessResult or inCameraOpenErrorResult depending on if opening was successful
        // This is an Asynchronous function call, so when it is done opening it will call the function depending on result.
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override public void onOpened() { inCameraOpenSuccessResult(camera); }
            @Override public void onError(int errorCode) { inCameraOpenErrorResult(errorCode); }
        });

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

            }
        }
    }

    // Get the webcam (External camera)
    public OpenCvCamera getExternalCamera() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        return OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
    }

    // If the camera was opened up, then start streaming.
    public void inCameraOpenSuccessResult(OpenCvCamera camera) {
        camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
    }

    // If camera had an error when trying to be opened.
    public void inCameraOpenErrorResult(int errorCode) {

    }
}

class ColorDensityPipeline extends OpenCvPipeline
{
    Telemetry telemetry;
    private int threshold = 30;

    public ColorDensityPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input)
    {

        Mat thresholdMat = new Mat();
        // Get the average color for green and then add on the threshold, for the min value.
        Scalar meanColor = Core.mean(input);
        Scalar min = new Scalar(0,meanColor.val[1]+threshold,0,0);
        Scalar max = new Scalar(150,255,150,255);
        // Do the thresholding.
        Core.inRange(input, min, max,thresholdMat);

        // Get size
        Size imageSize = thresholdMat.size();

        //Split the image into 3 parts, and get their brightnesses.
        // This could be improved by doing a for loop.
        Rect leftRectCrop = new Rect(0, 0, (int)(imageSize.width/3), (int)imageSize.height);
        Mat leftCropped = new Mat(thresholdMat, leftRectCrop);
        double brightnessLeft = Core.mean(leftCropped).val[0];

        Rect middleRectCrop = new Rect((int)(imageSize.width/3), 0, (int)(imageSize.width/3), (int)imageSize.height);
        Mat middleCropped = new Mat(thresholdMat, middleRectCrop);
        double brightnessMiddle = Core.mean(middleCropped).val[0];

        Rect rightRectCrop = new Rect((int)(imageSize.width/3)*2, 0, (int)(imageSize.width/3), (int)imageSize.height);
        Mat rightCropped = new Mat(thresholdMat, rightRectCrop);
        double brightnessRight = Core.mean(rightCropped).val[0];

        // Get which one is brightest
        if (brightnessLeft > brightnessMiddle && brightnessLeft > brightnessRight) {
            telemetry.addData("RESULT: ", "LEFT");
        } else if (brightnessMiddle > brightnessRight) {
            telemetry.addData("RESULT: ", "MIDDLE");
        } else {
            telemetry.addData("RESULT: ", "RIGHT");
        }

        // print data
//        telemetry.addData("Max Color: ", max);
//        telemetry.addData("Min Color: ", min);
//        telemetry.addData("Mean Color: ", meanColor);
//        telemetry.addData("Bright Right: ", brightnessRight);
//        telemetry.addData("Bright Middle: ", brightnessMiddle);
//        telemetry.addData("Bright Left: ", brightnessLeft);

        telemetry.update();

        return thresholdMat;
    }
}