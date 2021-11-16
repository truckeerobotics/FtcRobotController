// autonomous program that drives bot forward a set distance, stops then
// backs up to the starting point using encoders to measure the distance.

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Drive Encoder")
//@Disabled
public class DriveWithEncoder extends LinearOpMode
{
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;

    @Override
    public void runOpMode() throws InterruptedException
    {
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        // You will need to set this based on your robot's
        // gearing to get forward control input to result in
        // forward motion.
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // reset encoder counts kept by motors.
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // set motors to run forward for 5000 encoder counts.
        motorFrontLeft.setTargetPosition(5000);
        motorBackLeft.setTargetPosition(5000);
        motorFrontRight.setTargetPosition(5000);
        motorBackRight.setTargetPosition(5000);

        // set motors to run to target encoder position and stop with brakes on.
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // wait for start button.

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();

        // set both motors to 25% power. Movement will start. Sign of power is
        // ignored as sign of target encoder position controls direction when
        // running to position.

        motorFrontLeft.setPower(0.25);
        motorBackLeft.setPower(0.25);
        motorFrontRight.setPower(0.25);
        motorBackRight.setPower(0.25);

        // wait while opmode is active and left motor is busy running to position.

        while (opModeIsActive() && motorFrontLeft.isBusy())   //leftMotor.getCurrentPosition() < leftMotor.getTargetPosition())
        {
            telemetry.addData("encoder-fwd-front-left", motorFrontLeft.getCurrentPosition() + "  busy=" + motorFrontLeft.isBusy());
            telemetry.addData("encoder-fwd-back-left", motorBackLeft.getCurrentPosition() + "  busy=" + motorBackLeft.isBusy());
            telemetry.addData("encoder-fwd-front-right", motorFrontRight.getCurrentPosition() + "  busy=" + motorFrontRight.isBusy());
            telemetry.addData("encoder-fwd-back-left", motorBackRight.getCurrentPosition() + "  busy=" + motorBackRight.isBusy());
            telemetry.update();
            idle();
        }

        // set motor power to zero to turn off motors. The motors stop on their own but
        // power is still applied so we turn off the power.

        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorFrontRight.setPower(0.0);
        motorBackRight.setPower(0.0);

        // wait 5 sec to you can observe the final encoder position.

        resetStartTime();

        while (opModeIsActive() && getRuntime() < 5)
        {
            telemetry.addData("encoder-fwd-front-left", motorFrontLeft.getCurrentPosition();
            telemetry.addData("encoder-fwd-back-left", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-fwd-front-right", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-fwd-back-left", motorBackRight.getCurrentPosition());
            telemetry.update();
            idle();
        }

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

        motorFrontLeft.setTargetPosition(0);
        motorBackLeft.setTargetPosition(0);
        motorFrontRight.setTargetPosition(0);
        motorBackRight.setTargetPosition(0);

        // Power sign matters again as we are running without encoder.
        motorFrontLeft.setPower(-0.25);
        motorBackLeft.setPower(-0.25);
        motorFrontRight.setPower(-0.25);
        motorBackRight.setPower(-0.25);

        while (opModeIsActive() && motorFrontLeft.getCurrentPosition() > motorFrontLeft.getTargetPosition())
        {
            telemetry.addData("encoder-fwd-front-left", motorFrontLeft.getCurrentPosition();
            telemetry.addData("encoder-fwd-back-left", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-fwd-front-right", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-fwd-back-left", motorBackRight.getCurrentPosition());
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
            telemetry.addData("encoder-fwd-front-left", motorFrontLeft.getCurrentPosition();
            telemetry.addData("encoder-fwd-back-left", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-fwd-front-right", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-fwd-back-left", motorBackRight.getCurrentPosition());
            telemetry.update();
            idle();
        }
    }
}