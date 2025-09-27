package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.*;
import com.arcrobotics.ftclib.hardware.SimpleServo;
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

    public enum ARTIFACT_POSITION {
        LEFT(0),
        MIDDLE(1),
        RIGHT(2);

        public final int index;
        ARTIFACT_POSITION(int index) {
            this.index = index;
        }
    }

    public static enum MOTIF {
        GREEN_PURPLE_PURPLE,
        PURPLE_GREEN_PURPLE,
        PURPLE_PURPLE_GREEN,
        NONE
    }

    ARTIFACT_COLOR[] motifPattern;

    private final RevColorSensorV3 leftC, middleC, rightC;
    private final SimpleServo left,middle,right;

    public static double LEFT_RELEASE = 0;
    public static double MIDDLE_RELEASE = 0;
    public static double RIGHT_RELEASE = 0;
    public static double LEFT_HOLD = 0.5;
    public static double MIDDLE_HOLD = 0.5;
    public static double RIGHT_HOLD = 0.5;

    public ARTIFACT_COLOR[] inventory = new ARTIFACT_COLOR[] {
            ARTIFACT_COLOR.EMPTY,
            ARTIFACT_COLOR.EMPTY,
            ARTIFACT_COLOR.EMPTY
    };

    private final List<ARTIFACT_COLOR> releaseHistory = new ArrayList<>();
    private MOTIF motif = MOTIF.NONE;

    public SorterSys(SimpleServo left, SimpleServo middle, SimpleServo right,
                     RevColorSensorV3 leftC, RevColorSensorV3 middleC, RevColorSensorV3 rightC) {
        this.leftC = leftC;
        this.middleC = middleC;
        this.rightC = rightC;
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public Command load() {
        return new InstantCommand(() -> {
            int pos = releaseNext();
            switch (pos) {
                case 0: CommandScheduler.getInstance().schedule(releaseCommand(ARTIFACT_POSITION.LEFT));
                case 1: CommandScheduler.getInstance().schedule(releaseCommand(ARTIFACT_POSITION.MIDDLE));
                case 2: CommandScheduler.getInstance().schedule(releaseCommand(ARTIFACT_POSITION.RIGHT));
            }
        }, this);
    }

    private Command releaseCommand(ARTIFACT_POSITION position) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> release(position), this),
                new WaitCommand(500),
                new InstantCommand(() -> hold(position), this)
        );
    }

    private void release(ARTIFACT_POSITION pos) {
        switch (pos) {
            case LEFT: {
                left.setPosition(LEFT_RELEASE);
                inventory[0] = ARTIFACT_COLOR.EMPTY;
            }
            case MIDDLE: {
                middle.setPosition(MIDDLE_RELEASE);
                inventory[1] = ARTIFACT_COLOR.EMPTY;
            }
            case RIGHT: {
                right.setPosition(RIGHT_RELEASE);
                inventory[2] = ARTIFACT_COLOR.EMPTY;
            }
        }
    }

    private void hold(ARTIFACT_POSITION pos) {
        switch (pos) {
            case LEFT: left.setPosition(LEFT_HOLD);
            case MIDDLE: middle.setPosition(MIDDLE_HOLD);
            case RIGHT: right.setPosition(RIGHT_HOLD);
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

    @Override
    public void periodic() {
        inventory[0] = getColor(leftC);
        inventory[1] = getColor(middleC);
        inventory[2] = getColor(rightC);
    }
}
