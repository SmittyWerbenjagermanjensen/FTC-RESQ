package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;




    import com.qualcomm.robotcore.eventloop.opmode.OpMode;
    import com.qualcomm.robotcore.hardware.ColorSensor;
    import com.qualcomm.robotcore.hardware.LegacyModule;
    import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
    import com.qualcomm.robotcore.hardware.LED;
    import com.qualcomm.robotcore.hardware.TouchSensor;
    import com.qualcomm.ftcrobotcontroller.R;

    /**
     * TeleOp Mode
     * <p>
     * Enables control of the robot via the gamepad
     */

public class GyroTest extends LinearOpMode {
        GyroSensor sensor1;
        boolean x;
        double theta;
        int loops;
        double Averagetheta;
        double yaw;
        int desiredAngle;




        public void rotation(int desiredAngle) {

                yaw = (sensor1.getRotation() - Averagetheta) * time ;
                if (yaw < desiredAngle) {
                    x = true;
                } else {
                    //stop robot lel
                    x = false;
                }

        }



        @Override

        public void runOpMode() throws InterruptedException{
           sensor1 = hardwareMap.gyroSensor.get("sensor1");

            for (loops = 0; loops < 1000; loops++){
                theta += sensor1.getRotation();
                sleep(1);
            }
            Averagetheta = theta/1000;

            waitForStart();

            while (opModeIsActive()) {

                rotation(90);

            }
        }

//Github pls


    }



