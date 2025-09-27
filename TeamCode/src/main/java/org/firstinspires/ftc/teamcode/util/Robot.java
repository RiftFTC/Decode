package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import org.firstinspires.ftc.teamcode.subsystems.SorterSys;

public class Robot {
    public static Pose2d startPose = new Pose2d(new Vector2d(0,0), Math.toRadians(90));

    public static SorterSys.MOTIF motif = SorterSys.MOTIF.NONE;

}
