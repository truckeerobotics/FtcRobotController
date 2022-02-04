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
        camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
    }

    // If camera had an error when trying to be opened.
    public void inCameraOpenErrorResult(int errorCode) {

    }
}

class ColorDensityPipeline extends OpenCvPipeline
{
    Telemetry telemetry;
    private int threshold = 0;

    public ColorDensityPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input)
    {
        Mat thresholdMat = new Mat();
        // Get the average color for green and then add on the threshold, for the min value.
        Scalar meanColor = Core.mean(input);
        Scalar min = new Scalar(255,meanColor.val[1]+threshold,255);
        Scalar max = new Scalar(255,255,255);
        // Do the thresholding.
        Core.inRange(input, min, max,thresholdMat);

        // Get size
        Size imageSize = thresholdMat.size();

        //Split the image into 3 parts
        Rect toCrop = new Rect(0, 0, (int)(imageSize.width/3), (int)imageSize.height);
        Mat cropped = new Mat(thresholdMat, toCrop);
        Size croppedSize = cropped.size();

        // print data
        telemetry.addData("Cropped Width: ", croppedSize.width);
        telemetry.addData("Cropped Height: ", croppedSize.height);
        telemetry.update();

        return thresholdMat;
    }
}