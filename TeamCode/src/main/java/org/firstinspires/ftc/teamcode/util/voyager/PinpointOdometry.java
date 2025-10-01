package org.firstinspires.ftc.teamcode.util.voyager;

import com.acmerobotics.roadrunner.Pose2d;
import org.firstinspires.ftc.teamcode.roadrunner.Localizer;
import xyz.devmello.voyager.math.geometry.PointXYZ;
import xyz.devmello.voyager.robot.AbstractOdometry;

public class PinpointOdometry extends AbstractOdometry {
    private final Localizer driver;
    public PinpointOdometry(Localizer localizer) {
        driver = localizer;
    }

    @Override
    public void tick() {
        driver.update();
    }

    @Override
    public PointXYZ getRawPosition() {
        Pose2d pos = driver.getPose();
        return new PointXYZ(pos.position.x, pos.position.y, pos.heading.toDouble());
    }

    @Override
    public void setOffset(PointXYZ pointXYZ) {
        driver.setPose(new Pose2d(pointXYZ.x(), pointXYZ.y(), pointXYZ.z().rad()));
    }

}
