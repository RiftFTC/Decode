package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.*;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Config
public class SorterSys extends SubsystemBase {

    public enum ARTIFACT_COLOR {
        GREEN,
        PURPLE,
        EMPTY
    }

    /**
     * Positions
     * LEFT = 0
     * MIDDLE = 1
     * RIGHT = 2
     * NO_RELEASE = -1
     */

    public static enum MOTIF {
        GREEN_PURPLE_PURPLE,
        PURPLE_GREEN_PURPLE,
        PURPLE_PURPLE_GREEN,
        NONE
    }

    public enum USAGE_PRIORITY {
        TURRET,
        INTAKE1,
        INTAKE2
    }

    private USAGE_PRIORITY currentPriority = USAGE_PRIORITY.INTAKE1;

    ARTIFACT_COLOR[] motifPattern;

    //TODO: add two distance sensors to detect artifacts on each side of the intake
    //Issue URL: https://github.com/RiftFTC/Decode/issues/24
    // should have a method to return which side is better to intake from based on distance readings
    private final Rev2mDistanceSensor intakeLeft, intakeRight;
    private final RevColorSensorV3 leftC, middleC, rightC;
    private final SimpleServo spinner;

    public static double LEFT_ENTER = 0;
    public static double LEFT_ENTER1 = 0;
    public static double LEFT_EXIT = 0;
    public static double MIDDLE_ENTER = 0;
    public static double MIDDLE_ENTER1 = 0;
    public static double MIDDLE_EXIT = 0;
    public static double RIGHT_ENTER = 0;
    public static double RIGHT_ENTER1 = 0;
    public static double RIGHT_EXIT = 0;

    public ARTIFACT_COLOR[] inventory = new ARTIFACT_COLOR[] {
            ARTIFACT_COLOR.EMPTY,
            ARTIFACT_COLOR.EMPTY,
            ARTIFACT_COLOR.EMPTY
    };

    private final List<ARTIFACT_COLOR> releaseHistory = new ArrayList<>();
    private MOTIF motif = MOTIF.NONE;

    public SorterSys(SimpleServo spinner,
                     RevColorSensorV3 leftC, RevColorSensorV3 middleC, RevColorSensorV3 rightC, Rev2mDistanceSensor intakeLeft, Rev2mDistanceSensor intakeRight) {
        this.leftC = leftC;
        this.middleC = middleC;
        this.rightC = rightC;
        this.spinner = spinner;
        this.intakeLeft = intakeLeft;
        this.intakeRight = intakeRight;
    }

    private void release(int pos) {
        switch (pos) {
            case 0: {
                spinner.setPosition(LEFT_EXIT);
            }
            case 1: {
                spinner.setPosition(MIDDLE_EXIT);
            }
            case 2: {
                spinner.setPosition(RIGHT_EXIT);
            }
            case -1: {
                // Do nothing
            }
        }
    }

    public int releaseNext() {
        if (Arrays.stream(inventory).allMatch(c -> c == ARTIFACT_COLOR.EMPTY)) return -1;

        int i = IntStream.range(0, 3)
                .filter(x -> inventory[x] == motifPattern[releaseHistory.size() % 3])
                .findFirst()
                .orElseGet(() -> IntStream.range(0, 3)
                        .filter(x -> inventory[x] != ARTIFACT_COLOR.EMPTY)
                        .findFirst()
                        .orElse(-1));

        if (i != -1) releaseHistory.add(inventory[i]);
        return i;

    }

    public static MOTIF getMotifFromAprilTag(int id) {
        switch (id) {
            case 21: return MOTIF.GREEN_PURPLE_PURPLE;
            case 22: return MOTIF.PURPLE_GREEN_PURPLE;
            case 23: return MOTIF.PURPLE_PURPLE_GREEN;
            default: return MOTIF.NONE;
        }
    }

    public ARTIFACT_COLOR getColor(RevColorSensorV3 colorSensor) {
        if (colorSensor.getDistance(DistanceUnit.MM) > 50) return ARTIFACT_COLOR.EMPTY;
        return (colorSensor.green() > colorSensor.red() && colorSensor.green() > colorSensor.blue()) ? ARTIFACT_COLOR.GREEN : ARTIFACT_COLOR.PURPLE;
    }

