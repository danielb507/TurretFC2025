package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static dev.nextftc.bindings.Bindings.button;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.List;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

@TeleOp(name = "TeleOpTwoController")

public class TeleopTwoController extends NextFTCOpMode {

    CRServoEx intake = new CRServoEx("intake");
    ServoEx leftRelease = new ServoEx("lbar");
    ServoEx rightRelease = new ServoEx("rbar");
    ServoEx triggerServo = new ServoEx("launch");
    MotorEx rightFlyWheel = new MotorEx("rfw");
    MotorEx leftFlyWheel = new MotorEx("lfw");

    LLResultTypes.FiducialResult lastResult = null;

    boolean running = true;

    Button leftReleaseButton = button(() -> gamepad2.dpad_left);
    Button rightReleaseButton = button(() -> gamepad2.dpad_right);
    Button triggerButton = button(() -> gamepad2.y);

    DriverControlledCommand driverControlled = new PedroDriverControlled(
            Gamepads.gamepad1().leftStickY().negate(),
            Gamepads.gamepad1().leftStickX().negate(),
            Gamepads.gamepad1().rightStickX().negate(),
            false
    );

    public TeleopTwoController() {
        addComponents(
                new SubsystemComponent(Turret.INSTANCE),
                new SubsystemComponent(FlyWheel.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE

        );
    }
//finsihee


    @Override
    public void onStartButtonPressed() {

        driverControlled.schedule();

        button(() -> gamepad1.dpad_down)
                .whenTrue(() -> lockOn())
                .whenBecomesFalse(() -> lockOff());

        button(() -> gamepad2.b)
                .toggleOnBecomesTrue()
                .whenBecomesTrue(() -> runFlyWheel())
                .whenBecomesFalse(() -> stopFlyWheel());
        rightReleaseButton.whenBecomesTrue(() -> rightRelease.setPosition(0));
        rightReleaseButton.whenBecomesFalse(() -> rightRelease.setPosition(0.4));
        leftReleaseButton.whenBecomesTrue(() -> leftRelease.setPosition(0.8));
        leftReleaseButton.whenBecomesFalse(() -> leftRelease.setPosition(0.3));
        triggerButton.whenTrue(() -> triggerServo.setPosition(.4));
        triggerButton.whenFalse(() -> triggerServo.setPosition(.7));
        button(() -> gamepad1.left_bumper)
                .toggleOnBecomesTrue()
                .whenBecomesTrue(() -> intake.setPower(-1))
                .whenBecomesFalse(() -> intake.setPower(0));
    }

    @Override
    public void onUpdate() {
       BindingManager.update();
    }

    public static Limelight3A limelight = null;

    @Override
    public void onInit() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(0); // Switch to pipeline number 0
        FlyWheel.INSTANCE.off.schedule();
    }

    @Override
    public void onStop() {
        BindingManager.reset();
    }

    Runnable runFlyWheel(){
        FlyWheel.INSTANCE.on.schedule();
        //FlyWheel.INSTANCE.on1.schedule();
        return null;
    }
    Runnable stopFlyWheel(){
        FlyWheel.INSTANCE.off.schedule();
        //FlyWheel.INSTANCE.off1.schedule();
        return null;
    }
    public void lockOn(){
            LLResult result = TeleopTwoController.limelight.getLatestResult();
            if (result != null) {

                if (result.isValid()) {
                    List<LLResultTypes.FiducialResult> feducialResults = result.getFiducialResults();
                    //telemetry.addData("Tx:", feducialResults.get(0).getTargetXDegrees());
                    lastResult = feducialResults.get(0);

                    if (lastResult != null) {

                        if (lastResult.getTargetXDegrees() < 0) {
                            follower().turnDegrees(abs(lastResult.getTargetXDegrees()), true);
                        }
                        else if (lastResult.getTargetXDegrees() > 0) {
                            follower().turnDegrees(abs(lastResult.getTargetXDegrees()), false);
                        }
                        //telemetry.addData("last tx:",lastResult.getTargetXDegrees());

                    }
                }
            }


        //telemetry.update();

    }
    public void lockOff(){
        running = false;
        follower().breakFollowing();
        driverControlled.start();

    }
    /*
    Runnable rightReleaseLift(){
        rightRelease.setPosition(0);
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
               rightRelease.setPosition(.4);
            }
        }, 4000 );
        return null;
    }
    Runnable leftReleaseLift(){
        leftRelease.setPosition(0);
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                leftRelease.setPosition(.4);
            }
        }, 4000 );
        return null;
    }
    if(gamepad1.a){
                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }

            if(gamepad1.dpad_right){
                rbar.setPosition(0);
                currentTime = runtime.seconds();
            } if(runtime.seconds() >= currentTime + 3){
                rbar.setPosition(.4);
            }

            if(gamepad1.dpad_left){
                lbar.setPosition(1);
                currentTime1 = runtime.seconds();
            } if(runtime.seconds() >= currentTime1 + 3){
                lbar.setPosition(.3);
            }

            if (gamepad1.y){
                launch.setPosition(.4);
            } else {
                launch.setPosition(.7);
            }

            if (gamepad1.b){
                lfw.setPower(.5);
                rfw.setPower(.5);
            } else {
                lfw.setPower(0);
                rfw.setPower(0);
            }

     */

}
