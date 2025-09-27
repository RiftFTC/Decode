package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AutoBaseOpMode extends OpMode {

    protected MotorEx fl, fr, bl, br, shooter, intake;
    protected SimpleServo turret, pitch, left, middle, right;
    protected RevColorSensorV3 leftC, middleC, rightC;
    protected DriveSys driveSys;
    protected TurretSys turretSys;
    protected IntakeSys intakeSys;
    protected SorterSys sorterSys;
    protected OrchestratorSys orchestratorSys;


    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        initHw();
        configHw();
        initSys();
        initMisc();

    }

    @Override
    public void init_loop() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        if (!currentDetections.isEmpty()) {
            for (AprilTagDetection tag : currentDetections) {
                Robot.motif = SorterSys.getMotifFromAprilTag(tag.id);
                tad("Tag ID", tag.id);
                tad("Motif", Robot.motif);
            }
        }
    }

    public void initHw() {
        fl = new MotorEx(hardwareMap, "fl");
        fr = new MotorEx(hardwareMap, "fr");
        bl = new MotorEx(hardwareMap, "bl");
        br = new MotorEx(hardwareMap, "br");
        shooter = new MotorEx(hardwareMap, "shooter");
        intake = new MotorEx(hardwareMap, "intake");
        turret = new SimpleServo(hardwareMap, "turret", 0, 360);
        pitch = new SimpleServo(hardwareMap, "pitch", 0, 360);
        left = new SimpleServo(hardwareMap, "left", 0, 360);
        middle = new SimpleServo(hardwareMap, "middle", 0, 360);
        right = new SimpleServo(hardwareMap, "right", 0, 360);
        leftC = hardwareMap.get(RevColorSensorV3.class, "leftC");
        middleC = hardwareMap.get(RevColorSensorV3.class, "middleC");
        rightC = hardwareMap.get(RevColorSensorV3.class, "rightC");
    }

    public void configHw() {
        shooter.setInverted(false);
    }

    public void initSys() {
        intakeSys = new IntakeSys(intake);
        sorterSys = new SorterSys(left, middle, right, leftC, middleC, rightC);
    }

    public void initMisc() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        aprilTag = new AprilTagProcessor.Builder()
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(webcamName);
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
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
        Robot.startPose = driveSys.drive.localizer.getPose();
    }
}
