
package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DeviceManager {
    public HardwareMap hardwareMap;


    public DcMotorEx intake; //intake motor
    public DcMotorEx arm; //Lifts tray to point where blocks and balls are dropped off


    public Servo trayTilt;
    public Servo flap;

    //Drivetrain Motors
    public DcMotorEx backLeft, backRight, frontLeft, frontRight;

    //Device Initialization
    void init(boolean autoIsRunning){
        if(!autoIsRunning) {
            frontRight = hardwareMap.get(DcMotorEx.class, "front_right");
            frontLeft = hardwareMap.get(DcMotorEx.class, "front_left");
            backRight = hardwareMap.get(DcMotorEx.class, "back_right");
            backLeft = hardwareMap.get(DcMotorEx.class, "back_left");
        }
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        arm = hardwareMap.get(DcMotorEx.class, "arm");
        trayTilt = hardwareMap.get(Servo.class, "tray_tilt");
        flap = hardwareMap.get(Servo.class, "flap");
    }

    public DeviceManager(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }
}
