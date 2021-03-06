

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jzerez17 on 10/20/15.
 */



public class MyFirstStateMachine extends OpMode {
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    Servo climberArm;
    Servo rightZiplineTrigger;
    Servo leftZiplineTrigger;
    ColorSensor color_1;
    ColorSensor color_2;

    int moveNumber;
    int pathSegment = 0;
    int targetPosLeft = 0;
    int targetPosRight = 0;
    int lastPosLeft = 0;
    int lastPosRight = 0;
    double targetSpeed = 0;
    double climberArmPos = 0;
    double intakePower = -1;
    final static double climberArmResetPos = 0.3;
    int loops = 0;
    double totalBlueOne;
    double totalBlueTwo;
    boolean redBeacon = false;
    boolean blueBeacon = false;
    boolean climberArmReset = false;
    int e;



    final static int left = 0;
    final static int right = 1;
    final static double oneEightyDegreeTurn = (17*Math.PI);
    final static double ninetyDegreeTurn = (8.5*Math.PI);
    final static double fortyFiveDegreeTurn = (4.25*Math.PI);

    //declare double arrays
    //{left dist, right dist, speed}

    //Beacon Path
    final static double[] b1 = {11.7, 12, 25};
    final static double[] b2 = {fortyFiveDegreeTurn, 0, 25};
    final static double[] b3 = {77, 77, 20};  //working numbers : 75 75 20
    final static double[] b4 = {fortyFiveDegreeTurn, 0, 35};
    final static double[] b5 = {5, 5, 25};   //working number : 7 7 25
    //final static double [] b6 = {fortyFiveDegreeTurn, -fortyFiveDegreeTurn, 35};
   // final static double [] b7 = {2.5, 2.5, 35};
    final static double[][] beacon = {b1, b2, b3, b4, b5};

    //Second Color reading path
    final static double[] cr1 = {3.5, 3.5, 0.25};
    final static double[][] colorReadingMatix = {cr1};

    //Repair Path
    final static double[] r1 = {6, 0, 15};
    final static double [] r2 = {-6, 0, 15};
    final static double[] r3 = {0,0,0};
    final static double[] r4 = {0, 6, 15};
    final static double [] r5 = {0, -6, 15};
    final static double[][] repair = {r1, r2, r3, r4, r5};

    final static double ticksPerInch = (1120/(Math.PI*4)
    );
    final static int speedFactor = 100;
    public ElapsedTime coolDown = new ElapsedTime();
    public ElapsedTime colorReading = new ElapsedTime();


    public MyFirstStateMachine() throws InterruptedException{
    }
    @Override
    public void init () {
        motor1 = hardwareMap.dcMotor.get("motor_1");
        motor1.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2 = hardwareMap.dcMotor.get("motor_2");
        motor3 = hardwareMap.dcMotor.get("motor_3");
        motor2.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor1.setDirection(DcMotor.Direction.REVERSE);
        climberArm = hardwareMap.servo.get("servo_1");
        color_1 = hardwareMap.colorSensor.get("sensor_1");
        color_2 = hardwareMap.colorSensor.get("sensor_2");
        rightZiplineTrigger = hardwareMap.servo.get("servo_2");
        leftZiplineTrigger = hardwareMap.servo.get("servo_3");

        //rightZiplineTrigger.setPosition(1);
        //leftZiplineTrigger.setPosition(0);
        //climberArm.setPosition(1);

        moveNumber = 0;
        pathSegment = 0;
        climberArmPos = 1;
        motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }


    @Override
    public void start(){
    moveNumber = 1;
        motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightZiplineTrigger.setPosition(0);
        leftZiplineTrigger.setPosition(0.7);
        climberArm.setPosition(1);

    }

    @Override
    public void loop() {
        motor3.setPower(intakePower);

        switch (moveNumber) {
            case 1:
                if (pathSegment < 5) {                                                      //repeat 5 times
                    targetPosLeft = (lastPosLeft + (int)Math.round((beacon[pathSegment][left]) * ticksPerInch));    //find target position in beacon matrix
                    targetPosRight = (lastPosRight + (int)Math.round((beacon[pathSegment][right])*ticksPerInch));
                    targetSpeed = ((beacon[pathSegment][2])/speedFactor);
                    motor1.setPower(targetSpeed);                                                   //find motor speed in beacon matrix
                    motor2.setPower(targetSpeed);                                                   //find motor speed in beacon matrix
                    motor1.setTargetPosition(targetPosLeft);                                        //set target position from beacon matrix
                    motor2.setTargetPosition(targetPosRight);
                    motor1.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);               //run to position
                    motor2.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);


                    if ((motor1.getCurrentPosition() < (targetPosLeft + 2.5) && motor1.getCurrentPosition() > (targetPosLeft - 2.5)) &&
                            motor2.getCurrentPosition() < (targetPosRight + 2.5) && motor2.getCurrentPosition() > (targetPosRight - 2.5)) {
                        //if the motor is within 5 degrees of the target position
                        motor1.setPower(0.0);                                               //set the motor power to zero
                        motor2.setPower(0.0);
                        lastPosLeft = motor1.getCurrentPosition();
                        lastPosRight = motor2.getCurrentPosition();
                        pathSegment++;
                    }
                 }
                if (pathSegment == 5) {                                                     //When loops is equal to 3
                    intakePower = 0;
                    moveNumber++;                                                           //increase the move number
                    pathSegment = 0;                                                        //reset the path segment
                    motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                    motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                    colorReading.reset();
                }
                break;                                                                      //break


