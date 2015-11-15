package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jzerez17 on 10/5/15.
 */



public class Gyrotest2 extends OpMode{
    GyroSensor gyro;
    DcMotor left;
    DcMotor right;
    TouchSensor touch;
    double yaw;
    double avgTheta;
    private int i;
    private ElapsedTime timer = new ElapsedTime();
    boolean turn = true;

    private double getTheta() {
        yaw = ((gyro.getRotation()-avgTheta)*timer.time());
        return yaw;
    }

    private void Gyroturn(double desiredAngle, double leftMotorSpeed, double rightMotorSpeed) {
        if (yaw<desiredAngle) {
            right.setPower(rightMotorSpeed);
            left.setPower(leftMotorSpeed);
        } else {
            right.setPower(0);
            left.setPower(0);
            timer.reset();
            turn = false;
            //pathseg++;
        }
    }

    public  Gyrotest2(){
    }
    @Override
    public void init(){
        gyro = hardwareMap.gyroSensor.get("sensor_1");
        touch = hardwareMap.touchSensor.get("sensor_2");
        left = hardwareMap.dcMotor.get("motor_1");
        right = hardwareMap.dcMotor.get("motor_2");

        for (i = 0; i <2500; i++) {
            avgTheta += gyro.getRotation();
        }
        if (i==2500) {
            avgTheta = avgTheta/2500;
        }

    }
    @Override
    public void start(){
        timer.reset();


    }
    @Override
    public void loop(){
        timer.startTime();
        if (turn) {
            Gyroturn(45,25,0);
        }




        telemetry.addData("theta", gyro.getRotation());
        telemetry.addData("avgTheta", avgTheta);
        telemetry.addData("timer", timer.time());
        //telemetry.addData("time", timer.time());

    }
}

