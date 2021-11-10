package org.firstinspires.ftc.teamcode;

// importing many different libraries

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

// the beginning of the teleop mode

@TeleOp(name = "AI vision", group = "Concept")

public class VisionAI extends LinearOpMode {

    ThresholdPipeline pipeline = new ThresholdPipeline();

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Get the camera and configure it
        OpenCvCamera camera = getExternalCamera();
        //camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
        pipeline.TelemetryPipeline(telemetry);
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


// Pipe :)
class ThresholdPipeline extends OpenCvPipeline
{
    public Scalar lower = new Scalar(0, 0, 0);
    public Scalar upper = new Scalar(255, 255, 100);

    private Mat ycrcbMat = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();

    Telemetry telemetry;

    public void TelemetryPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input)
    {

        // Set the "color space" of the Mat (image)
        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);

        // Go through and get if in range (Color wise). All colors out of range will be 0, all in-range will be 1.
        // Results in a Mat with 1's and 0's (Binary)
        Core.inRange(ycrcbMat, lower, upper, binaryMat);

        // Release (Remove) the previous Mat.
        maskedInputMat.release();

        // Make all the pixels that are 0's black (On the binary Mat), and no change for all that are 1's (On the binary Mat)
        Core.bitwise_and(input, input, maskedInputMat, binaryMat);

        // Some debug.
        telemetry.addData("[processFrame]", "processed");
        telemetry.addData("[Lower Scalar]", lower);
        telemetry.addData("[Upper Scalar]", upper);
        telemetry.update();

        return maskedInputMat;
    }


}