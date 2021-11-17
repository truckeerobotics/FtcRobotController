package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "robot")
public class Bigboi extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        //Hardware map
        double timePass = 0;
        int intake = 0;
        int spin = 0;
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        DcMotor intakeRight = hardwareMap.dcMotor.get("intakeRight");
        DcMotor spinMotor = hardwareMap.dcMotor.get("spin");
        DcMotor armMotor = hardwareMap.dcMotor.get("armMotor");
        //Servo servo = hardwareMap.servo.get("servo");



        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spinMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            //servo.setPosition(0);

            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x * 1.1; // 1.1 counters imperfect strafing
            double rx = gamepad1.right_stick_x;
            
            //Wheel power calculations
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x - rx) / denominator;
            double backLeftPower = (y - x - rx) / denominator;
            double frontRightPower = (y - x + rx) / denominator;
            double backRightPower = (y + x + rx) / denominator;

            if(gamepad1.x){
                intake = 1;
            } else if(gamepad1.b){
                intake = -1;
            } else{
                intake = 0;
            }
            
            if(gamepad1.left_bumper){
                spin = 1;
            }else if(gamepad1.right_bumper){
                spin = -1;
            }else{
                spin = 0;
            }

            if(gamepad1.dpad_up){
                armMotor.setPower(1);
            }
            if(gamepad1.dpad_down){
                armMotor.setPower(-0.5);
            }
            if (!gamepad1.dpad_down && gamepad1.dpad_up){
                armMotor.setPower(0);
            }



            //setting power
            intakeRight.setPower(intake);
            intakeLeft.setPower(intake);
            spinMotor.setPower(spin);
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            
            //control debugs
            telemetry.addData("frontLeft: ", frontLeftPower);
            telemetry.addData("frontRight: ", frontRightPower);
            telemetry.addData("backLeft: ", backLeftPower);
            telemetry.addData("backRight: ", backRightPower);
            telemetry.addData("X: ", gamepad1.x);
            telemetry.addData("B: ", gamepad1.b);
            telemetry.addData("LB:", gamepad1.left_bumper);
            telemetry.addData("RB:", gamepad1.right_bumper);
            telemetry.addData("frontLeftPosition", motorFrontLeft.getCurrentPosition());
            telemetry.addData("frontRightPosition", motorFrontRight.getCurrentPosition());

            telemetry.update();
        }
    }
}
