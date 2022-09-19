//package org.firstinspires.ftc.teamcode;
//
//import static org.firstinspires.ftc.robotcore.external.navigation.NavUtil.meanIntegrate;
//import static org.firstinspires.ftc.robotcore.external.navigation.NavUtil.plus;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;
//import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.AccelerationSensor;
//import com.qualcomm.robotcore.hardware.HardwareDevice;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.RobotLog;
//
//import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.robotcore.external.navigation.Position;
//import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
//
//import java.lang.reflect.Array;
//import java.lang.reflect.Constructor;
//import java.util.List;
//
//@Autonomous(name = "IMU Testing")
//@Disabled
//public class TestingIMU extends LinearOpMode {
//    private BNO055IMU imu;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.loggingEnabled = true;
//        parameters.loggingTag = "IMU";
//        parameters.mode = BNO055IMU.SensorMode.IMU;
//
//        TestAccelerationIntegrator integrator = new TestAccelerationIntegrator();
//        parameters.accelerationIntegrationAlgorithm = integrator;
//
//
//        imu = hardwareMap.get(BNO055IMU.class, "imu");
//
//        imu.initialize(parameters);
//
//        waitForStart();
//
//        //integrator.setAccelerationOffset(imu.getAcceleration());
//        imu.startAccelerationIntegration(new Position(DistanceUnit.INCH, 0.0, 0.0, 0.0, 0), new Velocity(DistanceUnit.INCH, 0.0, 0.0, 0.0, 0), 100);
//        while (opModeIsActive()) {
//            Orientation orientation = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
//            Acceleration acceleration = imu.getOverallAcceleration();
//            Position position = imu.getPosition();
//
//            telemetry.addData("X Axis Angle", orientation.firstAngle);
//            telemetry.addData("Y Axis Angle", orientation.secondAngle);
//            telemetry.addData("Z Axis Angle", orientation.thirdAngle);
//
//            telemetry.addData("X Axis Accel", acceleration.xAccel);
//            telemetry.addData("Y Axis Accel", acceleration.yAccel);
//            telemetry.addData("Z Axis Accel", acceleration.zAccel);
//
//            telemetry.addData("X Axis Pos", position.x);
//            telemetry.addData("Y Axis Pos", position.y);
//            telemetry.addData("Z Axis Pos", position.z);
//
//            telemetry.addData("Temp", imu.getTemperature().temperature);
//
//            telemetry.addData("Status", imu.getSystemStatus());
//            telemetry.addData("Calibration Status", imu.getCalibrationStatus());
//
//            telemetry.addData("Raw Velocity", imu.getVelocity());
//
//            telemetry.addData("Gravity", imu.getGravity());
//
//            telemetry.update();
//        }
//
//
//    }
//}
//
