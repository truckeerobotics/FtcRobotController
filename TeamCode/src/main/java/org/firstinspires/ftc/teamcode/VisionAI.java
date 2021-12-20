package org.firstinspires.ftc.teamcode;

// importing many different libraries

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.imgproc.Imgproc.INTER_AREA;
import static org.opencv.imgproc.Imgproc.resize;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Size;
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


@Autonomous(name = "The Pain Train (WIP)", group = "Concept")

public class VisionAI extends LinearOpMode {

    final String pathTensorflowLiteModel = "duck_level_model.tflite";
    int level = 0;
    TFlitePipeline pipeline;

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");


        // Get the camera and configure it
        OpenCvCamera camera = getExternalCamera();

        // Create the Interpreter and throw an exception if anything goes wrong.
        Interpreter modelInterpreter = null;
        try {
            modelInterpreter = createTensorflowModelInterpreter(telemetry);
        } catch (IOException e) {
            telemetry.addData("Error", "Failed creating Interpreter, see LogCat");
            e.printStackTrace();
        }

        telemetry.update();

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

    /// Pain:

    // Get a model interpreter using the path of the model file.
    public Interpreter createTensorflowModelInterpreter(Telemetry telemetry) throws IOException {
        // Get an asset manager by using the context of the app (Basically android system integration), and then getting the assets.
        AssetManager assetManager = hardwareMap.appContext.getAssets();
        // Gets an AssetFileDescriptor, which is essentially our way to get the place in memory where the file of that path is.
        AssetFileDescriptor fileDescriptor = assetManager.openFd(pathTensorflowLiteModel);
        // This gets the raw bytes of the file, and puts it into this File stream.
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        // This is a file channel, which we can then read, write and map from.
        FileChannel fileChannel = inputStream.getChannel();
        // Put the position of where in memory the file is, in longs (Longs are extremely big integers [64 bit integers])
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        // This is a map of the file, which essentially allows you to access it's memory directly.
        MappedByteBuffer modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        // Create an Interpreter using this MappedByteBuffer, this is what we send out of the function, and what can be used for running the model.
        // noinspection deprecation - For whatever reason android studio thinks Interpreter is depricated, so just ignore that using this comment.
        Interpreter interpreter = new Interpreter(modelBuffer);
        return interpreter;
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
        Mat resizedimage = new Mat();
        Size scaleSize = new Size(1280,853);
        resize(input, resizedimage, scaleSize , 0, 0, INTER_AREA);

        float[] inputBuffer = matToFloatArray(resizedimage);
        float[][][][] inputArray = expandFloatArrayToOutput(inputBuffer, resizedimage.height(), resizedimage.width(), 3);
        // Output
        float[][] outputArray = new float[1][3];


        interpreter.run(inputArray, outputArray);

        telemetry.addData("Output1: ", outputArray[0][0]);
        telemetry.addData("Output2: ", outputArray[0][1]);
        telemetry.addData("Output3: ", outputArray[0][2]);

        telemetry.update();

        return resizedimage;
    }

    public float[] matToFloatArray(Mat doubleMat) {
        Mat floatMat = new Mat();
        doubleMat.convertTo(floatMat, CV_32F);
        float[] buffer = new float[(int)floatMat.total() * floatMat.channels()];
        floatMat.get(0,0,buffer);
        return buffer;
    }

    public float[][][][] expandFloatArrayToOutput(float[] toExpand, int height, int width, int colorCount) {
        float[][][][] expandedFloatArray = new float [1][width][height][colorCount];
        int index = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int color = 0; color < 3; color++) {
                    expandedFloatArray[0][x][y][color] = toExpand[index];
                    index++;
                }
            }
        }
        return expandedFloatArray;

    }

}
