package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.*;

public class Robot {

    public static Pose2d startPose = new Pose2d(new Vector2d(0,0), Math.toRadians(90));

    public static void schedule(Command... cmd) {
        CommandScheduler.getInstance().schedule(cmd);
    }


}
