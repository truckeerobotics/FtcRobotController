package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;

@TeleOp(name = "Main Drive")
public class Bigboi extends LinearOpMode {
    
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


    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        //Control toggle setup
        map.put("controller1ButtonX", false);
        map.put("controller2ButtonX", false);
        map.put("controller1ButtonB", false);
        map.put("controller2ButtonB", false);



        //Default powers
        double armPower = 0.68;
        double wristPower = 0.7;
        int intake = 0;
        int spin = 0;
        
        //hardwaremap
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


        //set motor direction
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spinMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            
            //save gamepad stick positions to veribles
            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x * 1; // 1.1 counters imperfect strafing
            double rx = gamepad1.right_stick_x;

            //Wheel power calculations
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x - rx) / denominator;
            double backLeftPower = (y - x - rx) / denominator;
            double frontRightPower = (y - x + rx) / denominator;
            double backRightPower = (y + x + rx) / denominator;
            
            //carosel spinner
            if(gamepad1.left_bumper){
                spin = 1;
            }else if(gamepad1.right_bumper){
                spin = -1;
            }else{
                spin = 0;
            }
            
            //toggle intake
            if(onPush(gamepad2.x, "controller2ButtonX")) {
                if (intake != 1) {
                    intake = 1;
                } else {
                    intake = 0;
                }
            }
            
            //toggle intake
            if(onPush(gamepad2.b, "controller2ButtonB")) {
                if (intake != -1) {
                    intake = -1;
                } else {
                    intake = 0;
                }
            }
            
            //moves arm up and down
            if(gamepad2.dpad_up){
                armMotor.setPower(0.5);
            }else if(gamepad2.dpad_down){
                armMotor.setPower(-0.3);
            }else{
                armMotor.setPower(0);
            }
            
            //old code idk why this was being used but keep it in case it breaks something  
//             if(gamepad2.dpad_up){
//                 armMotor.setPower(0.5);
//             }
//             if(gamepad2.dpad_down){
//                 armMotor.setPower(-0.3);
//             }else{
//                 armMotor.setPower(0);
//             }
            
            
            //rotate arm
            if(armPower < 1 && gamepad2.left_stick_y > 0) {
                armPower += gamepad2.left_stick_y / 500;
            }

            if(armPower > 0 && gamepad2.left_stick_y < 0){
                armPower += gamepad2.left_stick_y / 500;
            }
            
            //rotate wrist
            if(wristPower < 1 && gamepad2.right_stick_y > 0) {
                wristPower += gamepad2.right_stick_y / 500;
            }

            if(wristPower > 0 && gamepad2.right_stick_y < 0){
                wristPower += gamepad2.right_stick_y / 500;
            }
            
            //setting power
            armRotation.setPosition(wristPower);
            armMovement.setPosition(armPower);
            intakeRight.setPower(intake);
            intakeLeft.setPower(intake);
            spinMotor.setPower(spin);
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            //control debugs
            telemetry.addData("arm pos: ", armMovement.getPosition());
            telemetry.addData("wrist pos: : ", armRotation.getPosition());
            telemetry.addData("frontLeft: ", frontLeftPower);
            telemetry.addData("frontRight: ", frontRightPower);
            telemetry.addData("backLeft: ", backLeftPower);
            telemetry.addData("backRight: ", backRightPower);
            telemetry.addData("X: ", gamepad2.x);
            telemetry.addData("B: ", gamepad2.b);
            telemetry.addData("LB:", gamepad1.left_bumper);
            telemetry.addData("RB:", gamepad1.right_bumper);

            telemetry.update();
        }
    }
}
