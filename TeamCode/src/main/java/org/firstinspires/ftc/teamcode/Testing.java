package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

@TeleOp(name = "testing")
public class Testing extends LinearOpMode {

    //Define togglebind hashmap
    HashMap<String, Boolean> map=new HashMap<String, Boolean>();

    //onpush function
    boolean onPush(boolean button, String buttonName){
        //loop though toggle hashmap
        for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
            //save the key and value to varibles
            String key = entry.getKey();
            Boolean value = entry.getValue();
            //check if the key is the value passed into the function
            if(key == buttonName){
                //if it is and the last tick was a diffrent value we set it to true, thus toggleing the boolean
                if(button && !value){
                    map.put(key, button);
                    return true;
                }else{
                    map.put(key, button);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void runOpMode() throws InterruptedException {

        //Control toggle setup
        map.put("controller1ButtonX", false);
        map.put("controller2ButtonX", false);
        map.put("controller1ButtonB", false);
        map.put("controller2ButtonB", false);


        Servo intakeLeft = hardwareMap.servo.get("intakeLeft");
        Servo intakeRight = hardwareMap.servo.get("intakeRight");

        double intakeLeftPower = 0;
        double intakeRightPower = 1;

        intakeRight.setPosition(intakeRightPower);
        intakeLeft.setPosition(intakeLeftPower);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //open
            if(onPush(gamepad1.x, "controller1ButtonX")){
                intakeLeftPower = 0;
                intakeRightPower = 1;
            }

            //closed
            if(onPush(gamepad1.b, "controller1ButtonB")) {
                intakeLeftPower = 0.5;
                intakeRightPower = 0.5;
            }

            intakeRight.setPosition(intakeRightPower);
            intakeLeft.setPosition(intakeLeftPower);

            telemetry.addData("intakeRight: ", intakeRight.getPosition());
            telemetry.addData("intakeLeft: ", intakeLeft.getPosition());

            telemetry.update();
        }
    }
}
