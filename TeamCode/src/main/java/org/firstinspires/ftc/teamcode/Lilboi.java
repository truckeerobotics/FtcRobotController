package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

@TeleOp(name = "Lil Main Drive")
public class Lilboi extends LinearOpMode {

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

        // default power
        int intake = 0;

        //hardwaremap
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor spinMotor = hardwareMap.dcMotor.get("spin");
        DcMotor shoulderMotor = hardwareMap.dcMotor.get("shoulder");
        DcMotor elbowMotor = hardwareMap.dcMotor.get("elbow");
        Servo intakeLeft = hardwareMap.servo.get("intakeLeft");
        Servo intakeRight = hardwareMap.servo.get("intakeRight");

        intakeRight.setDirection(Servo.Direction.REVERSE);

        //set motor direction
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Setup shoulder
        shoulderMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            
            //save gamepad stick positions to variables
            double y = gamepad1.left_stick_y;
            double x = gamepad1.right_stick_x * 1; // 1.1 counters imperfect strafing
            double rx = -gamepad1.left_stick_x;

            //Wheel power calculations
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x - rx) / denominator;
            double backLeftPower = (y - x - rx) / denominator;
            double frontRightPower = (y - x + rx) / denominator;
            double backRightPower = (y + x + rx) / denominator;
            double spin = 0;
            double shoulderPower = 0;
            double elbowPower = 0;

            //carousel spinner
            if(gamepad1.left_bumper){
                spin = 1;
            }else if(gamepad1.right_bumper){
                spin = -1;
            }else{
                spin = 0;
            }

            if(gamepad1.dpad_up){
                shoulderPower = 0.25;
            }else if(gamepad1.dpad_down){
                shoulderPower = -0.25;
            }else{
                shoulderPower = 0;
            }

            if(gamepad1.dpad_right){
                elbowPower = 0.5;
            }else if(gamepad1.dpad_left){
                elbowPower = -0.5;
            }else {
                elbowPower = 0;
            }

            //toggle intake
            if(onPush(gamepad2.x, "controller2ButtonX")) {
                if (intake != 1) {
                    intake = 1;
                } else {
                    intake = 0;
                }
            }

            //toggle intake
            if(onPush(gamepad2.b, "controller2ButtonB")) {
                if (intake != -1) {
                    intake = -1;
                } else {
                    intake = 0;
                }
            }
            intakeRight.setPosition(intake);
            intakeLeft.setPosition(intake);

            shoulderMotor.setPower(shoulderPower);
            elbowMotor.setPower(elbowPower);
            spinMotor.setPower(spin);
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            telemetry.addData("Debug: ", shoulderMotor.getCurrentPosition());
            telemetry.addData("frontLeft: ", frontLeftPower);
            telemetry.addData("frontRight: ", frontRightPower);
            telemetry.addData("backLeft: ", backLeftPower);
            telemetry.addData("backRight: ", backRightPower);
            telemetry.addData("LB:", gamepad1.left_bumper);
            telemetry.addData("RB:", gamepad1.right_bumper);

            telemetry.update();
        }
    }
}
