package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.graph.GraphManager;
import com.bylazar.graph.PanelsGraph;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class FlyWheel implements Subsystem {
    GraphManager Manager = PanelsGraph.INSTANCE.getManager();

    private FlyWheel() {
    }


    private MotorEx leftFlyWheel = new MotorEx("lfw");

    KineticState leftFlyWheelTarget = new KineticState();

    private ControlSystem leftFlyWheelControl = ControlSystem.builder()
            .velPid(0.007, 0.0, 0.0001)
            .elevatorFF(0)
            .build();

    public void setYLinear(double ty){
        double encoderClicksPerRev = 28; //what is the econder resoltion
        double target =  (encoderClicksPerRev);
        leftFlyWheelTarget = new KineticState(leftFlyWheel.getState().getVelocity() + target);
        leftFlyWheelControl.setGoal(leftFlyWheelTarget);
    }

    @Override
    public void initialize() {

    }
    @Override
    public void periodic() {

    }
}