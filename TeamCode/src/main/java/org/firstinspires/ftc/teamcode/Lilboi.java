package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

@TeleOp(name = "Lil Main Drive")
public class Lilboi extends LinearOpMode {

    // Constants for controlling the robot
    // Driving
    private final double LateralSpeed = 0.6;
    private final double StrafeSpeed = 0.7;
    private final double RotationalSpeed = 0.6;

    // Grabber/intake
    private final double maxIntake = 0.85;
    private final double minIntake = 0.3;
    private final double manualIntakeSpeed = 0.01;

    // Smart driving
    private final double drivingPowerForwardDelta = 0.02;
    private final double drivingPowerBackwardDelta = 0.0175;
    private final double drivingPowerStrafeDelta = 0.02;
    private final double drivingPowerRotationDelta = 0.04;

    private final double drivingPowerDifferenceCutoff = 0.1;

    // Control variables used for making driving *smooth*.
    private double yTarget = 0;
    private double xTarget = 0;
    private double rxTarget = 0;

    private double yCurrent = 0;
    private double xCurrent = 0;
    private double rxCurrent = 0;

    // Used for on-push to keep track of pushed buttons.
    HashMap<String, Boolean> map=new HashMap<String, Boolean>();

    // Helper function with a hashmap to do things when a button is pressed rather than held.
    boolean onPush(boolean button, String buttonName){
        //loop though toggle hashmap
        for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
            //save the key and value to varibles
            String key = entry.getKey();
            Boolean value = entry.getValue();
            //check if the key is the value passed into the function
            if(key == buttonName){
                //if it is and the last tick was a diffrent value we set it to true, thus toggleing the boolean
                if(button && !value){
                    map.put(key, button);
                    return true;
                }else{
                    map.put(key, button);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void runOpMode() throws InterruptedException {

        //Control toggle setup
        map.put("controller1ButtonX", false);
        map.put("controller2ButtonX", false);
        map.put("controller1ButtonB", false);
        map.put("controller2ButtonB", false);

        //hardware map
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor spinMotor = hardwareMap.dcMotor.get("spin");
        DcMotor distance = hardwareMap.dcMotor.get("distance");
        DcMotor height = hardwareMap.dcMotor.get("height");

        Servo intakeLeft = hardwareMap.servo.get("intakeLeft");
        Servo intakeRight = hardwareMap.servo.get("intakeRight");

        intakeRight.setDirection(Servo.Direction.REVERSE);

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        distance.setDirection(DcMotor.Direction.REVERSE);

        double intakePower = minIntake;

        intakeRight.setPosition(intakePower);
        intakeLeft.setPosition(intakePower);

        height.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        distance.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // Smart/Smooth driving logic

            yTarget = -gamepad1.left_stick_y * LateralSpeed; // Remember, this is reversed!
            xTarget = -gamepad1.left_stick_x * StrafeSpeed * 1.1; // Counteract imperfect strafing
            rxTarget = -gamepad1.right_stick_x * RotationalSpeed;

            // If difference cutoff is not reached, then move current/actual movement to target movement.
            if (!(Math.abs(yTarget - yCurrent) < drivingPowerDifferenceCutoff)) {
                if (yTarget > yCurrent) {
                    yCurrent += drivingPowerForwardDelta;
                } else {
                    yCurrent -= drivingPowerBackwardDelta;
                }
            } else {
                yCurrent = yTarget;
            }

            if (!(Math.abs(xTarget - xCurrent) < drivingPowerDifferenceCutoff)) {
                if (xTarget > xCurrent) {
                    xCurrent += drivingPowerStrafeDelta;
                } else {
                    xCurrent -= drivingPowerStrafeDelta;
                }
            } else {
                xCurrent = xTarget;
            }

            if (!(Math.abs(rxTarget - rxCurrent) < drivingPowerDifferenceCutoff)) {
                if (rxTarget > rxCurrent) {
                    rxCurrent += drivingPowerRotationDelta;
                } else {
                    rxCurrent -= drivingPowerRotationDelta;
                }
            } else {
                rxCurrent = rxTarget;
            }

            // Debug
            telemetry.addData("xTarget: ", xTarget);
            telemetry.addData("yTarget: ", yTarget);
            telemetry.addData("rxTarget: ", rxTarget);

            telemetry.addData("xCurrent: ", xCurrent);
            telemetry.addData("yCurrent: ", yCurrent);
            telemetry.addData("rxCurrent: ", rxCurrent);

            // Take things from smart/smooth driving and implement the intended axial changes.

            double y = yCurrent;
            double x = xCurrent;
            double rx = rxCurrent;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            double spin = 0;
            double distancePower = 0;
            double heightPower = 0;

            //carousel spinner
            if(gamepad1.left_bumper){
                spin = 1;
            }else if(gamepad1.right_bumper){
                spin = -1;
            }else{
                spin = 0;
            }

            distancePower = gamepad2.left_stick_y / 3;
            heightPower = gamepad2.right_stick_y * -1.1;

            // Open intake on push of X on controller 2
            if(onPush(gamepad2.x, "controller2ButtonX")){
                intakePower = maxIntake;
            }

            // Close intake on push of B on controller 2
            if(onPush(gamepad2.b, "controller2ButtonB")) {
                intakePower = minIntake;
            }

            if (gamepad2.a) {
                if (intakePower > minIntake) {
                    intakePower -= manualIntakeSpeed;
                }
            }
            if (gamepad2.y) {
                if (intakePower < maxIntake) {
                    intakePower += manualIntakeSpeed;
                }
            }


            intakeRight.setPosition(intakePower);
            intakeLeft.setPosition(intakePower);

            spinMotor.setPower(spin);
            distance.setPower(distancePower);
            height.setPower(heightPower);
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            telemetry.addData("frontLeft: ", frontLeftPower);
            telemetry.addData("frontRight: ", frontRightPower);
            telemetry.addData("backLeft: ", backLeftPower);
            telemetry.addData("backRight: ", backRightPower);
            telemetry.addData("distanceArm: ", distancePower);
            telemetry.addData("heightArm: ", heightPower);
            telemetry.addData("Arm Encoders: ", "distance " + distance.getCurrentPosition() + " height " + height.getCurrentPosition());
            telemetry.addData("LB:", gamepad1.left_bumper);
            telemetry.addData("RB:", gamepad1.right_bumper);

            telemetry.update();
        }
    }
}
