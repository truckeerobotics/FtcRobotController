package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Meg", group="Linear Opmode")

public class Meg extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private Servo servo1 = null;
    private Servo servo2 = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        //servo1 = hardwareMap.get(Servo.class, "servo1");
        //servo2 = hardwareMap.get(Servo.class, "servo2");


        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        //servo1.setDirection(Servo.Direction.FORWARD);
        //servo2.setDirection(Servo.Direction.FORWARD);


        waitForStart();
        runtime.reset();

        double servoTurn1 = 0;
        double servoTurn2 = 0.5;

        while (opModeIsActive()) {

            double leftPower;
            double rightPower;

            double drive = gamepad1.left_stick_y;
            double turn  = -gamepad1.left_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            if(gamepad1.a){
                servoTurn1 = 0.5;
                servoTurn2 = 1;
            }else if(!gamepad1.a){
                servoTurn1 = 0;
                servoTurn2 = 0.5;
            }
            //servo1.setPosition(servoTurn1);
            //servo2.setPosition(servoTurn2);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Status", "Right Gamepad X: " + gamepad1.right_stick_x + " A: " + gamepad1.a);
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
