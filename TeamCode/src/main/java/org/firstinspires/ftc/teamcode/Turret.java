package org.firstinspires.ftc.teamcode;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.graph.GraphManager;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.graph.PanelsGraph;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToState;
import dev.nextftc.hardware.impl.MotorEx;

public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();
    private Turret() { }

    private MotorEx yLinear = new MotorEx("yLinear");

    KineticState targetState = new KineticState();

    private ControlSystem controlSystem = ControlSystem.builder()
            .posPid(0.005, 0, 0)
            .elevatorFF(0)
            .build();

    public void setYLinear(double ty){
        double encoderClicksPerRev = 576d;
        double target =  encoderClicksPerRev / ty;
        targetState = new KineticState(yLinear.getState().getPosition() + target);
        controlSystem.setGoal(targetState);
    }

    @Override
    public void periodic() {
        yLinear.setPower(controlSystem.calculate(yLinear.getState()));
        LLResult result = TurretAuto.limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                setYLinear(result.getTy());
            }
        }
    }
}