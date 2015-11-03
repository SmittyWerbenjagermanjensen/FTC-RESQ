package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jzerez17 on 10/20/15.
 */
public class AnotherAutonomous extends OpMode {
    DcMotor motor1;
    int x = 0;


    public AnotherAutonomous() {
    }
    @Override
    public void init () {
        motor1 = hardwareMap.dcMotor.get("motor_2");
        motor1.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    @Override
    public void start(){

    }
    @Override
    public void loop() {
        telemetry.addData("encoders", motor1.getCurrentPosition());
        telemetry.addData("x", x);
        switch (x) {
            case 0:
                motor1.setPower(0.2);
                motor1.setTargetPosition(1120);
                motor1.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

                if (motor1.getCurrentPosition() < 1125 && motor1.getCurrentPosition() > 1115) {
                    motor1.setPower(0.0);
                    x = 1;
                    motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
                break;
            case 1:
                motor1.setPower(0.1);
                motor1.setTargetPosition(2400);
                motor1.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

                if (motor1.getCurrentPosition() < 2405 && motor1.getCurrentPosition() > 2395) {
                    motor1.setPower(0.0);
                    x = 2;
                    motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
        }
    }
    @Override
    public void stop(){

    }

}