    private ARTIFACT_COLOR[] getMotifPattern(MOTIF motif) {
        switch (motif) {
            case GREEN_PURPLE_PURPLE:
                return new ARTIFACT_COLOR[]{ARTIFACT_COLOR.GREEN, ARTIFACT_COLOR.PURPLE, ARTIFACT_COLOR.PURPLE};
            case PURPLE_GREEN_PURPLE:
                return new ARTIFACT_COLOR[]{ARTIFACT_COLOR.PURPLE, ARTIFACT_COLOR.GREEN, ARTIFACT_COLOR.PURPLE};
            case PURPLE_PURPLE_GREEN:
                return new ARTIFACT_COLOR[]{ARTIFACT_COLOR.PURPLE, ARTIFACT_COLOR.PURPLE, ARTIFACT_COLOR.GREEN};
            default:
                return new ARTIFACT_COLOR[]{};
        }
    }

    public boolean beginningNewMotifCycle() {
        int motifLength = motifPattern.length;
        int historySize = releaseHistory.size();
        if (motifLength == 0) return false;
        return historySize % motifLength == 0;
    }

    private void setMotif(MOTIF motif) {
        this.motif = motif;
        releaseHistory.clear();
        this.motifPattern = getMotifPattern(motif);
    }

    public void setCurrentPriority(USAGE_PRIORITY currentPriority) {
        this.currentPriority = currentPriority;
    }

    public MOTIF getMotif() {
        return motif;
    }

    public ARTIFACT_COLOR[] getInventory() {
        return inventory;
    }

    public boolean isFull() {
        return Arrays.stream(inventory).allMatch(c -> c != ARTIFACT_COLOR.EMPTY);
    }

    public boolean isEmpty() {
        return Arrays.stream(inventory).allMatch(c -> c == ARTIFACT_COLOR.EMPTY);
    }

    public void updateInventory() {
        inventory[0] = getColor(leftC);
        inventory[1] = getColor(middleC);
        inventory[2] = getColor(rightC);
    }

    public void getIntakeSide() {
        double leftDistance = intakeLeft.getDistance(DistanceUnit.MM);
        double rightDistance = intakeRight.getDistance(DistanceUnit.MM);

        if (leftDistance < 50 && rightDistance < 50) {
            setCurrentPriority((leftDistance < rightDistance) ? USAGE_PRIORITY.INTAKE1 : USAGE_PRIORITY.INTAKE2); // 0 for left, 1 for right
        } else if (leftDistance < 50) {
            setCurrentPriority(USAGE_PRIORITY.INTAKE1);
        } else if (rightDistance < 50) {
            setCurrentPriority(USAGE_PRIORITY.INTAKE2);
        } else {
            setCurrentPriority(USAGE_PRIORITY.INTAKE1); // default to left intake
        }
    }

    @Override
    public void periodic() {



        if(currentPriority.equals(USAGE_PRIORITY.TURRET)) {
            release(releaseNext());
        } else if (currentPriority.equals(USAGE_PRIORITY.INTAKE1)) {
            if (inventory[0] == ARTIFACT_COLOR.EMPTY) {
                spinner.setPosition(LEFT_ENTER);
            } else if (inventory[1] == ARTIFACT_COLOR.EMPTY) {
                spinner.setPosition(MIDDLE_ENTER);
            } else if (inventory[2] == ARTIFACT_COLOR.EMPTY) {
                spinner.setPosition(RIGHT_ENTER);
            }
        } else if (currentPriority.equals(USAGE_PRIORITY.INTAKE2)) {
            if (inventory[2] == ARTIFACT_COLOR.EMPTY) {
                spinner.setPosition(RIGHT_ENTER1);
            } else if (inventory[1] == ARTIFACT_COLOR.EMPTY) {
                spinner.setPosition(MIDDLE_ENTER1);
            } else if (inventory[0] == ARTIFACT_COLOR.EMPTY) {
                spinner.setPosition(LEFT_ENTER1);
            }
        }


    }
}
