package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class TrayTilt {
    public Servo trayTilt;
    public Position currentMode;

    public enum Position {
        READY(0.0),
        TILT(0.5);

        public double tiltPosition;

        Position(double tiltPosition) {
            this.tiltPosition = tiltPosition;
        }

    }

    public TrayTilt(DeviceManager deviceManager){
        trayTilt = deviceManager.trayTilt;
        
        trayTilt.setPosition(0);

    }

    public void run(Position position){
        trayTilt.setPosition(position.tiltPosition);
        currentMode = position;
    }

    public void ready(){
        run(Position.READY);
    }

    public void tilt(){
        run(Position.TILT);
    }


}

