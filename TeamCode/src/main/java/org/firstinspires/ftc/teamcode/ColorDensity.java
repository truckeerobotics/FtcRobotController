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

import java.util.ArrayList;
import java.util.Arrays;

@Autonomous(name = "Color Density", group = "Concept")

public class ColorDensity extends LinearOpMode {

    // Constants
    private final int cameraPixelWidth = 1920;
    private final int cameraPixelHeight = 1080;

    // Instance Variables
    private int level = 0;
    private ColorDensityPipeline pipeline;

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

        // Main Loop
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
        camera.startStreaming(cameraPixelWidth, cameraPixelHeight, OpenCvCameraRotation.UPRIGHT);
    }

    // If camera had an error when trying to be opened.
    public void inCameraOpenErrorResult(int errorCode) {
        System.out.println("Error occurred, check logcat if possible. The error code is " + errorCode);
    }
}

class ColorDensityPipeline extends OpenCvPipeline
{
    // Constants
    private final int threshold = 30;
    private final int levelCount = 3;
    private final double thresholdConfirm = 0.7;
    // Instance Variables
    private Telemetry telemetry;
    private int levelResult = -1;


    public ColorDensityPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input)
    {
        // Debug
        System.out.println("Entering Pipeline");

        if (levelResult != -1) {
            return input;
        }

        // Create a mat to put our threshold image into.
        Mat thresholdMat = new Mat();
        // Get the average color for green and then add on the green threshold.
        Scalar meanColor = Core.mean(input);
        Scalar min = new Scalar(0,meanColor.val[1]+threshold,0,0);
        Scalar max = new Scalar(150,255,150,255);
        // Do the thresholding and put the result in the thresholdMat.
        Core.inRange(input, min, max,thresholdMat);

        // Get image size for splitting the image later.
        Size imageSize = thresholdMat.size();

        double brightestValue = 0;
        int brightestLevel = 2;
        double sum = 0;

        for (int level = 0; level < levelCount; level++) {
            Rect rectCrop = new Rect((int)(imageSize.width/levelCount) * level, 0, (int)(imageSize.width/levelCount), (int)imageSize.height);
            Mat matCropped = new Mat(thresholdMat, rectCrop);
            double brightness = Core.mean(matCropped).val[0];

            sum += brightness;
            if (brightness > brightestValue) {
                brightestValue = brightness;
                brightestLevel = level;
            }
        }

        // Check brightest level if good enough to confirm successful result.
        if (sum * thresholdConfirm < brightestValue) {
            telemetry.addData("SUCCESSFUL, level result: : ", brightestLevel);
            levelResult = brightestLevel;
        } else {
            telemetry.addData("FAILED, with sum:", sum);
            telemetry.addData("FAILED, with brightest value of:", brightestValue);
            telemetry.addData("FAILED, brightest level: ", brightestLevel);
        }
        telemetry.update();

        System.out.println("Exiting Pipeline");
        return thresholdMat;
    }
}