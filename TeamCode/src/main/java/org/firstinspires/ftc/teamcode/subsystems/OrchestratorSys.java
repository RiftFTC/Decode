package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;

public class OrchestratorSys extends SubsystemBase {
    private final IntakeSys intake;
    private final SorterSys sorter;
    private final TurretSys turret;

    public OrchestratorSys(IntakeSys intake, SorterSys sorter, TurretSys turret) {
        this.turret = turret;
        this.intake = intake;
        this.sorter = sorter;
    }

    @Override
    public void periodic() {
        if (turret.withinLaunchZone() && !intake.isOn()) {
            sorter.setCurrentPriority(SorterSys.USAGE_PRIORITY.TURRET);
            intake.setActive(false);
        }

        if (!turret.withinLaunchZone()) {
            //set the intake to on
            intake.setActive(true);
        }

        // TODO: finish this orchestrator logic



//        if (sorter.isFull() && intake.isOn()) {
//            intake.setActive(false);
//            turret.setActive(true);
//        }
//
//        if (!sorter.isFull() && !intake.isOn() ) {
//            intake.setActive(true);
//        }
//
//        if (sorter.isEmpty() && turret.isActive()) {
//            turret.setActive(false);
//        }
//
//        if (!turret.isActive() && !sorter.isEmpty()) {
//            turret.setActive(true);
//        }
    }
}
