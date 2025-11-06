package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;

public class IntakeSys extends SubsystemBase {
    private MotorEx intake;
    public static double INTAKE_POWER = 0.8;
    private boolean isOn = false;

    public IntakeSys(MotorEx intake) {
        this.intake = intake;
        intake.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.FLOAT);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setActive(boolean on) {
        isOn = on;
    }


    @Override
    public void periodic() {
           if (isOn) {
               intake.set(INTAKE_POWER);
           } else {
               intake.set(0);
           }
    }
}
