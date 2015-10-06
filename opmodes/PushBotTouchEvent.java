package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotTouchEvent
//

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Provide a basic autonomous operational mode that demonstrates the use of an
 * touch sensor to control the arm using a state machine for the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-30-11-45
 */
public class PushBotTouchEvent extends PushBotTelemetrySensors

{
    //--------------------------------------------------------------------------
    //
    // PushBotTouchEvent
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public PushBotTouchEvent ()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotTouchEvent

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during auto-operation.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //
        // NOTE: The touch sensor controls the WHEELS in this op-mode.  The main
        // use of the touch sensor in the other PushBot[...]Sensor classes is to
        // operate the arm.  This method operates the DRIVE WHEELS.
        //

        //
        // If a touch sensor has been detected, then set the power level to
        // zero.
        //
        if (is_touch_sensor_pressed ())
        {
            set_drive_power (0.0, 0.0);
        }
        //
        // Else a white line has not been detected, so set the power level to
        // full forward.
        //
        else
        {
            set_drive_power (1.0, 1.0);
        }

        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry

    } // loop

    /**
     * Created by jzerez17 on 10/5/15.
     */
    public static class GyroTest2 extends OpMode {
        TouchSensor touch;
        GyroSensor gyro;
        double avgTheta;
        int i;
        double yaw;
        ElapsedTime timer;



        public GyroTest2(){

        }

        @Override
        public void init(){
            touch = hardwareMap.touchSensor.get("sensor1");
            gyro = hardwareMap.gyroSensor.get("sensor2");
            for (i = 0; i < 5000; i++) {
                avgTheta += (gyro.getRotation());
            }
            avgTheta = avgTheta/5000;

        }

        public void start(){

        }
        @Override
        public void loop(){
            yaw = 0;
            timer.reset();
            if (touch.isPressed()) {
                    yaw += (gyro.getRotation() - avgTheta) * timer.time();
            }
            telemetry.addData("avgTheta", avgTheta);
            telemetry.addData("yaw", yaw);
            telemetry.addData("timer", timer.time());
        }
    }
} // PushBotTouchEvent
