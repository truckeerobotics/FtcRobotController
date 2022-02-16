package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

@TeleOp(name = "Lil Main Drive")
public class Lilboi extends LinearOpMode {

    private final double LateralSpeed = 0.6;
    private final double StrafeSpeed = 0.7;
    private final double RotationalSpeed = 0.6;


    //Define togglebind hashmap
    HashMap<String, Boolean> map=new HashMap<String, Boolean>();

    //onpush function
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

        double intakeLeftPower = 0.3;
        double intakeRightPower = 0.3;

        intakeRight.setPosition(intakeRightPower);
        intakeLeft.setPosition(intakeLeftPower);

        height.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        distance.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            double y = -gamepad1.left_stick_y * LateralSpeed; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * StrafeSpeed * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x * RotationalSpeed;

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

            distancePower = gamepad2.left_stick_y / 4;
            heightPower = gamepad2.left_stick_x * 1.1;

            //open
            if(onPush(gamepad2.x, "controller2ButtonX")){
                intakeLeftPower = 1;
                intakeRightPower = 1;
            }

            //closed
            if(onPush(gamepad2.b, "controller2ButtonB")) {
                intakeLeftPower = 0.3;
                intakeRightPower = 0.3;
            }

            intakeRight.setPosition(intakeRightPower);
            intakeLeft.setPosition(intakeLeftPower);

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
            telemetry.addData("LB:", gamepad1.left_bumper);
            telemetry.addData("RB:", gamepad1.right_bumper);

            telemetry.update();
        }
    }
}
