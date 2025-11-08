package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import java.util.Timer;

@Autonomous(name = "closeBlue")

public class closeBlue extends NextFTCOpMode {
    CRServoEx intake = new CRServoEx("intake");
    ServoEx leftRelease = new ServoEx("lbar");
    ServoEx rightRelease = new ServoEx("rbar");
    ServoEx triggerServo = new ServoEx("launch");
    MotorEx rightFlyWheel = new MotorEx("rfw");
    MotorEx leftFlyWheel = new MotorEx("lfw");

    LLResultTypes.FiducialResult lastResult = null;

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    private final Pose startPose = new Pose(22, 123, Math.toRadians(144));
    private final Pose launchPose = new Pose(37, 95, Math.toRadians(-52));
    private final Pose parkPose = new Pose(37,120, Math.toRadians(-52));

    public PathChain driveForward, launchPath, parkPath;

    public void buildPaths() {

        launchPath = follower().pathBuilder()
                .addPath(new BezierLine(startPose, launchPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading())
                .build();
        parkPath = follower().pathBuilder()
                .addPath(new BezierLine(launchPose, parkPose))
                .setLinearHeadingInterpolation(launchPose.getHeading(), parkPose.getHeading())
                .build();
    }

    public Command launchBall = new SetPosition(triggerServo, .4);
    public Command theDownies = new SetPosition(triggerServo, .7);
    public Command rightGateOpen = new SetPosition(rightRelease, 0);
    public Command rightGateClose = new SetPosition(rightRelease, .4);
    public Command leftGateOpen = new SetPosition(leftRelease, .8);
    public Command leftGateClose = new SetPosition(leftRelease, .3);
    public Command runIntake = new SetPower(intake, -1);

    public closeBlue() {
        addComponents(
                new SubsystemComponent(Turret.INSTANCE),
                new SubsystemComponent(FlyWheel.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(0); // Switch to pipeline number 0

        buildPaths();
        follower().setStartingPose(startPose);
    }

    private Command autonomousRoutine() {
        return new ParallelGroup(
                FlyWheel.INSTANCE.autoOn,
                rightGateClose,
                leftGateClose,
                new SequentialGroup(
                        new FollowPath(launchPath),
                        launchBall,
                        new Delay(.5),
                        runIntake,
                        theDownies, //Henry don't hate us please
                        rightGateOpen,
                        new Delay(3),
                        launchBall,
                        new Delay(.5),
                        theDownies, //Henry don't hate us please
                        leftGateOpen,
                        new Delay(3),
                        launchBall,
                        new Delay(.5),
                        theDownies, //Henry don't hate us please
                        new FollowPath(parkPath)
                )
        );
    }

    @Override
    public void onStartButtonPressed() {
        autonomousRoutine().schedule();
        //follower().followPath(launchPath);
        //FlyWheel.INSTANCE.launchOn(1500);
        //triggerServo.setPosition(.4);
        //sleep(500);
        //triggerServo.setPosition(.7);
    }


    @Override
    public void onUpdate() {

    }

    public static Limelight3A limelight = null;

    @Override
    public void onStop() {

    }


}
