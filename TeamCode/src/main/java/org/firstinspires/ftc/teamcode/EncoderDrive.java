/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Encoder Drive: Attempt 2 (WIP)", group="Pushbot")

public class EncoderDrive extends LinearOpMode {

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
        motorFrontLeft.setTargetPosition(1000);
        motorBackLeft.setTargetPosition(1000);
        motorFrontRight.setTargetPosition(1000);
        motorBackRight.setTargetPosition(1000);


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

            telemetry.addData("encoder-fwd-left-front", motorFrontLeft.getCurrentPosition() + "  busy=" + motorFrontLeft.isBusy());
            telemetry.addData("encoder-fwd-right-front", motorFrontRight.getCurrentPosition() + "  busy=" + motorFrontRight.isBusy());
            telemetry.addData("encoder-fwd-left-back", motorBackLeft.getCurrentPosition() + "  busy=" + motorBackLeft.isBusy());
            telemetry.addData("encoder-fwd-right-back", motorBackRight.getCurrentPosition() + "  busy=" + motorBackRight.isBusy());
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
            telemetry.addData("Runtime: ", getRuntime());
            telemetry.addData("encoder-fwd-left-end", motorFrontLeft.getCurrentPosition());
            telemetry.addData("encoder-fwd-right-end", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-fwd-left-end", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-fwd-right-end", motorBackRight.getCurrentPosition());
            telemetry.update();
            idle();
        }

        // From current position back up to starting point. In this example instead of
        // having the motor monitor the encoder we will monitor the encoder ourselves.

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
            telemetry.addData("encoder-front-left", motorFrontLeft.getCurrentPosition());
            telemetry.addData("encoder-back-left", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-front-right", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-back-right", motorBackRight.getCurrentPosition());
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
            telemetry.addData("encoder-front-left-end", motorFrontLeft.getCurrentPosition());
            telemetry.addData("encoder-back-left-end", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-front-right-end", motorFrontRight.getCurrentPosition());
            telemetry.addData("encoder-back-right-end", motorBackRight.getCurrentPosition());
            telemetry.update();
            idle();
        }
    }
}
