package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "Bruteforce - blue")
public class BruteforceBlue extends LinearOpMode {

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

        waitForStart();

        if (isStopRequested()) return;

        double[] powers = {0,0,0,0};
        
        // Step 1:  Strafe for 0.5 seconds
        //WARNING: could stafe in wrong direction change forward_speed to a negitive if it does
        powers = calcPower(0, FORWARD_SPEED, 0);
        motorFrontLeft.setPower(powers[0]);
        motorBackLeft.setPower(powers[1]);
        motorFrontRight.setPower(powers[2]);
        motorBackRight.setPower(powers[3]);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        // Step 2:  Drive forwards for 2.3 seconds
        //WARNING: may go backwords. if it does change forward_speed to -forward_speed
        powers = calcPower(FORWARD_SPEED, 0, 0);
        motorFrontLeft.setPower(powers[0]);
        motorBackLeft.setPower(powers[1]);
        motorFrontRight.setPower(powers[2]);
        motorBackRight.setPower(powers[3]);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        //stop motors
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        
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
        powers = calcPower(0, FORWARD_SPEED, 0);
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
