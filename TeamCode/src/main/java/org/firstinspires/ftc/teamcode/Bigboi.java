package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "robot")
public class Bigboi extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        int intake = 0;
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        DcMotor intakeRight = hardwareMap.dcMotor.get("intakeRight");
        DcMotor spin = hardwareMap.dcMotor.get("spin");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spin.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            telemetry.addData("frontLeft: ", frontLeftPower);
            telemetry.addData("frontRight: ", frontRightPower);
            telemetry.addData("backLeft: ", backLeftPower);
            telemetry.addData("backRight: ", backRightPower);
            telemetry.addData("A: ", gamepad1.a);
            telemetry.addData("LB:", gamepad1.left_bumper);
            telemetry.addData("RB:", gamepad1.right_bumper);
            telemetry.update();


            if(gamepad1.x){
                intake = 0;
            }
            if(gamepad1.y){
                intake = 1;
            }
            if(gamepad1.b){
                intake = -1;
            }

            intakeRight.setPower(intake);
            intakeLeft.setPower(intake);


            if(gamepad1.left_bumper){
                spin.setPower(1);
            }else if(gamepad1.right_bumper){
                spin.setPower(-1);
            }else{
                spin.setPower(0);
            }

            // if(gamepad1.a){
            //    spin.setPower(1);
            // }else{
            //    spin.setPower(0);
            // }
        }
    }
}