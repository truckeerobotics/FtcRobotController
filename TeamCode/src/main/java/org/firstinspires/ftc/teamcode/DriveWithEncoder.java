package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Drive Encoder")
@Disabled
public class DriveWithEncoder extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        telemetry.addData("status: ", "init!");

        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        // You will need to set this based on your robot's
        // gearing to get forward control input to result in
        // forward motion.
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // From current position back up to starting point. In this example instead of
        // having the motor monitor the encoder we will monitor the encoder ourselves.

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorFrontLeft.setTargetPosition(1000);
        motorBackLeft.setTargetPosition(1000);
        motorFrontRight.setTargetPosition(1000);
        motorBackRight.setTargetPosition(1000  );

        waitForStart();

        if (isStopRequested()) return;

        // Power sign matters again as we are running without encoder.
        motorFrontLeft.setPower(-1);
        motorBackLeft.setPower(-1);
        motorFrontRight.setPower(-1);
        motorBackRight.setPower(-1);

        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > motorFrontLeft.getTargetPosition())
        {
            telemetry.addData("encoder-back-front-left", motorFrontLeft.getCurrentPosition());
            telemetry.addData("encoder-back-back-left", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-back-front-right", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-back-back-left", motorBackRight.getCurrentPosition());
            telemetry.update();
            idle();
        }

        // set motor power to zero to stop motors.

        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorFrontRight.setPower(0.0);
        motorBackRight.setPower(0.0);

        resetStartTime();

        while (opModeIsActive() && getRuntime() < 5)
        {
            telemetry.addData("encoder-back-front-left", motorFrontLeft.getCurrentPosition());
            telemetry.addData("encoder-back-back-left", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-back-front-right", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-back-back-left", motorBackRight.getCurrentPosition());
            telemetry.update();
            idle();
        }
    }
}