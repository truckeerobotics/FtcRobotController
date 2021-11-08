package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "hello world!")
public class opMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            telemetry.addData("Test: ", "hello world");
            telemetry.update();
        }
    }
}