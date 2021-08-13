package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    public DeviceManager deviceManager;

    public Drivetrain drivetrain;

    public Arm arm;
    public Flap flap;
    public TrayTilt trayTilt;

    public Robot(HardwareMap hardwareMap) {
        deviceManager = new DeviceManager(hardwareMap);
    }

    public void init(boolean autoIsRunning) {
        deviceManager.init(autoIsRunning);

        // drivetrain is only used for TeleOp
        if (!autoIsRunning) {
            drivetrain = new Drivetrain(deviceManager);
        }

        arm = new Arm(deviceManager);
        flap = new Flap(deviceManager);
        trayTilt = new TrayTilt(deviceManager);
    }
}
