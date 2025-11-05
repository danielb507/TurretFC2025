package org.firstinspires.ftc.teamcode;




import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.graph.GraphManager;
import com.bylazar.graph.PanelsGraph;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class FlyWheel implements Subsystem {

    public static final FlyWheel INSTANCE = new FlyWheel();
    PanelsTelemetry telemetryManager = PanelsTelemetry.INSTANCE;
    GraphManager manager = PanelsGraph.INSTANCE.getManager();
    private FlyWheel() {
    }

    public MotorEx leftFlyWheel = new MotorEx("lfw");
    public MotorEx rightFlyWheel = new MotorEx("rfw");


    private ControlSystem leftFlyWheelControl = ControlSystem.builder()
            .velPid(0.008,0.0,0.001) //.008 0 0.002
            .elevatorFF(0.03)
            .build();

    private ControlSystem rightFlyWheelControl = ControlSystem.builder()
            .velPid(0.008,0.0,0.001) //.008 0 0.002
            .elevatorFF(0.03)
            .build();

    public final Command off = new RunToVelocity(leftFlyWheelControl, 0.0).requires(this).named("FlywheelOff");
    public final Command on = new RunToVelocity(leftFlyWheelControl, 1000).requires(this).named("FlywheelOff");

    public final Command off1 = new RunToVelocity(rightFlyWheelControl, 0.0).requires(this).named("FlywheelOff");
    public final Command on1 = new RunToVelocity(rightFlyWheelControl, -1000).requires(this).named("FlywheelOff");

    @Override
    public void initialize() {

    }
    @Override
    public void periodic() {

        telemetryManager.getTelemetry().addData("left state", leftFlyWheel.getState().toString());
        telemetryManager.getTelemetry().addData("right state", rightFlyWheel.getState().toString());

        manager.addData("state", leftFlyWheel.getState().getVelocity());
        manager.addData("reference", leftFlyWheelControl.getGoal().getVelocity() + 500);

        telemetryManager.getTelemetry().update();
        manager.update();

        leftFlyWheel.setPower(leftFlyWheelControl.calculate(leftFlyWheel.getState()));
        rightFlyWheel.setPower(rightFlyWheelControl.calculate(rightFlyWheel.getState()));


    }
}