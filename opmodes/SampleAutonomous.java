package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jzerez17 on 10/4/15.
 */
public class SampleAutonomous extends LinearOpMode{
    GyroSensor Gyro;
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor intake;
    Servo climberArm;
    ElapsedTime timer;
    LightSensor light;
    ColorSensor color;

    double avgTheta;
    double theta;
    double yaw;
    int i;
    int ticks;
    double speed;

    private void goStraight1 (int ticks, double speed) {
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
        leftDrive.setTargetPosition(ticks+leftDrive.getCurrentPosition());
        rightDrive.setTargetPosition(ticks+rightDrive.getCurrentPosition());
        leftDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }
    private void goStraight2 (long time, double speed) {
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
        timer.reset();
        if (timer.time() > time) {
            leftDrive.setPower(0);
            rightDrive.setPower(0);
        }
    }
    private void pivotRightEncoder (int ticks, double speed) {
        rightDrive.setPower(speed);
        rightDrive.setTargetPosition(ticks + rightDrive.getCurrentPosition());
        rightDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }
    private double getTheta(){
        timer.reset();
        yaw += (Gyro.getRotation() - avgTheta) * timer.time();
        return yaw;
    }
    private void pivotRightGyro (double desiredAngle, double speed, int tolerance) {
        if (getTheta() < 0.8*desiredAngle) {
            rightDrive.setPower(speed);
        }else if (getTheta() > 0.8*desiredAngle){
            rightDrive.setPower(0.2);
        }else if (getTheta() > desiredAngle + tolerance) {
            rightDrive.setPower(-0.2);
        }else if (getTheta() < desiredAngle + tolerance && getTheta() > desiredAngle - tolerance){
            rightDrive.setPower(0.0);
        }
    }


    @Override
    public void runOpMode() throws InterruptedException {
        Gyro = hardwareMap.gyroSensor.get("sensor1");
        leftDrive = hardwareMap.dcMotor.get("motor1");
        rightDrive = hardwareMap.dcMotor.get("motor2");
        intake = hardwareMap.dcMotor.get("intake");
        climberArm = hardwareMap.servo.get("servo1");
        light = hardwareMap.lightSensor.get("sensor2");
        color = hardwareMap.colorSensor.get("sensor3");

        //is this the right place to declare methods? Test pls




        waitForStart();

        while (opModeIsActive()) {
            for (i = 0; i < 1000; i++) {
                theta += Gyro.getRotation();
                sleep(1);
            }
            avgTheta = theta/1000;

            intake.setPower(-1);
            goStraight1(1000, 0.3);
            pivotRightGyro(45, 0.5, 2);
            goStraight1(5000, 0.5);
            pivotRightGyro(45, -0.5, 2);
            if (light.getLightDetected() < 50) {
                rightDrive.setPower(0.25);
                leftDrive.setPower(0.25);
            } else {
                rightDrive.setPower(0.0);
                leftDrive.setPower(0.0);
            }
            climberArm.setPosition(255);
            sleep(750);
            climberArm.setPosition(0);
            sleep(100);

            }
        }
    }

