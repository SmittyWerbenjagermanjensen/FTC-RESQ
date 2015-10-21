

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by jzerez17 on 10/20/15.
 */
public class MyFirstStateMachine extends OpMode {
    DcMotor motor1;
    DcMotor motor2;
    int moveNumber = 0;
    int pathSegment = 0;
    int targetPos;
    int loops;

    //declare int arrays
    //{left dist, right dist, speed}

    //Beacon Path
    static int[] b1 = {10, 10, 50};
    static int[] b2 = {0, 5, 50};
    static int[] b3 = {10, 0, 50};
    static int[][] beacon = {b1, b2, b3};

    //Mountain Path
    static int[] m1 = {10,10,50};
    static int[] m2 = {0, 5, 50};
    static int[] m3 = {10, 0, 50};
    static int[][] mountain = {m1, m2, m3};

    static int ticksPerRevolution = 28149;
    static int speedFactor = 100;

    public MyFirstStateMachine() {
    }
    @Override
    public void init () {
        motor1 = hardwareMap.dcMotor.get("motor_1");
        motor1.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2 = hardwareMap.dcMotor.get("motor_2");

    }
    @Override
    public void start(){

    }
    @Override
    public void loop() {
        telemetry.addData("encoders", motor1.getCurrentPosition());                         //display encoder telemetry
        telemetry.addData("Move Number", moveNumber);                                       //display the move number
        switch (moveNumber) {
            case 0:
                for (loops = 0; loops < 3; loops++) {                                       //repeat 3 times
                    targetPos = (beacon[pathSegment][0]) * ticksPerRevolution;              //find target position in beacon matrix
                    motor1.setPower((beacon[pathSegment][2]) / speedFactor);                //find motor speed in beacon matrix
                    motor2.setPower((beacon[pathSegment][2]) / speedFactor);                //find motor speed in beacon matrix
                    motor1.setTargetPosition(targetPos);                                    //set target position from beacon matrix
                    motor1.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);       //run to position

                    if (motor1.getCurrentPosition() < (targetPos + 5) && motor1.getCurrentPosition() > (targetPos - 5)) {
                        //if the motor is within 5 degrees of the target position
                        motor1.setPower(0.0);                                               //set the motor power to zero
                        pathSegment++;                                                      //move down 1 row in the beacon matrix
                        motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);    //reset encoders
                    }
                 }
                if (loops == 3) {   //When loops is equal to 3
                    moveNumber++;   //increase the move number
                }
                break;              //break


            case 1:
                motor1.setPower(0.1);
                motor1.setTargetPosition(2400);
                motor1.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

                if (motor1.getCurrentPosition() < 2405 || motor1.getCurrentPosition() > 2395) {
                    motor1.setPower(0.0);
                    moveNumber = 2;
                    motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
        }
    }
    @Override
    public void stop(){

    }

}
