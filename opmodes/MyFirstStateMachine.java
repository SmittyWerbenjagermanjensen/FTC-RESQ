

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jzerez17 on 10/20/15.
 */
public class MyFirstStateMachine extends OpMode {
    DcMotor motor1;
    DcMotor motor2;
    int moveNumber;
    int pathSegment = 0;
    int targetPosLeft = 0;
    int targetPosRight = 0;
    int lastPosLeft = 0;
    int lastPosRight = 0;
    double targetSpeed = 0;
    boolean loops = true;


    static int left = 0;
    static int right = 1;
    //declare int arrays
    //{left dist, right dist, speed}

    //Beacon Path
    static double[] b1 = {50, 50, 50};
    static double[] b2 = {75, 75, 25};
    static double[] b3 = {100, 0, 15};
    static double[][] beacon = {b1, b2, b3};

    //Mountain Path
    static double[] m1 = {10,10, 50};
    static double[] m2 = {0, 5, 50};
    static double[] m3 = {10, 0, 50};
    static double[][] mountain = {m1, m2, m3};

    static double ticksPerInch = (1120/(Math.PI*8));
    static int speedFactor = 100;
    //public ElapsedTime coolDown = new ElapsedTime();


    public MyFirstStateMachine() throws InterruptedException{
    }
    @Override
    public void init () {
        motor1 = hardwareMap.dcMotor.get("motor_1");
        motor1.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2 = hardwareMap.dcMotor.get("motor_2");
        motor2.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor1.setDirection(DcMotor.Direction.REVERSE);
        moveNumber = 0;
        pathSegment = 0;
        telemetry.addData("Move Number", moveNumber);                                       //display the move number
        telemetry.addData("path segment", pathSegment);
        telemetry.addData("target pos", (beacon[pathSegment][0]) * ticksPerInch);

        motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

        telemetry.addData("right", motor2.getCurrentPosition());
        telemetry.addData("left", motor1.getCurrentPosition());




    }
    @Override
    public void start(){
    moveNumber = 1;
        motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    @Override
    public void loop() {

        switch (moveNumber) {
            case 1:
                if (pathSegment < 3) {                                                      //repeat 3 times
                    targetPosLeft = (lastPosLeft + (int)Math.round((beacon[pathSegment][left]) * ticksPerInch));    //find target position in beacon matrix
                    targetPosRight = (lastPosRight + (int)Math.round((beacon[pathSegment][right])*ticksPerInch));
                    targetSpeed = ((beacon[pathSegment][2])/speedFactor);
                    motor1.setPower(targetSpeed);                                                   //find motor speed in beacon matrix
                    motor2.setPower(targetSpeed);                                                   //find motor speed in beacon matrix
                    motor1.setTargetPosition(targetPosLeft);                                        //set target position from beacon matrix
                    motor2.setTargetPosition(targetPosRight);
                    motor1.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);               //run to position
                    motor2.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);


                    if ((motor1.getCurrentPosition() < (targetPosLeft + 5) && motor1.getCurrentPosition() > (targetPosLeft - 5)) &&
                            motor2.getCurrentPosition() < (targetPosRight + 5) && motor2.getCurrentPosition() > (targetPosRight - 5)) {
                        //if the motor is within 5 degrees of the target position
                        motor1.setPower(0.0);                                               //set the motor power to zero
                        motor2.setPower(0.0);
                        lastPosLeft = motor1.getCurrentPosition();
                        lastPosRight = motor2.getCurrentPosition();
                     pathSegment++;



                    }
                 }
                if (pathSegment == 3) {                                                     //When loops is equal to 3
                    moveNumber++;                                                           //increase the move number
                    pathSegment = 0;                                                        //reset the path segment
                    motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                    motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
                break;                                                                      //break


            case 2:

                break;

        }

        telemetry.addData("encoders left", motor1.getCurrentPosition());                         //display encoder telemetry
        telemetry.addData("encoder right", motor2.getCurrentPosition());

        telemetry.addData("left target pos", targetPosLeft);
        telemetry.addData("right target pos", targetPosRight);

        telemetry.addData("Move Number", moveNumber);                                       //display the move number
        telemetry.addData("path segment", pathSegment);

        telemetry.addData("power",((beacon[pathSegment][2])/speedFactor));

    }
    @Override
    public void stop(){

    }

}
