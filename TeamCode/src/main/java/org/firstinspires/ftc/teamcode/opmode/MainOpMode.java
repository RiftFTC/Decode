package org.firstinspires.ftc.teamcode.opmode;

import android.util.Log;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class MainOpMode extends BaseOpMode {

    @Override
    public void initialize() {
        super.initialize();
        register(driveSys);
        try {
            camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                    telemetry.addData("Camera has been initialized", true);
                    telemetry.update();
                }

                @Override
                public void onError(int errorCode) {
                    Log.e("OpenCv", "Error opening camera: " + errorCode);
                }
            });
        } catch (Exception e) {
            Log.e("OpenCv", "Error opening camera");
            Log.e("OpenCv", e.getMessage());
        }

        driveSys.setDefaultCommand(driveSys.drive(
                gamepadEx1::getLeftY,
                gamepadEx1::getLeftX,
                gamepadEx1::getRightX
        ));


    }
}
