package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import org.firstinspires.ftc.teamcode.util.math.Precision;

import java.util.function.DoubleSupplier;

public class IntakeSys extends SubsystemBase {
    private MotorEx intake;
    public static double INTAKE_POWER = 0.8;
    private boolean isOn = false;
    private boolean manual = false;

    public IntakeSys(MotorEx intake) {
        this.intake = intake;
        intake.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.FLOAT);
    }

    public Command run(DoubleSupplier p) {
        return new RunCommand(
                ()-> {
                    if(p.getAsDouble() > 0){
                        intake.set(p.getAsDouble());
                        manual = true;
                    } else {
                        manual = false;
                    }
                },this
        );
    }

    public boolean isOn() {
        return isOn;
    }

    public void setActive(boolean on) {
        isOn = on;
    }

    @Override
    public void periodic() {
           if (isOn && !manual) {
               intake.set(INTAKE_POWER);
           } else if (!isOn && !manual) {
               intake.set(0);
           }
    }
}
