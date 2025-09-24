package org.firstinspires.ftc.teamcode.roadrunner;

import android.util.Log;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class PinpointEncoder implements Encoder {
    private GoBildaPinpointDriverRR pinpoint;
    private boolean usePerpendicular;
    private DcMotor anyDummyMotor;

    public PinpointEncoder(
            GoBildaPinpointDriverRR pinpoint,
            boolean usePerpendicular,
            DcMotor anyDummyMotor
    ) {
        this.pinpoint = pinpoint;
        this.usePerpendicular = usePerpendicular;
        this.anyDummyMotor = anyDummyMotor;

        Log.println(Log.INFO, "PinpointEncoder", "init: Initializing pinpoint encoder in tuning mode");
        Log.println(Log.INFO, "PinpointEncoder", "init: Old yaw scalar = " + pinpoint.getYawScalar());
        Log.println(Log.WARN, "PinpointEncoder", "init: Setting Pinpoint yaw scalar to 0. Perform power cycle to reset");
        RobotLog.addGlobalWarningMessage(
                "Setting Pinpoint yaw scalar to 0 for tuning purposes (previously %f)." +
                        " Perform a power cycle to reset it before running Feedback Tuner",
                pinpoint.getYawScalar()
        );
        // Makes the output from the pinpoint robot centric
        // Officially recommended by Gobilda:
        // https://discord.com/channels/225450307654647808/225451520911605765/1286457799798296669
        pinpoint.setYawScalar(0.0);
        pinpoint.resetPosAndIMU();
    }

    @Override
    public DcMotorSimple.Direction getDirection() {
        return DcMotorSimple.Direction.FORWARD;
    }

    @Override
    public void setDirection(DcMotorSimple.Direction direction) {
        // Not used, but required by the interface
    }

    @Override
    public PositionVelocityPair getPositionAndVelocity() {
        // this will run twice when accessing both directions, which isn't ideal for loop times
        // however all tuners only use it once so it's fine
        pinpoint.update();
        double pos;
        double vel;

        if (usePerpendicular) {
            // y = strafe = perpendicular
            pos = pinpoint.getPositionRR().position.y;
            vel = pinpoint.getVelocityRR().linearVel.y;
        } else {
            pos = pinpoint.getPositionRR().position.x;
            vel = pinpoint.getVelocityRR().linearVel.x;
        }
        return new PositionVelocityPair(pos, vel, pos, vel);
    }

    @Override
    public DcMotorController getController() { // I hate this
        return anyDummyMotor.getController();
    }
}
