package org.firstinspires.ftc.teamcode;

// importing many different libraries



import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


// the beginning of the teleop mode

@TeleOp(name = "The Pain Train", group = "Concept")

public class VisionAI extends LinearOpMode {

    final String pathTensorflowLiteModel = "test_model.tflite";
    TFlitePipeline pipeline;




    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Get the camera and configure it
        OpenCvCamera camera = getExternalCamera();

        // Create the Interpreter and throw an exception if anything goes wrong.
        Interpreter modelInterpreter = null;
        try {
            modelInterpreter = createTensorflowModelinterpreter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the pipeline and give it access to debugging and the model
        pipeline = new TFlitePipeline(telemetry, modelInterpreter);

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

    // Pure pain and suffering! :D
    // I'll try to get to adding comments to this someday.
    public Interpreter createTensorflowModelinterpreter() throws IOException {
        AssetManager assetManager = hardwareMap.appContext.getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd(pathTensorflowLiteModel);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        MappedByteBuffer modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        return new Interpreter(modelBuffer);
    }

}

// Changing so rapidly commenting would be pointless, and counterproductive.

class TFlitePipeline extends OpenCvPipeline
{

    Telemetry telemetry;
    Interpreter interpreter;

    public TFlitePipeline(Telemetry telemetry, Interpreter interpreter) {
        this.telemetry = telemetry;
        this.interpreter = interpreter;
    }


    @Override
    public Mat processFrame(Mat input)
    {
        return input;
    }


}


// Changing so rapidly commenting would be pointless, and counterproductive.

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
