package org.firstinspires.ftc.teamcode;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.graph.GraphManager;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.graph.PanelsGraph;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToState;
import dev.nextftc.hardware.impl.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Configurable
public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();
    GraphManager Manager = PanelsGraph.INSTANCE.getManager();

    private Turret() { }


    private MotorEx yLinear = new MotorEx("ylinear");

    KineticState targetStateY = new KineticState();
    KineticState targetStateX = new KineticState();

    private ControlSystem yLinearControl = ControlSystem.builder()
            .posPid(0.007, 0.0, 0.0001)
            .elevatorFF(0)
            .build();

    private ControlSystem xLinearControl = ControlSystem.builder()
            .posPid(0.004, 0.0, 0.0001)
            .elevatorFF(0)
            .build();

    public void setYLinear(double ty){
        double encoderClicksPerRev = 1440d / 360d;
        double target =  (encoderClicksPerRev * ty);
        targetStateY = new KineticState(yLinear.getState().getPosition() + target);
        yLinearControl.setGoal(targetStateY);
    }

    @Override
    public void initialize(){
    }
    @Override
    public void periodic() {
        if(TurretAuto.isStarted) {
            yLinear.setPower(yLinearControl.calculate(yLinear.getState()));

            Manager.addData("Goal", xLinearControl.getGoal().getPosition());
            Manager.update();
        }
    }
}