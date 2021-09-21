package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.ftdi.eeprom.FT_EEPROM_232H;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public abstract class OmegaTeleOp extends OpMode {
    Robot robot;
    ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    final int RUN_MILLS = 1500;
    final int RUN_MILLISECONDS = 1000;

    DriveMode driveMode;


    //different drive modes
    public enum DriveMode{
        SQUARED,
        CUBED,
        NORMAL;
    }

    public static final double DEFAULT_STRAFE = 2;

    abstract public DriveMode getCurrentMode();

    @Override
    public void init(){
        robot = new Robot(hardwareMap);
        robot.init(false);
        time.reset();
    }

    @Override
    public void loop() {
        drive(2, DriveMode.NORMAL);
        //trayTilt();
        arm();
        //flap();
        intake();
        moveArm();
        //dropOffCircles();
        //dropOffCubes();
        moveTrayTilt();

        telemetry.addLine("Arm")
                .addData("Arm Position: ", robot.arm.arm.getCurrentPosition());
    }

    /*public void dropOffCircles(){

        if(gamepad1.right_bumper ){
            time.reset();
            time.startTime();
            while(time.milliseconds() < RUN_MILLS){
                drive(DEFAULT_STRAFE,DriveMode.NORMAL);
            }
            robot.trayTilt.ready();
            time.reset();
            time.startTime();
            while(time.milliseconds() < RUN_MILLISECONDS){
                drive(DEFAULT_STRAFE, DriveMode.NORMAL);
            }
            robot.trayTilt.tilt();
            time.reset();
            time.startTime();
            while(time.milliseconds() < RUN_MILLS){
                drive(DEFAULT_STRAFE,DriveMode.NORMAL);
            }
            robot.trayTilt.ready();
        }
    }*/

    public void dropOff(boolean flap){

        time.reset();
        time.startTime();

        while(time.milliseconds() < 200){
            drive(DEFAULT_STRAFE, DriveMode.NORMAL);
        }

        robot.trayTilt.ready(); // already should be in position

        // lift arm
        time.reset();
        time.startTime();
        while(time.milliseconds() < 1000){
            robot.arm.dropOff();
        }

        // tilt tray and open flap
        time.reset();
        time.startTime();
        while(time.milliseconds() < 500){
            robot.trayTilt.tilt();
            if (flap) robot.flap.open();
            robot.flap.open();
        }


        //tilt back and close flap
        time.reset();
        time.startTime();
        while(time.milliseconds() < 500){
            robot.trayTilt.ready();
            if (flap) robot.flap.close();
        }

        // drop arm back down
        time.reset();
        time.startTime();
        while (time.milliseconds() < 1000){
            robot.arm.pickUp();
        }

    }

    public void moveTrayTilt() {
        if (gamepad1.x) {
            robot.trayTilt.tilt();
        } else if (gamepad1.y) {
            robot.trayTilt.ready();
        }
    }


    public void dropOffCubes(){
        if (gamepad1.left_bumper){
            dropOff(true);
        }
    }

    public void dropOffCircles(){
        if (gamepad1.right_bumper){
            dropOff(false);
        }
    }

    public void moveArm(){
        if (gamepad1.right_bumper){
            robot.arm.dropOff();
        }
    }

    public void drive(double strafe, DriveMode driveMode) {
        // https://gm0.copperforge.cc/en/stable/docs/software/mecanum-drive.html
        // https://www.chiefdelphi.com/t/paper-mecanum-and-omni-kinematic-and-force-analysis/106153/5 (3rd paper)


        // moving left joystick up means robot moves forward
        double vertical = -gamepad1.left_stick_y;  // flip sign because y axis is reversed on joystick

        // moving left joystick to the right means robot moves right
        double horizontal = gamepad1.left_stick_x * strafe;  // counteract imperfect strafing by multiplying by constant

        // moving right joystick to the right means clockwise rotation of robot
        double rotate = gamepad1.right_stick_x;

        // calculate initial power from gamepad inputs
        // to understand this, draw force vector diagrams (break into components)
        // and observe the goBILDA diagram on the GM0 page (linked above)
        // both our front wheel powers are set to negative because of gears
        double frontLeftPower = (vertical + horizontal + rotate);
        double backLeftPower = vertical - horizontal + rotate;
        double frontRightPower = -(vertical - horizontal - rotate) * 0.4;
        double backRightPower = vertical + horizontal - rotate;

        // if there is a power level that is out of range
        if (
                Math.abs(frontLeftPower) > 1 ||
                        Math.abs(backLeftPower) > 1 ||
                        Math.abs(frontRightPower) > 1 ||
                        Math.abs(backRightPower) > 1
        ) {
            // scale the power within [-1, 1] to keep the power levels proportional
            // (if the power is over 1 the FTC SDK will just make it 1)

            // find the largest power
            double max = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            max = Math.max(Math.abs(frontRightPower), max);
            max = Math.max(Math.abs(backRightPower), max);

            // scale everything with the ratio max:1
            // don't need to worry about signs because max is positive
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        // square or cube gamepad inputs
        if (getCurrentMode() == DriveMode.SQUARED) {
            // need to keep the sign, so multiply by absolute value of itself
            frontLeftPower *= Math.abs(frontLeftPower);
            backLeftPower *= Math.abs(backLeftPower);
            frontRightPower *= Math.abs(frontRightPower);
            backRightPower *= Math.abs(backRightPower);
        } else if (getCurrentMode() == DriveMode.CUBED) {
            frontLeftPower = Math.pow(frontLeftPower, 3);
            backLeftPower = Math.pow(backLeftPower, 3);
            frontRightPower = Math.pow(frontRightPower, 3);
            backRightPower = Math.pow(backRightPower, 3);
        } // if drive mode is normal, don't do anything

        // set final power values to motors
        robot.deviceManager.frontLeft.setPower(frontLeftPower);
        robot.deviceManager.backLeft.setPower(backLeftPower);
        robot.deviceManager.frontRight.setPower(frontRightPower);
        robot.deviceManager.backRight.setPower(backRightPower);
    }

    public void intake(){
        if(gamepad2.left_bumper) {
            robot.intake.in();
        }

        else if(gamepad2.right_bumper) {
            robot.intake.out();
        }
        else {
            robot.intake.stop();
        }
    }

    public void trayTilt(){
        boolean pressedOnce = false;

        if(gamepad1.right_bumper && !pressedOnce){

            robot.trayTilt.tilt();
            pressedOnce = true;
        }

         if(gamepad1.right_bumper && pressedOnce){
             robot.trayTilt.ready();
             pressedOnce = false;
         }
    }


    public void flap(){
        boolean pressedOnce = false;

        if(gamepad2.x && !pressedOnce){
            robot.flap.open();
            pressedOnce = true;
        }
        if(gamepad2.x && pressedOnce){
            robot.flap.close();
            pressedOnce = false;
        }
    }
    public void arm(){
        boolean pressedOnce = false;
        if(gamepad2.y && !pressedOnce){
            robot.arm.dropOff();
            pressedOnce = true;
        }

        if(gamepad2.y && pressedOnce){
            robot.arm.pickUp();
            pressedOnce = false;
        }
    }
}
