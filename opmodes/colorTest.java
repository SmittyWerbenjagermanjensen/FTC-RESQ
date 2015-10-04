package com.qualcomm.ftcrobotcontroller.opmodes;


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
public class colorTest extends OpMode {
    ColorSensor sensorRGB;
    TouchSensor touch;
    boolean x = true;
    boolean bEnabled = true;




    public colorTest() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        hardwareMap.logDevices();
        sensorRGB = hardwareMap.colorSensor.get("sensor1");
        touch = hardwareMap.touchSensor.get("sensor2");


    }

    @Override
    public void loop() {
        x = false;
        sensorRGB.enableLed(x);
        if (touch.isPressed()) {
            sensorRGB.enableLed(x);
            x = true;

        }

        telemetry.addData("red", sensorRGB.red());
        telemetry.addData("blue", sensorRGB.blue());
        telemetry.addData("green", sensorRGB.green());
        telemetry.addData("hue", sensorRGB.argb());
        telemetry.addData("brightness", sensorRGB.alpha());
        telemetry.addData("x", x);


    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */

}

