package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.DriveSys;
import org.firstinspires.ftc.teamcode.subsystems.TurretSys;

public class AutoBaseOpMode extends OpMode {

    MecanumDrive drive;
    protected MotorEx fl, fr, bl, br, shooter;
    protected SimpleServo turret, pitch;
    protected DriveSys driveSys;
    protected TurretSys turretSys;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        initHw();
        configHw();
        initSys();
        initMisc();
    }

    public void initHw() {
        fl = new MotorEx(hardwareMap, "fl");
        fr = new MotorEx(hardwareMap, "fr");
        bl = new MotorEx(hardwareMap, "bl");
        br = new MotorEx(hardwareMap, "br");
        shooter = new MotorEx(hardwareMap, "shooter");
        turret = new SimpleServo(hardwareMap, "turret", 0, 360);
        pitch = new SimpleServo(hardwareMap, "pitch", 0, 360);
    }

    public void configHw() {
        shooter.setInverted(false);
    }

    public void initSys() {
    }

    public void initMisc() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
    }

    protected void tad(String caption, Object value) {
        telemetry.addData(caption, value);
    }

    public void schedule(Command... commands) {
        CommandScheduler.getInstance().schedule(commands);
    }

    public void register(Subsystem... subsystems) {
        CommandScheduler.getInstance().registerSubsystem(subsystems);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
    }


    @Override
    public void stop() {
        super.stop();
        Robot.startPose = drive.localizer.getPose();
    }
}
