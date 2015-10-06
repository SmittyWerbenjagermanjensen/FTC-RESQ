package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jzerez17 on 10/5/15.
 */
public class Gyrotest2 extends OpMode{
    GyroSensor gyro;
    TouchSensor touch;
    double yaw;
    double avgTheta;
    int i;
    //ElapsedTime timer;


    public  Gyrotest2(){
    }
    @Override
    public void init(){
        gyro = hardwareMap.gyroSensor.get("sensor1");
        touch = hardwareMap.touchSensor.get("sensor2");

        for (i = 0; i <2500; i++) {
            avgTheta += gyro.getRotation();
        }
        avgTheta = avgTheta/2500;

    }
    @Override
    public void start(){

    }
    @Override
    public void loop(){
        /*
        timer.reset();
        yaw = 0;
        if (touch.isPressed()) {
            yaw += (gyro.getRotation()-avgTheta) * timer.time();
        }
*/
        telemetry.addData("theta", gyro.getRotation());
        telemetry.addData("avgTheta", avgTheta);
        //telemetry.addData("time", timer.time());

    }
}

