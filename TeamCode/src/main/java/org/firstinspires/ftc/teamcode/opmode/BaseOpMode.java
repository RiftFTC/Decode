package org.firstinspires.ftc.teamcode.opmode;

import android.annotation.SuppressLint;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.DriveSys;
import org.firstinspires.ftc.teamcode.subsystems.TurretSys;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import xyz.devmello.voyager.math.geometry.Angle;

public class BaseOpMode extends CommandOpMode {
    protected GamepadEx gamepadEx1, gamepadEx2;
    protected MotorEx fl, fr, bl, br, shooter;
    protected DriveSys driveSys;
    protected TurretSys turretSys;
    private double loopTime = 0;
    protected OpenCvCamera camera;
    public enum TEAM {
        RED,
        BLUE
    }
    public TEAM team;
    public void setTeam() {
        team = TEAM.BLUE;
    }
    @Override
    public void initialize() {
        setTeam();
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);
        initHw();
        configHw();
        initSys();
        setupMisc();
    }

    public void initHw() {
        fl = new MotorEx(hardwareMap, "fl");
        fr = new MotorEx(hardwareMap, "fr");
        bl = new MotorEx(hardwareMap, "bl");
        br = new MotorEx(hardwareMap, "br");
        shooter = new MotorEx(hardwareMap, "shooter");
    }

    public void configHw() {
        shooter.setInverted(false);
    }

    public void initSys() {
        driveSys = new DriveSys(hardwareMap);

        //turretSys = new TurretSys(hardwareMap);
    }

    @SuppressLint("SdCardPath")
    public void setupMisc() {
        driveSys.voyager.setSpeed(1);
        driveSys.voyager.setAngleTolerance(Angle.fromDeg(3));
        driveSys.voyager.setTolerance(3);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
        //camera.setPipeline(pipeline);
    }

    protected GamepadButton gb1(GamepadKeys.Button button) {
        return gamepadEx1.getGamepadButton(button);
    }

    protected GamepadButton gb2(GamepadKeys.Button button) {
        return gamepadEx2.getGamepadButton(button);
    }

    protected void tad(String caption, Object value) {
        telemetry.addData(caption, value);
    }

    @Override
    public void run() {
        super.run();
//        activityManager.getMemoryInfo(memoryInfo);
//        tad("Available Memory", (float) memoryInfo.availMem / (float) memoryInfo.totalMem * 100.0F);
        double loop = System.nanoTime();
        telemetry.addData("hz ", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
