package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class MyFIRSTJavaOpMode extends OpMode {
    private IMU imu;
    private DcMotor frontright;
    private DcMotor intake;
    private DcMotor frontleft;
    private DcMotor backright;
    private DcMotor backleft;
    double yPower = 0;
    double xPower = 0;
    double rxPower = 0;
    double intakePower = 1;
    double denominator = 1;


//    private DigitalChannel digitalTouch;
//    private DistanceSensor sensorColorRange;
//    private Servo servoTest;

    @Override
    public void init() {
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        backright = hardwareMap.get(DcMotor.class, "backright");
        backleft = hardwareMap.get(DcMotor.class, "backleft");
        intake = hardwareMap.get(DcMotor.class, "intake");
//    digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
//    sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
//    servoTest = hardwareMap.get(Servo.class, "servoTest");
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.REVERSE);
        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
    }

    @Override
    public void loop() {
        telemetry.addLine("Press A To Reset Yaw");
        telemetry.addLine("Moving The Right Joystick Left And Right Turns The Bot Left And Right");
        telemetry.addLine("The Left Stick moves the robot back and forth + strafes");

        if (gamepad1.a) {
            imu.resetYaw();
        }
        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = rxPower * Math.cos(-botHeading) - yPower * Math.sin(-botHeading);
        double rotY = xPower * Math.sin(-botHeading) + yPower * Math.cos(-botHeading);

        double denominator = Math.max(Math.abs(yPower) + Math.abs(rxPower) + Math.abs(xPower), 1);
        double frontleftPower = (rotY + rotX + xPower) / denominator;
        double frontrightPower = (rotY - rotX - xPower) / denominator;
        double backrightPower = (rotY + rotX - xPower) / denominator;
        double backleftPower = (rotY - rotX + xPower) / denominator;

        // run until the end of the match (driver presses STOP)
        if (gamepad1.y) {
            intake.setPower(intakePower);
        } else {
            intake.setPower(0);
        }
        yPower = this.gamepad1.left_stick_y;
        rxPower = -this.gamepad1.left_stick_x * 1.1;
        xPower = -this.gamepad1.right_stick_x;
        frontleft.setPower(frontleftPower);
        backleft.setPower(backleftPower);
        frontright.setPower(frontrightPower);
        backright.setPower(backrightPower);
        telemetry.addData("Target Power", yPower);
        telemetry.addData("Motor Power", frontright.getPower());
        telemetry.addData("Motor Power", frontleft.getPower());
        telemetry.addData("Motor Power", backleft.getPower());
        telemetry.addData("Motor Power", backright.getPower());
        telemetry.addData("Status", "Running");
        telemetry.update();
    }







}