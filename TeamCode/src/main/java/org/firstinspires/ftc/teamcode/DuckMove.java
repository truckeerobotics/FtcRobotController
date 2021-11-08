package org.firstinspires.ftc.teamcode;

// importing many different libraries

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

// the beginning of the teleop mode

@TeleOp(name = "Duck ai", group = "Concept")

// defining objects that the camera can see

public class DuckMove extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };
    private static final String VUFORIA_KEY = "AXCvFLP/////AAABmXXWm/vnYEsllGHFF6t9dmInylLJVEPCMoTUn+I6rmvMFcuvsUriaQiua2uPh2HJ3qq/+IkIJ7MpywB2KKVUNwspEQUKmw2FLP2FGbZdgI0jWb6H+gaeQz8hV6Y+c/8tCPcrSIrHBtdoicjDnPEO1CwcDhaYxVAoCQcG5aFE1l7IX+nDt7l1JZRK7dZGuzxnXsQX3A4nyrlNXJL3MVw0ZZuwMZCXYMiMmD3d13bH2CI5/HDkDnYflUeLlFde24grmCXScbZvp7ZXZeqXUP/02ioH1f3RVWh7KFKISUEsoPAOPO28coXKHKCJoMqD2rf8P1LsciMagoeTAO0nvthGQ/Az79Px1GN71KsHykobOWBW";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        initVuforia();
        initTfod();


        if (tfod != null) {
            tfod.activate();

            tfod.setZoom(2.5, 16.0/9.0);
        }

        waitForStart();

        int frame = 0;

        if (opModeIsActive()) {
            while (opModeIsActive()) {


                if (tfod != null) {
                    telemetry.addData("frame: ", frame);
                    frame++;

                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int i = 0;


                        for (Recognition recognition : updatedRecognitions) {
                            if(recognition.getLeft() > 500){
                                //left?
                            }else if(recognition.getLeft() < 200){
                                //right?
                            }else{
                                //forward?
                            }
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f", recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f", recognition.getRight(), recognition.getBottom());

                            i++;
                        }



                        telemetry.update();
                    }
                }
            }
        }
    }

    private void initVuforia() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}