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
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class FlyWheel implements Subsystem {

    public static final FlyWheel INSTANCE = new FlyWheel();
    PIDCoefficients flyWheelCoeffs = new PIDCoefficients(.05, 0, 0);
    PanelsTelemetry telemetryManager = PanelsTelemetry.INSTANCE;
    GraphManager manager = PanelsGraph.INSTANCE.getManager();
    private FlyWheel() {
    }

    public MotorEx leftFlyWheel = new MotorEx("lfw");

    KineticState leftFlyWheelTarget = new KineticState();

    private ControlSystem leftFlyWheelControl = ControlSystem.builder()
            .velPid(flyWheelCoeffs)
            .build();

    public final Command off = new RunToVelocity(leftFlyWheelControl, 0.0).requires(this).named("FlywheelOff");
    public final Command on = new RunToVelocity(leftFlyWheelControl, 500.0).requires(this).named("FlywheelOff");

    @Override
    public void initialize() {

    }
    @Override
    public void periodic() {

        telemetryManager.getTelemetry().addData("state", leftFlyWheel.getState().toString());

        manager.addData("state", leftFlyWheel.getState().getVelocity());
        manager.addData("reference", leftFlyWheelControl.getGoal().getVelocity());

        telemetryManager.getTelemetry().update();

        leftFlyWheel.setPower(leftFlyWheelControl.calculate(leftFlyWheel.getState()));


    }
}