package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Flap {
    public Servo flap;

    public Position currentPosition;

    public enum Position{
        OPEN(0),//when flap lets blocks through
        CLOSE(1);//when flap lets balls through

        public double flapPosition;

        Position(double flapPosition){
            this.flapPosition = flapPosition;
        }
    }

    public Flap (DeviceManager deviceManager){

        //flap = deviceManager.flap;

        flap.setPosition(0);

        flap.setDirection(Servo.Direction.FORWARD);

    }

    public void run(Position position){
        flap.setPosition(position.flapPosition);

        //updates current position
        currentPosition = position;
    }

    public void open(){
        run(Position.OPEN);//opens flap to let blocks through
    }

    public void close(){
        run(Position.CLOSE);//closes flap to let balls through
    }
}
