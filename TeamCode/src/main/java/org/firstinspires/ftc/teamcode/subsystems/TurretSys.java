package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import xyz.devmello.voyager.Voyager;
import xyz.devmello.voyager.math.geometry.PointXY;
import xyz.devmello.voyager.math.geometry.PointXYZ;

public class TurretSys extends SubsystemBase {

    private final SimpleServo turret, pitch;
    private final MotorEx motor;
    private final Voyager drive;

    public final PointXY GOAL_POSE = new PointXY(-72, 72); //inches

    public static double TURRET_MID = 0.5; //180 degrees
    public static double TURRET_RIGHT = 0.15; //90 degrees
    public static double TURRET_LEFT = 0.85; //270 degrees

    public static double PITCH_MIN = 0.2; //20 inches
    public static double PITCH_MAX = 0.8; //200 inches

    public static double POWER_MAX = 1; //for 200 inches distance
    public static double POWER_MIN = 0.3; //for 20 inches distance

    private boolean isActive = false;

    public TurretSys(SimpleServo turret, SimpleServo pitch, MotorEx motor, Voyager drive) {
        this.turret = turret;
        this.drive = drive;
        this.pitch = pitch;
        this.motor = motor;
        turret.setPosition(TURRET_MID);
        motor.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.FLOAT);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Calculates the servo position value for the turret based on a target angle.
     * <p>
     * This method performs a linear mapping from the turret's physical angular range
     * (90 to 270 degrees) to the corresponding servo pulse width modulation (PWM)
     * values. The calculated position ensures the turret rotates to the correct
     * orientation relative to the robot's heading.
     *
     * @param targetAngle The desired angle for the turret in degrees, relative to the robot's
     * forward direction. This value is expected to be between 90 and 270 degrees.
     * @return The calculated servo position as a double, ranging from {@link #TURRET_RIGHT} to
     * {@link #TURRET_LEFT}, which corresponds to the PWM signal required to
     * position the turret at the specified angle.
     */
    public double getTargetPosition(double targetAngle) {
        return TURRET_RIGHT + (TURRET_LEFT - TURRET_RIGHT) * (targetAngle - 90) / 180;
    }

    public double getTargetPower(double distance) {
        return POWER_MIN + (POWER_MAX - POWER_MIN) * (distance - 20) / 180;
    }

    public double getPitchPosition(double distance) {
        return PITCH_MIN + (PITCH_MAX - PITCH_MIN) * (distance - 20) / 180;
    }

    @Override
    public void periodic() {
        PointXYZ position = drive.getPosition();
        double distance = position.distance(GOAL_POSE);
        //position.angleTo(GOAL_POSE).subtract(position.z()).deg() is the angle from robot to goal relative to robot forward in degrees
        //Math.max(90, Math.min(270, ...)) limits the angle to be between 90 and 270 degrees
        //getTargetPosition(...) converts the angle to servo position
        turret.setPosition(getTargetPosition(Math.max(90, Math.min(270, position.angleTo(GOAL_POSE).subtract(position.z()).deg()))));
        pitch.setPosition(getPitchPosition(distance));
        if (isActive) {
            motor.set(getTargetPower(distance));
        } else {
            motor.set(0);
        }
    }
}
