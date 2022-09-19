//package org.firstinspires.ftc.teamcode;
//
//import static org.firstinspires.ftc.robotcore.external.navigation.NavUtil.meanIntegrate;
//import static org.firstinspires.ftc.robotcore.external.navigation.NavUtil.minus;
//import static org.firstinspires.ftc.robotcore.external.navigation.NavUtil.plus;
//
//import static java.lang.Math.round;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.robotcore.util.RobotLog;
//
//import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
//import org.firstinspires.ftc.robotcore.external.navigation.Position;
//import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
//
//public class TestAccelerationIntegrator implements BNO055IMU.AccelerationIntegrator {
//    //------------------------------------------------------------------------------------------
//    // State
//    //------------------------------------------------------------------------------------------
//
//    BNO055IMU.Parameters parameters;
//    Position position;
//    Velocity velocity;
//    Acceleration acceleration;
//
//    Acceleration accelerationOffset;
//
//    public Position getPosition() {
//        return this.position;
//    }
//
//    public Velocity getVelocity() {
//        return this.velocity;
//    }
//
//    public Acceleration getAcceleration() {
//        return this.acceleration;
//    }
//
//    public void setAccelerationOffset(Acceleration newAccelerationOffset) { accelerationOffset = newAccelerationOffset; }
//
//    //------------------------------------------------------------------------------------------
//    // Construction
//    //------------------------------------------------------------------------------------------
//
//    public TestAccelerationIntegrator() {
//        this.parameters = null;
//        this.position = new Position();
//        this.velocity = new Velocity();
//        this.acceleration = null;
//    }
//
//    //------------------------------------------------------------------------------------------
//    // Operations
//    //------------------------------------------------------------------------------------------
//
//    @Override
//    public void initialize(@NonNull BNO055IMU.Parameters parameters, @Nullable Position initialPosition, @Nullable Velocity initialVelocity) {
//        this.parameters = parameters;
//        this.position = initialPosition != null ? initialPosition : this.position;
//        this.velocity = initialVelocity != null ? initialVelocity : this.velocity;
//        this.acceleration = null;
//    }
//
//    @Override
//    public void update(Acceleration linearAcceleration) {
//        // We should always be given a timestamp here
//        if (linearAcceleration.acquisitionTime != 0) {
//            // We can only integrate if we have a previous acceleration to baseline from
//            if (acceleration != null) {
//                Acceleration accelPrev = acceleration;
//                Velocity velocityPrev = velocity;
//
//                acceleration = linearAcceleration;
//                if (accelerationOffset != null) {
//                    //RobotLog.vv("Before","accel=%s", acceleration);
//                    //RobotLog.vv("Offset","accel=%s", accelerationOffset);
//                    acceleration = minus(acceleration, accelerationOffset);
//                    //RobotLog.vv("After","accel=%s", acceleration);
//                }
//                acceleration.xAccel = round(acceleration.xAccel * 10)*0.1;
//                acceleration.yAccel = round(acceleration.yAccel * 10)*0.1;
//
//                if (accelPrev.acquisitionTime != 0) {
//                    Velocity deltaVelocity = meanIntegrate(acceleration, accelPrev);
//                    velocity = plus(velocity, deltaVelocity);
//                }
//
//                if (velocityPrev.acquisitionTime != 0) {
//                    Position deltaPosition = meanIntegrate(velocity, velocityPrev);
//                    position = plus(position, deltaPosition);
//                }
//
//                if (parameters != null && parameters.loggingEnabled) {
//                    RobotLog.vv(parameters.loggingTag, "dt=%.3fs accel=%s vel=%s pos=%s", (acceleration.acquisitionTime - accelPrev.acquisitionTime) * 1e-9, acceleration, velocity, position);
//                }
//            } else {
//                accelerationOffset = linearAcceleration;
//                acceleration = new Acceleration(linearAcceleration.unit, 0.0,0.0, 0.0, linearAcceleration.acquisitionTime);
//            }
//
//        }
//    }
//}
