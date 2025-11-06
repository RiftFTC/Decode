package xyz.devmello;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;
import java.util.Arrays;

public class CloseBlue {

    private static VelConstraint slowConstraint = new MinVelConstraint(Arrays.asList(

            new TranslationalVelConstraint(5),

            new AngularVelConstraint(1)

    ));

    private static VelConstraint mediumConstraint = new MinVelConstraint(Arrays.asList(

            new TranslationalVelConstraint(7.5),

            new AngularVelConstraint(1)

    ));

    private static VelConstraint maxConstraint = new MinVelConstraint(Arrays.asList(

            new TranslationalVelConstraint(40),

            new AngularVelConstraint(1)

    ));

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(1200);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(50, 50, Math.PI, Math.PI, 11.594258057297543)
                .setDimensions(17, 17)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-38.5, -55.4, Math.toRadians(180)))
                //PRELOAD

                .strafeTo(new Vector2d(-22.5, -47), mediumConstraint)
                .waitSeconds(5)
                .strafeTo(new Vector2d(-17.2, -47), slowConstraint)
                .waitSeconds(5)
                .strafeTo(new Vector2d(6, -47), mediumConstraint)
                .waitSeconds(5)
                .strafeTo(new Vector2d(30, -47), mediumConstraint)
                .waitSeconds(5)
                .strafeTo(new Vector2d(30.1, -47), maxConstraint)
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
