package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Arm auto (WIP)")
public class ArmAuto extends LinearOpMode {


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
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        DcMotor intakeRight = hardwareMap.dcMotor.get("intakeRight");
        DcMotor spinMotor = hardwareMap.dcMotor.get("spin");
        DcMotor armMotor = hardwareMap.dcMotor.get("armMotor");
        Servo armMovement = hardwareMap.servo.get("armMovement");
        Servo armRotation = hardwareMap.servo.get("armRotation");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spinMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        armMovement.setPosition(0.65);

        armMotor.setPower(0.5);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.3)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        armMotor.setPower(0);
        armMovement.setPosition(0.2);
        while (opModeIsActive() && (runtime.seconds() < 15)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

    }
}
