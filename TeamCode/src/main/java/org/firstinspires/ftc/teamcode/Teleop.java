package org.firstinspires.ftc.teamcode;

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
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import static dev.nextftc.bindings.Bindings.*;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "NextFTC TeleOp Program Java")

public class Teleop extends NextFTCOpMode {

    CRServoEx intake = new CRServoEx("intake");
    ServoEx leftRelease = new ServoEx("lbar");
    ServoEx rightRelease = new ServoEx("rbar");
    ServoEx triggerServo = new ServoEx("launch");
    MotorEx rightFlyWheel = new MotorEx("rfw");
    MotorEx leftFlyWheel = new MotorEx("lfw");



    Button targetButton = button(() -> gamepad1.dpad_down);
    Button runFlyWheelButton = button(() -> gamepad1.b);
    Button intakeButton = button(() -> gamepad1.a);
    Button leftReleaseButton = button(() -> gamepad1.dpad_left);
    Button rightReleaseButton = button(() -> gamepad1.dpad_right);
    Button triggerButton = button(() -> gamepad1.y);
    double timeLastReleasedLeft = 0.0;
    double timeLastReleasedRight = 0.0;



    public Teleop() {
        addComponents(
                new SubsystemComponent(Turret.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE

        );
    }
//finsihee


    @Override
    public void onStartButtonPressed() {
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX().negate(),
                Gamepads.gamepad1().rightStickX().negate(),
                false
        );
        driverControlled.schedule();

        targetButton.whenBecomesTrue(() -> Turret.INSTANCE.lockOn());
        runFlyWheelButton.whenTrue(() -> runFlyWheel());
        rightReleaseButton.whenBecomesTrue(() -> rightReleaseLift());
        leftReleaseButton.whenBecomesTrue(() -> leftReleaseLift());
        triggerButton.whenTrue(() -> triggerServo.setPosition(.4));
        triggerButton.whenFalse(() -> triggerServo.setPosition(.7));
        intakeButton.whenTrue(() -> intake.setPower(-1));
        intakeButton.whenFalse(() -> intake.setPower(0));
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
    }

    @Override
    public void onStop() {
        BindingManager.reset();
    }

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
    Runnable runFlyWheel(){
        leftFlyWheel.setPower(1);
        rightFlyWheel.setPower(-1);
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
    /*if(gamepad1.a){
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
