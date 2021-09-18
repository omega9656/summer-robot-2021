package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Normal Drive")
public class NormalDrive extends OmegaTeleOp{

    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.NORMAL;
    }
}
