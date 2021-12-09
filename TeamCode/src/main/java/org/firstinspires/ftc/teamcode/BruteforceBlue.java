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

    static final double FORWARD_SPEED = -0.5;
    static final double TURN_SPEED    = 0.4;

    DcMotor motorFrontLeft = null;
    DcMotor motorBackLeft = null;
    DcMotor motorFrontRight = null;
    DcMotor motorBackRight = null;
    DcMotor spin = null;
    Servo armRotation = null;

    public void calcPower(double y, double x, double rx){
        //Wheel power calculations
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        motorFrontLeft.setPower((y + x - rx) / denominator);
        motorBackLeft.setPower((y - x - rx) / denominator);
        motorFrontRight.setPower((y - x + rx) / denominator);
        motorBackRight.setPower((y + x + rx) / denominator);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //Hardware map
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        spin = hardwareMap.dcMotor.get("spin");
        armRotation = hardwareMap.servo.get("armRotation");


        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spin.setDirection(DcMotorSimple.Direction.REVERSE);

        armRotation.setPosition(0.7);

        waitForStart();

        if (isStopRequested()) return;

        // Step 1:  Strafe for 0.5 seconds
        //WARNING: could stafe in wrong direction change forward_speed to a negitive if it does
        calcPower(0, -FORWARD_SPEED, 0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        // Step 2:  Drive forwards for 2.5 seconds
        //WARNING: may go backwords. if it does change forward_speed to -forward_speed
        calcPower(FORWARD_SPEED, 0, 0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.5)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        //stop motors
        calcPower(0, 0, 0);


        //spin wheel
        //setpower is -1 so wheel moves in other direction
        spin.setPower(-1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        spin.setPower(0);
            
        //0.5 second delay with no motors on
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        
        //stafe for 1.5 seconds. (maybe get half inside box)
        //WARNING: could stafe in wrong direction change forward_speed to a negitive if it does
        calcPower(0, -FORWARD_SPEED, 0);

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.8)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        calcPower(0, 0, 0);
    }
}
