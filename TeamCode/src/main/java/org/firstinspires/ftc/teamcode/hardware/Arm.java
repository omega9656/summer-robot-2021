package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {

    public DcMotorEx arm;
    public Position currentMode;

    public enum Position{
        DROP_OFF(1), //dropping off the game elements
        PICKUP(2); //picking up game elements

        public int armPosition;

        Position(int armPosition){

            this.armPosition = armPosition;
        }
    }
    public Arm(DeviceManager deviceManager){
        arm = deviceManager.arm;

        //sets default position
        arm.setTargetPosition(0);
    }

    public void run(Position position){
        arm.setTargetPosition(position.armPosition);

        //updates the arm position to where they currently are
        currentMode = position;
    }

    //sets arm to 'drop off' position
    public void dropOff(){
        run(Position.DROP_OFF);
    }
    //sets arm to 'pick up' position
    public void pickUp(){
        run(Position.PICKUP);
    }
}
