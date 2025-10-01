package org.firstinspires.ftc.teamcode.util.voyager;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.roadrunner.Localizer;
import xyz.devmello.voyager.execution.follower.FollowerGenerator;
import xyz.devmello.voyager.execution.follower.generators.GenericFollowerGenerator;
import xyz.devmello.voyager.math.control.Controller;
import xyz.devmello.voyager.math.control.GenericTurnController;
import xyz.devmello.voyager.math.geometry.Angle;
import xyz.devmello.voyager.robot.Drive;
import xyz.devmello.voyager.robot.Robot;
import xyz.devmello.voyager.robot.components.AbstractMotor;
import xyz.devmello.voyager.robot.components.Motor;
import xyz.devmello.voyager.robot.drive.MecanumDrive;

@Config
public class VoyagerRobot {
    public static double coefficient = 0.05;
    private DcMotor dcMotorFrontRight;
    private DcMotor dcMotorFrontLeft;
    private DcMotor dcMotorBackRight;
    private DcMotor dcMotorBackLeft;

    private Motor motorFrontRight;
    private Motor motorFrontLeft;
    private Motor motorBackRight;
    private Motor motorBackLeft;

    private Drive drive;
    private PinpointOdometry odometry;
    private Robot robot;

    private static final Controller turnController = new GenericTurnController(coefficient);
    private static final FollowerGenerator followerGenerator = new GenericFollowerGenerator(turnController);

    private xyz.devmello.voyager.Voyager voyager;

    public void init(HardwareMap map, Localizer localizer) {

        dcMotorFrontRight = map.get(DcMotor.class, "fr");
        dcMotorFrontLeft = map.get(DcMotor.class, "fl");
        dcMotorBackRight = map.get(DcMotor.class, "br");
        dcMotorBackLeft = map.get(DcMotor.class, "bl");

        dcMotorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        dcMotorFrontLeft.setDirection(DcMotor.Direction.REVERSE);

        motorFrontRight = new AbstractMotor(
                dcMotorFrontRight::setPower,
                dcMotorFrontRight::getPower
        );
        motorFrontLeft = new AbstractMotor(
                dcMotorFrontLeft::setPower,
                dcMotorFrontLeft::getPower
        );
        motorBackRight = new AbstractMotor(
                dcMotorBackRight::setPower,
                dcMotorBackRight::getPower
        );
        motorBackLeft = new AbstractMotor(
                dcMotorBackLeft::setPower,
                dcMotorBackLeft::getPower
        );

        drive = new MecanumDrive(
                motorFrontRight,
                motorFrontLeft,
                motorBackRight,
                motorBackLeft,
                Angle.ZERO,
                false,
                false,
                true
        );

        odometry = new PinpointOdometry(localizer);

        robot = new Robot(drive, odometry);

        voyager = new xyz.devmello.voyager.Voyager(robot, followerGenerator);
    }


    public xyz.devmello.voyager.Voyager voyager() {
        return this.voyager;
    }
}
