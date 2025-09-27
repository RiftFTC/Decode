package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.pathfinder.VoyagerRobot;
import xyz.devmello.voyager.Voyager;

import java.util.function.DoubleSupplier;

@Config
public class DriveSys extends SubsystemBase {
    public final MecanumDrive drive;

    public static boolean AUTOMATION = false;
    public static double slow = 1;
    public static double slowT = 0.7;
    private VoyagerRobot voyagerRobot;
    public Voyager voyager;

    public DriveSys(HardwareMap hardwareMap) {
        drive = new MecanumDrive(hardwareMap, Robot.startPose);
        voyagerRobot.init(hardwareMap, drive.localizer);
        voyager = voyagerRobot.voyager();
    }

    public Command drive(DoubleSupplier f, DoubleSupplier s, DoubleSupplier t) {
        return new RunCommand(
                ()-> {
                    if (!AUTOMATION) {
                        drive.setDrivePowers(new PoseVelocity2d(
                                new Vector2d(
                                        f.getAsDouble() * slow,
                                        -s.getAsDouble()* slow
                                ),
                                -t.getAsDouble() * slow * slowT
                        ));
                    }
                },this
        );
    }


    public Command slow(double slow) {
        return new InstantCommand(() -> {
            DriveSys.slow = slow;
        });
    }

    @Override
    public void periodic() {
        drive.updatePoseEstimate();
        voyager.getOdometry().tick();
    }
}