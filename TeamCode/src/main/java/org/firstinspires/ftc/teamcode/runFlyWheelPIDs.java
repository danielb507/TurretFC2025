package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static java.lang.Math.abs;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.core.units.Angle;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.extensions.pedro.TurnBy;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.FlyWheel;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import static dev.nextftc.bindings.Bindings.*;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
@Configurable
@TeleOp(name = "runFlyWheelPIDs")

public class runFlyWheelPIDs extends NextFTCOpMode {


    public runFlyWheelPIDs() {
        addComponents(
                new SubsystemComponent(FlyWheel.INSTANCE)
        );
    }

    @Override
    public void onStartButtonPressed() {
    }

    @Override
    public void onUpdate() {
        FlyWheel.INSTANCE.on.schedule();
        //FlyWheel.INSTANCE.on1.schedule();
        telemetry.update();
    }

    @Override
    public void onInit() {
    }

    @Override
    public void onStop() {
    }
}
