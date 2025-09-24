package org.firstinspires.ftc.teamcode.roadrunner;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@I2cDeviceType
@DeviceProperties(
        name = "goBILDA® Pinpoint Odometry Computer Roadrunner Driver",
        xmlTag = "goBILDAPinpointRR",
        description = "goBILDA® Pinpoint Odometry Computer (IMU Sensor Fusion for 2 Wheel Odometry)"
)
public class GoBildaPinpointDriverRR extends GoBildaPinpointDriver {
    public float currentTicksPerMM = 0.0F;
    public static final float goBILDA_SWINGARM_POD = 13.262912F;
    public static final float goBILDA_4_BAR_POD = 19.894367F;

    public GoBildaPinpointDriverRR(I2cDeviceSynchSimple deviceClient, boolean deviceClientIsOwned) {
        super(deviceClient, deviceClientIsOwned);
    }

    public void setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods pods) {
        super.setEncoderResolution(pods);
        if (pods == GoBildaOdometryPods.goBILDA_SWINGARM_POD) {
            this.currentTicksPerMM = 13.262912F;
        }

        if (pods == GoBildaOdometryPods.goBILDA_4_BAR_POD) {
            this.currentTicksPerMM = 19.894367F;
        }

    }

    public void setEncoderResolution(double ticks_per_mm) {
        super.setEncoderResolution(ticks_per_mm);
        this.currentTicksPerMM = (float)ticks_per_mm;
    }

    public Pose2d setPosition(Pose2d pos) {
        this.setPosition(new Pose2D(DistanceUnit.INCH, pos.position.x, pos.position.y, AngleUnit.RADIANS, pos.heading.toDouble()));
        return pos;
    }

    public Pose2d getPositionRR() {
        Pose2D ftcPose = this.getPosition();
        return new Pose2d(ftcPose.getX(DistanceUnit.INCH), ftcPose.getY(DistanceUnit.INCH), ftcPose.getHeading(AngleUnit.RADIANS));
    }

    public PoseVelocity2d getVelocityRR() {
        Pose2D ftcVelocity = this.getVelocity();
        return new PoseVelocity2d(new Vector2d(ftcVelocity.getX(DistanceUnit.INCH), ftcVelocity.getY(DistanceUnit.INCH)), ftcVelocity.getHeading(AngleUnit.RADIANS));
    }
}