            case 2:
                color_1.enableLed(true);
                color_2.enableLed(true);
                climberArm.setPosition(climberArmPos);
                coolDown.startTime();
                colorReading.startTime();

                if ((coolDown.time() > 0.1)&&(climberArmPos>0.1)&&(!climberArmReset)) {
                    coolDown.reset();
                    climberArmPos = climberArmPos - 0.025;
                }
                if ((climberArm.getPosition() < 0.1)&&(climberArm.getPosition() > 0)&&(coolDown.time()>0.25)) {
                    climberArmReset = true;
                    climberArmPos = climberArmResetPos;
                    coolDown.reset();

                }
               if ((Math.abs(climberArm.getPosition()-climberArmResetPos) < 0.1) &&(coolDown.time() > 0.7)&&(climberArmReset)) {
                    climberArmPos = 0;

                }
                telemetry.addData("CADEN", climberArm.getPosition());


                if (colorReading.time()<10){                //If the time is less than 10 seconds
                    totalBlueOne += color_1.blue();         //add the blue readings from one sensor
                    totalBlueTwo += color_2.blue();         //add the blue readings from the second sensor
                }
                else {
                    if (totalBlueTwo > totalBlueOne) {      //The second sensor is greater than the first
                        pathSegment = 0;                    //set it to run the first half of the matrix
                        motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                        motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                        lastPosRight = 0;                   //reset motor target
                        lastPosLeft = 0;                    //reset motor target
                        moveNumber++;                       //go to the next move segment
                    } else {
                        pathSegment = 3;                    //set it to run the econd half of the matrix
                        motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                        motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                        lastPosRight = 0;                   //reset motor target
                        lastPosLeft = 0;                    //reset motor target
                        moveNumber++;                       //go to the next move segment
                    }
                }
                break;


            case 999: //CHANGE FOR BUTTON
                climberArm.setPosition(1.0);
                if (pathSegment < 5){
                    targetPosLeft = (lastPosLeft + (int)Math.round((repair[pathSegment][left]) * ticksPerInch));    //find target position in beacon matrix
                    targetPosRight = (lastPosRight + (int)Math.round((repair[pathSegment][right])*ticksPerInch));
                    targetSpeed = ((repair[pathSegment][2])/speedFactor);
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
                if ((pathSegment == 2) || pathSegment == 5) {
                    colorReading.reset();
                    moveNumber++;                                                           //increase the move number
                    pathSegment = 0;                                                        //reset the path segment
                    motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                    motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
                break;
/*
            case 4:
                if (colorReading.time() < 5) {
                    totalBlueTwo += color.blue();
            }
                else{
                    if(totalBlueOne>totalBlueTwo){
                        pathSegment = 0;
                        moveNumber++;
                    } else {
                        pathSegment = 2;
                        moveNumber++;
                    }
                }
                break;

/*
            case 4:

                color.enableLed(false);
                if (redBeacon) {
                    pathSegment = 4;
                }else {
                    pathSegment = 0;
                }

                if ((pathSegment < 3) || (pathSegment < 7)){
                    targetPosLeft = (lastPosLeft + (int)Math.round((repair[pathSegment][left]) * ticksPerInch));    //find target position in beacon matrix
                    targetPosRight = (lastPosRight + (int)Math.round((repair[pathSegment][right])*ticksPerInch));
                    targetSpeed = ((repair[pathSegment][2])/speedFactor);
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
                if ((pathSegment == 3) || (pathSegment == 7)) {                                                     //When loops is equal to 3
                    moveNumber++;                                                           //increase the move number
                    pathSegment = 0;                                                        //reset the path segment
                    motor2.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                    motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
                }



                break;
                */
        }


        telemetry.addData("encoders left", motor1.getCurrentPosition());                         //display encoder telemetry
        telemetry.addData("encoder right", motor2.getCurrentPosition());

        telemetry.addData("left target pos", targetPosLeft);
        telemetry.addData("right target pos", targetPosRight);
        telemetry.addData("colorsensor time", colorReading.time());

        telemetry.addData("Move Number", moveNumber);                                       //display the move number
        telemetry.addData("path segment", pathSegment);
        telemetry.addData("servo", climberArm.getPosition());
        telemetry.addData("CADEN", climberArm.getPosition());
        telemetry.addData("climberArmReset", climberArmReset);
        telemetry.addData("power",((beacon[pathSegment][2])/speedFactor));


        telemetry.addData("blue", totalBlueOne);


    }
    @Override
    public void stop(){

    }

}
