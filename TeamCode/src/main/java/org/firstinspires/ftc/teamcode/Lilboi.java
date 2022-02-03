package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Lil Main Drive")
public class Lilboi extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //hardwaremap
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor spinMotor = hardwareMap.dcMotor.get("spin");
        DcMotor shoulderMotor = hardwareMap.dcMotor.get("shoulder");
        DcMotor elbowMotor = hardwareMap.dcMotor.get("elbow");


        //set motor direction
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
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
                shoulderPower = 0.5;
            }else if(gamepad1.dpad_down){
                shoulderPower = -0.5;
            }else{
                shoulderPower = 0;
            }

            if(gamepad1.dpad_right){
                elbowPower = 1;
            }else if(gamepad1.dpad_left){
                elbowPower = -1;
            }else{
                elbowPower = 0;
            }

            shoulderMotor.setPower(shoulderPower);
            elbowMotor.setPower(elbowPower);
            spinMotor.setPower(spin);
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

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
