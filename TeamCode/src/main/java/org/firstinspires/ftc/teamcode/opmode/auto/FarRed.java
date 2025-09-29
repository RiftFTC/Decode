package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.opmode.BaseOpMode;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.DriveSys;
import org.firstinspires.ftc.teamcode.subsystems.OrchestratorSys;
import org.firstinspires.ftc.teamcode.subsystems.TurretSys;
import org.firstinspires.ftc.teamcode.util.ActionCommand;
import org.firstinspires.ftc.teamcode.util.Robot;

@Autonomous(name = "Far Red", group = "Auto")
public class FarRed extends AutoBaseOpMode{

    //TODO: Implement MeepMeep auto path
    //Issue URL: https://github.com/RiftFTC/Decode/issues/11
    Action leave;

    @Override
    public void init() {
        super.init();
        Robot.startPose = new Pose2d(0,0, Math.toRadians(90));
        driveSys = new DriveSys(hardwareMap);
        turretSys = new TurretSys(turret, pitch, shooter, driveSys.voyager, BaseOpMode.TEAM.RED);
        orchestratorSys = new OrchestratorSys(intakeSys, sorterSys, turretSys);
        register(driveSys, turretSys, intakeSys, sorterSys, orchestratorSys);
        telemetry.addData("Auto", "Far Red");
        telemetry.update();

        leave = driveSys.drive.actionBuilder(new Pose2d(0,0, Math.toRadians(90)))
                .lineToY(-10)
                .build();

        schedule(
                new SequentialCommandGroup(
                        new ActionCommand(leave)
                )
        );
    }
}
