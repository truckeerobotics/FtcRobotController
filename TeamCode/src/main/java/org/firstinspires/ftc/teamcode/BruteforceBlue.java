package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Blue Side - Bruteforce")
public class BruteforceBlue extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();

    DcMotor motorFrontLeft = null;
    DcMotor motorBackLeft = null;
    DcMotor motorFrontRight = null;
    DcMotor motorBackRight = null;
    DcMotor spin = null;
    Servo armRotation = null;

    static final double     MULTI_EXTRA             = 1.6;    // eg: TETRIX Motor Encoder
    static final double     COUNTS_PER_MOTOR_REV    = 7;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 60.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415) * MULTI_EXTRA;
    static final double     DRIVE_SPEED             = 0.3;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        //Hardware map
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        spin = hardwareMap.dcMotor.get("spin");

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

    }

    public void spinSpinner(double seconds, boolean clockwise) {
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

            //  sleep(250);   // optional pause after each move
        }
    }
}
