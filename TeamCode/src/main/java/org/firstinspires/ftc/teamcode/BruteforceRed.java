package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Red Side - Bruteforce")

public class BruteforceRed extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    static final double FORWARD_SPEED = -0.5;
    static final double TURN_SPEED    = 0.4;

    DcMotor motorFrontLeft = null;
    DcMotor motorBackLeft = null;
    DcMotor motorFrontRight = null;
    DcMotor motorBackRight = null;
    DcMotor spin = null;

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

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spin.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("test", "test", runtime.seconds());


        waitForStart();

        if (isStopRequested()) return;

        // Step 1:  Drive backwords for 0.3 seconds
        calcPower(-FORWARD_SPEED, 0, 0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.3)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Strafe for 2.5 seconds
        calcPower(0, FORWARD_SPEED, 0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        calcPower(0, 0, 0);


        spin.setPower(1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        spin.setPower(0);

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        calcPower(-FORWARD_SPEED, 0, 0);

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        calcPower(0, 0, 0);
    }
}
