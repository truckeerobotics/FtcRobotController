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

    public double[] calcPower(double y, double x, double rx){
        //Wheel power calculations
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x - rx) / denominator;
        double backLeftPower = (y - x - rx) / denominator;
        double frontRightPower = (y - x + rx) / denominator;
        double backRightPower = (y + x + rx) / denominator;
        double[] powers={frontLeftPower,backLeftPower,frontRightPower,backRightPower};
        return powers;
    }




    @Override
    public void runOpMode() throws InterruptedException {
        //Hardware map
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor spin = hardwareMap.dcMotor.get("spin");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spin.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("test", "test", runtime.seconds());


        waitForStart();

        if (isStopRequested()) return;

        double[] powers = {0,0,0,0};

        // Step 1:  Drive backwords for 0.3 seconds
        powers = calcPower(-FORWARD_SPEED, 0, 0);
        motorFrontLeft.setPower(powers[0]);
        motorBackLeft.setPower(powers[1]);
        motorFrontRight.setPower(powers[2]);
        motorBackRight.setPower(powers[3]);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.3)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Strafe for 2.5 seconds
        powers = calcPower(0, FORWARD_SPEED, 0);
        motorFrontLeft.setPower(powers[0]);
        motorBackLeft.setPower(powers[1]);
        motorFrontRight.setPower(powers[2]);
        motorBackRight.setPower(powers[3]);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);

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

        powers = calcPower(-FORWARD_SPEED, 0, 0);
        motorFrontLeft.setPower(powers[0]);
        motorBackLeft.setPower(powers[1]);
        motorFrontRight.setPower(powers[2]);
        motorBackRight.setPower(powers[3]);

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);


    }
}
