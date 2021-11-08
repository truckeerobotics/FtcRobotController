package org.firstinspires.ftc.teamcode;

// importing many different libraries

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

// the beginning of the teleop mode

@TeleOp(name = "AI vision", group = "Concept")

// defining objects that the camera can see

public class VisionAI extends LinearOpMode {

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

            }
        }
    }

}