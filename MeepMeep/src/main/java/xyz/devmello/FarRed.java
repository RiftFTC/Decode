package xyz.devmello;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;
import java.util.Arrays;

public class FarRed {

    private static VelConstraint slowConstraint = new MinVelConstraint(Arrays.asList(

            new TranslationalVelConstraint(5),

            new AngularVelConstraint(1)

    ));

    private static VelConstraint mediumConstraint = new MinVelConstraint(Arrays.asList(

            new TranslationalVelConstraint(15),

            new AngularVelConstraint(1)

    ));

    private static VelConstraint maxConstraint = new MinVelConstraint(Arrays.asList(

            new TranslationalVelConstraint(60),

            new AngularVelConstraint(1)

    ));

    public static void main(String[] args) {
        //TODO: (Far Red) Fix Auto Path to shoot from launch zone
        //Issue URL: https://github.com/RiftFTC/Decode/issues/21
        MeepMeep meepMeep = new MeepMeep(800);



        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(50, 50, Math.PI, Math.PI, 11.594258057297543)
                .setDimensions(17, 17)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(62, 14.75, Math.toRadians(180)))
                //PRELOAD

                .strafeTo(new Vector2d(24, 30), maxConstraint)
                .strafeTo(new Vector2d(24, 47))
                .strafeTo(new Vector2d(30, 47), slowConstraint)
                .waitSeconds(5)
                .strafeTo(new Vector2d(23,30), maxConstraint)
                .strafeTo(new Vector2d(1,30), maxConstraint)
                .strafeTo(new Vector2d(1,47))
                .strafeTo(new Vector2d(6, 47), slowConstraint)
                .waitSeconds(3)
                .strafeToLinearHeading(new Vector2d(62, 56), Math.toRadians(270), mediumConstraint)
                .strafeTo(new Vector2d(62, 61), slowConstraint)
                        .waitSeconds(3)
                .strafeToLinearHeading(new Vector2d(2, 47.6), Math.toRadians(180), mediumConstraint)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
