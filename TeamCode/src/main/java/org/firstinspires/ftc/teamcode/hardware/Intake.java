package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {
    public MotorPower currentPower; //declaring MotorPower
    public DcMotorEx intake; //declaring Intake

    public Intake(DeviceManager deviceManager) {
        intake = deviceManager.intake;

        intake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); // when ran, ran without encoder?
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE); // when power is 0, BRAKE


        currentPower = MotorPower.STOP; // default power is at STOP(0)
    }
    public enum MotorPower {
        // set the power for the intake at different states
        IN(-0.8),
        OUT(0.5),
        STOP(0);

        public double intakePower;

        MotorPower(double intakePower) {
            this.intakePower = intakePower;
        }
    }

    public void run(MotorPower motorPower){
        intake.setPower(motorPower.intakePower);
        currentPower = motorPower;
    }

    public void in(){
        run(MotorPower.IN); //calling the run method for the IN power
    }
    public void out(){
        run(MotorPower.OUT); //calling the run method for the OUT power
    }
    public void stop(){
        run(MotorPower.STOP); //calling the run method for the STOP power
    }
}
