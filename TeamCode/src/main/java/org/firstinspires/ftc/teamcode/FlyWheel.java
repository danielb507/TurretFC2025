package org.firstinspires.ftc.teamcode;




import com.acmerobotics.dashboard.config.Config;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
@Config
public class FlyWheel implements Subsystem {
    public static double P = 0.0;
    public static double I = 0.0;
    public static double D = 0.0;

    public static final FlyWheel INSTANCE = new FlyWheel();
    public KineticState LeftFlyWheelTarget = new KineticState();
    private FlyWheel() {
    }


    public MotorEx leftFlyWheel = new MotorEx("lfw");

    KineticState leftFlyWheelTarget = new KineticState();

    private ControlSystem leftFlyWheelControl = ControlSystem.builder()
            .velPid(P, I, D)
            .elevatorFF(0)
            .build();

    public void setLeftFlyWheel(double targetSpeed){
        double target = targetSpeed;
        leftFlyWheelTarget = new KineticState(0, target);
        leftFlyWheelControl.setGoal(leftFlyWheelTarget);
    }

    @Override
    public void initialize() {

    }
    @Override
    public void periodic() {

        leftFlyWheel.setPower(leftFlyWheelControl.calculate());
    }
}