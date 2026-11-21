package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "TeleOp")
public class MyFIRSTJavaOpMode extends OpMode {
    private IMU imu;
    private DcMotor frontright;
    private DcMotor shooter;
    private DcMotor intake;
    private DcMotor frontleft;
    private DcMotor backright;
    private DcMotor backleft;
    double yPower = 0;
    double rxPower = 0;
    double xPower = 0;
    double shooterPower = 0.75;
    double shooterreversePower = 0.65;
    double intakePower = 1;
    double denominator = 1;

    static final double FRONT_RIGHT_ADJUSTMENT = 0.55;


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
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        intake = hardwareMap.get(DcMotor.class, "intake");

        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//    digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
//    sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
//    servoTest = hardwareMap.get(Servo.class, "servoTest");
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.REVERSE);
        backright.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE);
        backleft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE);
        frontright.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE);
        frontleft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE);
        frontright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
    }

    @Override
    public void loop() {
        telemetry.addLine("Press A To Reset Yaw");
        telemetry.addLine("Moving The Right Joystick Left And Right Turns The Bot Left And Right");
        telemetry.addLine("The Left Stick moves the robot back and forth + strafes");
//        int position1 = frontright.getCurrentPosition();
//        int position2 = frontleft.getCurrentPosition();
//        int position3 = backright.getCurrentPosition();
//        int position4 = backleft.getCurrentPosition();

        if (gamepad1.a) {
            imu.resetYaw();
        }
        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

//        double rotX = xPower * Math.cos(-botHeading) - yPower * Math.sin(-botHeading);
//        double rotY = xPower * Math.sin(-botHeading) + yPower * Math.cos(-botHeading);
//        rotX = rotX * 1.1;

        double denominator = Math.max(Math.abs(yPower) + Math.abs(xPower) + Math.abs(rxPower), 1);
        double frontleftPower = (yPower + xPower + rxPower) / denominator;
        double frontrightPower = ((yPower - xPower - rxPower) * FRONT_RIGHT_ADJUSTMENT) / denominator;
        double backrightPower = (yPower + xPower - rxPower) / denominator;
        double backleftPower = (yPower - xPower + rxPower) / denominator;
//        double CPR = 28;
//        double revolutions1 = position1/CPR;
//        double revolutions2 = position1/CPR;
//        double revolutions3 = position1/CPR;
//        double revolutions4 = position1/CPR;
//        double angle1 = revolutions1 * 360;
//        double angle2 = revolutions2 * 360;
//        double angle3 = revolutions3 * 360;
//        double angle4 = revolutions4 * 360;
//        double angleNormalized1 = angle1 % 360;
//        double angleNormalized2 = angle2 % 360;
//        double angleNormalized3 = angle3 % 360;
//        double angleNormalized4 = angle4 % 360;

        // run until the end of the match (driver presses STOP)
        if (gamepad2.right_trigger > 0.3) {
            intake.setPower(-intakePower);
        } else if (gamepad2.x){
            intake.setPower(intakePower);
        } else {
            intake.setPower(0);
        }



        if (gamepad2.b) {
            shooter.setPower(-shooterreversePower);
        }
        if (gamepad2.left_trigger > 0.3) {
            shooter.setPower(shooterPower);
        } else {
            shooter.setPower(0);
        }
        yPower = this.gamepad1.left_stick_y;
        xPower = -this.gamepad1.left_stick_x * 1.1;
        rxPower = -this.gamepad1.right_stick_x;
        frontleft.setPower(frontleftPower);
        backleft.setPower(backleftPower);
        frontright.setPower(frontrightPower);
        backright.setPower(backrightPower);
        telemetry.addData("Target Power", yPower);
//        telemetry.addData("Frontright Encoder Position", position1);
//        telemetry.addData("Frontleft Encoder Position", position2);
//        telemetry.addData("Backright Encoder Position", position3);
//        telemetry.addData("Backleft Encoder Position", position4);
        telemetry.addData("Shooter Power", shooter.getPower());
        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Frontright Motor Power", frontright.getPower());
        telemetry.addData("Frontleft Motor Power", frontleft.getPower());
        telemetry.addData("Backleft Motor Power", backleft.getPower());
        telemetry.addData("Backright Motor Power", backright.getPower());
        telemetry.addData("Status", "Running");
        telemetry.update();
    }
}