package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "Red Side #2 - Smart")
public class SmartRed2 extends LinearOpMode {

    /// CONSTANTS ///

    // Arm Constants
    private final double armHeightCountsPerInch = 140;
    private final double armDistanceCountsPerInch = 35;

    // Claw constant (s)
    private final double maxClawServo = 0.85;
    private final double minClawServo = 0.3;

    // Computer vision constants
    private final int cameraPixelWidth = 1920;
    private final int cameraPixelHeight = 1080;

    // Encoder constants
    static final double     MULTI_EXTRA             = 1.6;    //
    static final double     COUNTS_PER_MOTOR_REV    = 7;    //
    static final double     DRIVE_GEAR_REDUCTION    = 60.0 ;     //
    static final double     WHEEL_DIAMETER_INCHES   = 4;     //
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415) * MULTI_EXTRA;
    static final double     DRIVE_SPEED             = 0.3;
    static final double     TURN_SPEED              = 0.5;

    /// INSTANCE VARIABLES ///

    // Timer
    private ElapsedTime runtime = new ElapsedTime();

    // Motors and Hardware
    DcMotor motorFrontLeft = null;
    DcMotor motorBackLeft = null;
    DcMotor motorFrontRight = null;
    DcMotor motorBackRight = null;
    DcMotor motorDistance = null;
    DcMotor motorHeight = null;
    DcMotor spin = null;
    Servo clawLeft = null;
    Servo clawRight = null;

    // Computer vision
    private ColorDensityPipelineRED pipeline;
    private int level = 2;

    // Start Encoder Level

    private int heightStartEncoder = -1;
    private int distanceStartEncoder = -1;

    // Used for multi-threading
    private static boolean finishedScoring = false;
    private static boolean finishedDriving1 = false;

    @Override
    public void runOpMode() throws InterruptedException {
        /// IMPORT AND CONFIGURE ALL HARDWARE ///

        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorDistance = hardwareMap.dcMotor.get("distance");
        motorHeight = hardwareMap.dcMotor.get("height");
        spin = hardwareMap.dcMotor.get("spin");
        clawLeft = hardwareMap.servo.get("intakeLeft");
        clawRight = hardwareMap.servo.get("intakeRight");

        clawRight.setDirection(Servo.Direction.REVERSE);

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        /// SETUP COMPUTER VISION ///

        // Get the camera and configure it
        OpenCvCamera camera = getExternalCamera();

        // Create the pipeline and give it access to debugging
        pipeline = new ColorDensityPipelineRED(telemetry);

        // Give the camera the pipeline.
        camera.setPipeline(pipeline);

        // Open up the camera. Send to inCameraOpenSuccessResult or inCameraOpenErrorResult depending on if opening was successful
        // This is an Asynchronous function call, so when it is done opening it will call the function depending on result.
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override public void onOpened() { inCameraOpenSuccessResult(camera); }
            @Override public void onError(int errorCode) { inCameraOpenErrorResult(errorCode); }
        });

        /// WAIT FOR SUCCESSFUL COMPUTER VISION RESULT ///
        // Run until opMode starts
        outer:
        while (opModeIsActive() == false) {
            // Wait 5 seconds to check for result
            runtime.reset();
            while (runtime.seconds() < 5) {
                if (isStopRequested()) {
                    return;
                }
                // Check if pipeline was success, if so escape.
                if (pipeline.getLevel() != -1) {
                    level = pipeline.getLevel();
                    telemetry.addData("Pipeline Successful Level", level);
                    telemetry.addData("Status", "Breaking Loop Successfully");
                    telemetry.update();
                    break outer;
                }
            }
            // Tell driver team that no successful level results yet (Not optimal, but will still work if it fails)
            telemetry.addData("Passed 5 Seconds", "No Successful Level Results Yet");
            telemetry.update();
        }

        if (isStopRequested()) {
            return;
        }

        // Reset to default claw position
        moveClaws(false, 500);

        while (!opModeIsActive()){
            if (isStopRequested()) {
                return;
            }
        }
        waitForStart();

        /// RUN MOVEMENT STEPS ///

        // When it starts set "0" encoder levels
        heightStartEncoder = motorHeight.getCurrentPosition();
        distanceStartEncoder = motorDistance.getCurrentPosition();
        telemetry.addData("Zero Arm Encoder Recorded ", heightStartEncoder + " " + distanceStartEncoder);

        // Tell driver team what is going on :D
        telemetry.addData("Started Successfully with Level", level);

        telemetry.update();

        // Capture placed cube.
        moveClaws(true, 1500);
        setArm(1, 2,0);


        // Raise up arm
        double levelHeightSetter = 15.85; //15.85 top level (level 2)
        double levelDistanceSetter = 3.1; //3 top level (level 2)

        if (level == 0) {
            levelHeightSetter = 4;
            levelDistanceSetter = 1.7;
        } else if (level == 1) {
            levelHeightSetter = 10;
            levelDistanceSetter = 4;
        }

        final double levelHeight = levelHeightSetter; //15.85 top level (level 2)
        final double levelDistance = levelDistanceSetter; //3 top level (level 2)

        finishedScoring = false;
        finishedDriving1 = false;
        new Thread(() -> {
            setArm(1, levelHeight,2);
            while (!finishedDriving1) {

            }
            telemetry.addData("In", "in");
            telemetry.update();
            moveForward(0.1,6);
            setArm(1, levelHeight,levelDistance);
            moveClaws(false, 1000);
            finishedScoring = true;
        }).start();
        moveForward(0.1,4);
        rotate(0.1,50);
        moveForward(0.2,17);
        finishedDriving1 = true;

        runtime.reset();
        while(!finishedScoring && (runtime.seconds() < 11)) {

        }
        runtime.reset();

        // Move it back and prepare for next step
        setArm(1, levelHeight,0.5);
        moveForward(0.3,-8);
        setArm(1, 6,0.25);
        rotate(0.3, -120);
        moveForward(0.75, 50);
        setArm(1, 3,0);

        while(opModeIsActive()){
        }
        // Return rather than crash out if a stop is requested.
        if (isStopRequested()) {
            return;
        }

    }


    /// COLOR DENSITY SETUP HELPERS ///

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

    /// MOVEMENT API.
    // TO DO: Put in separate class

    public void moveClaws(boolean close, long delayAfterMilliSeconds) {

        if (!opModeIsActive()) {
            return;
        }

        double clawPosition = minClawServo;
        if (close) {
            clawPosition = maxClawServo;
        }
        clawRight.setPosition(clawPosition);
        clawLeft.setPosition(clawPosition);
        sleep(delayAfterMilliSeconds);
    }

    public void spinSpinner(double seconds, boolean clockwise) {
        if (!opModeIsActive()) {
            return;
        }
        runtime.reset();
        if (clockwise) {
            spin.setPower(1);
        } else {
            spin.setPower(-1);
        }
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < seconds)) {
            telemetry.addData("Spinning: ", "Spinning!");
            telemetry.update();
        }
        spin.setPower(0);
        runtime.reset();
    }

    public void moveForward(double speed, double inches){
        encoderDrive(speed, inches, inches, inches, inches);
    }

    public void strafeLeft(double speed, double inches){
        inches *= 1.36; // Strafing is slower than normal driving.
        encoderDrive(speed, inches * -1, inches, inches, inches * -1);
    }

    public void rotate(double speed, double degrees){
        degrees *= 0.1945; // Turn inches to degrees
        encoderDrive(speed, degrees * -1, degrees * -1, degrees, degrees);
    }

    public void moveArm(double speed, double inchesVertical, double inchesHorizontal){
        int encoderCountHeight = (int)(inchesVertical*armHeightCountsPerInch) + motorHeight.getCurrentPosition();;
        int encoderCountDistance = (int)(inchesHorizontal*armDistanceCountsPerInch) + motorDistance.getCurrentPosition();

        setArmEncoderPosition(speed,encoderCountHeight,encoderCountDistance);
    }

    // Sets arm position relative to the start
    public void setArm(double speed, double inchesVertical, double inchesHorizontal) {
        if (heightStartEncoder == -1) {
            sleep(10);
            waitForStart();
            sleep(10);
            heightStartEncoder = motorHeight.getCurrentPosition();
        }
        if (distanceStartEncoder == -1) {
            sleep(10);
            waitForStart();
            sleep(10);
            distanceStartEncoder = motorDistance.getCurrentPosition();
        }

        int encoderCountHeight = (int)(inchesVertical*armHeightCountsPerInch) + heightStartEncoder;
        int encoderCountDistance = (int)(inchesHorizontal*armDistanceCountsPerInch) + distanceStartEncoder;
        telemetry.addData("Height CURRENT: ", motorHeight.getCurrentPosition());
        telemetry.addData("Distance CURRENT: ", motorDistance.getCurrentPosition());

        telemetry.addData("Height: ", encoderCountHeight);
        telemetry.addData("Distance: ", encoderCountDistance);
        telemetry.update();

        setArmEncoderPosition(speed, encoderCountHeight, encoderCountDistance);
    }

    // Try to NOT use directly, this is more of a helper function.
    public void setArmEncoderPosition(double speed, int encoderPositionVertical, int encoderPositionHorizontal) {
        if (opModeIsActive()) {
            telemetry.addData("Vertical", encoderPositionVertical);
            telemetry.addData("Horizontal", encoderPositionHorizontal);
            telemetry.update();

            motorHeight.setTargetPosition(encoderPositionVertical);
            motorDistance.setTargetPosition(encoderPositionHorizontal);

            motorHeight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDistance.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            motorHeight.setPower(Math.abs(speed));
            motorDistance.setPower(Math.abs(speed)/2);

            while (opModeIsActive()  && (motorHeight.isBusy() || motorDistance.isBusy())) {
                telemetry.addData("Path1",  "Running to %7d :%7d");
                telemetry.addData("Path2",  "Running at %7d :%7d", motorHeight.getCurrentPosition(), motorDistance.getCurrentPosition());
                telemetry.update();
            }

            motorHeight.setPower(0);
            motorDistance.setPower(0);

            motorHeight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorDistance.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //telemetry.addData("",  );
            //telemetry.update();

            sleep(100);

        }
    }



    /// ENCODER API ///
    // TO DO: Put in separate class
    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    private void encoderDrive(double speed, double frontRightInches, double backRightInches, double frontLeftInches, double backLeftInches) {

        int newFrontLeftTarget;
        int newBackLeftTarget;
        int newFrontRightTarget;
        int newBackRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = motorFrontLeft.getCurrentPosition() + (int)(frontLeftInches * COUNTS_PER_INCH);
            newBackLeftTarget = motorBackLeft.getCurrentPosition() + (int)(backLeftInches * COUNTS_PER_INCH);
            newFrontRightTarget = motorFrontRight.getCurrentPosition() + (int)(frontRightInches * COUNTS_PER_INCH);
            newBackRightTarget = motorBackRight.getCurrentPosition() + (int)(backRightInches * COUNTS_PER_INCH);
            motorFrontLeft.setTargetPosition(newFrontLeftTarget);
            motorBackLeft.setTargetPosition(newBackLeftTarget);
            motorFrontRight.setTargetPosition(newFrontRightTarget);
            motorBackRight.setTargetPosition(newBackRightTarget);


            // Turn On RUN_TO_POSITION
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            motorFrontLeft.setPower(Math.abs(speed));
            motorBackLeft.setPower(Math.abs(speed));
            motorFrontRight.setPower(Math.abs(speed));
            motorBackRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive()  && (motorFrontLeft.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy() && motorBackRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d");
                telemetry.addData("Path2",  "Running at %7d :%7d", motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition(), motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition());
                telemetry.update();
                double distanceInInches = (Math.abs(newFrontLeftTarget - motorFrontLeft.getCurrentPosition() * (1/COUNTS_PER_INCH)));
                if (distanceInInches < 2) {
                    distanceInInches = distanceInInches*0.5;
                    if (distanceInInches < 0.25) {
                        distanceInInches = 0.1;
                    }
                    double newSpeed = Math.abs(speed*(1/distanceInInches));
                    motorFrontLeft.setPower(newSpeed);
                    motorBackLeft.setPower(newSpeed);
                    motorFrontRight.setPower(newSpeed);
                    motorBackRight.setPower(newSpeed);
                }
            }

            // Stop all motion;
            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackRight.setPower(0);

            // Turn off RUN_TO_POSITION
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            //sleep(2500);   // optional pause after each move
        }
    }
}
