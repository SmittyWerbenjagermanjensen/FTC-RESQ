package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jzerez17 on 10/31/15.
 */
public class COLORAVERAGES extends OpMode {
    ColorSensor color;
    int i;
    double green;
    double red;
    double blue;
    boolean isItRed;
    static ElapsedTime whyyyy = new ElapsedTime();

    public COLORAVERAGES() {

    }
    @Override
    public void init() {
        color = hardwareMap.colorSensor.get("sensor_1");
    }
    @Override
    public void start(){
        whyyyy.reset();
        whyyyy.startTime();
    }
    @Override
    public void loop(){
        if (whyyyy.time()<10) {
            green += color.green();
            blue += color.blue();
            red += color.red();
        }
        if (whyyyy.time()>10){
            if ((0.7*red)>(green)) {
                isItRed = false;
            } else {
                isItRed = true;
            }
        }

telemetry.addData("green", green);
        telemetry.addData("blue", blue);
        telemetry.addData("red", red);
        telemetry.addData("i", i);
        telemetry.addData("RvB", isItRed);
    }
    @Override
    public void stop(){

    }


}
