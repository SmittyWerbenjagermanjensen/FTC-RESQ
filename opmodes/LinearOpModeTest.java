package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by jzerez17 on 10/14/15.
 */
public class LinearOpModeTest extends LinearOpMode {

    DcMotor andymark;
    DcMotor tetrix;
    int x = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        andymark = hardwareMap.dcMotor.get("motor1");
        tetrix = hardwareMap.dcMotor.get("motor2");
        waitOneFullHardwareCycle();

        waitForStart();
        while (opModeIsActive()) {
/*
           andymark.setPower(0.5);
            tetrix.setPower(0.5);
           andymark.setTargetPosition(2400);
            andymark.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

            andymark.setPower(0.0);
            tetrix.setPower(0.0);
            telemetry.addData("encoder", andymark.getCurrentPosition());

            andymark.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            while (andymark.getCurrentPosition()<=1000) {
                andymark.setPower(0.5);
                tetrix.setPower(0.5);
            }
            andymark.setPower(0.0);
            tetrix.setPower(0.0);
            telemetry.addData("encoder", andymark.getCurrentPosition());

        }
        */

            if (x == 1) {
                andymark.setPower(0.5);
                tetrix.setPower(0.5);
                sleep(1000);
                x = 2;
            }


            if (x == 2) {
                andymark.setPower(0.0);
                tetrix.setPower(0.0);
            }
            telemetry.addData("encoder", andymark.getCurrentPosition());
            telemetry.addData("x", x);

        }
    }
}
