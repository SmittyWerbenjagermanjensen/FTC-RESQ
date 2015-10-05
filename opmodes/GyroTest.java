package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class GyroTest extends LinearOpMode {
        GyroSensor sensor1;
        int x;
        double theta;
        int loops;
        double Averagetheta;
        double yaw;
        int desiredAngle;
        double speed;
        ElapsedTime timer;

    public double getTheta() {
        timer.reset();
        yaw += (sensor1.getRotation() - Averagetheta) * timer.time() / 1000000000;
        return yaw;
        }

    public void Gyroturn(int desiredAngle, double speed) {

        if (getTheta() < 0.8*desiredAngle) { //If the current angle is equal to 80% of the desired Angle
            //keep turning at "speed" power
            x = 2;

        } else if (getTheta() > 0.8*desiredAngle) {
            //slow down the motors to a constant power
            x = 1;
        } else if (getTheta() > (desiredAngle - 1) && getTheta() < (desiredAngle + 1)) {
            //if current angle is within 1 degree of desired angle
            //stop the motors
            x = 0;


        } else if (getTheta() > desiredAngle + 1) {
            //turn the other way at constant power
            x = -1;
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
            Gyroturn(90, 0.5);
            timer.reset();
            telemetry.addData("x", x);
            telemetry.addData("timer", timer);
            break;
            }
        }
    }



