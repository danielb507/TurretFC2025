import static com.rowanmcalpin.nextftc.ftc.OpModeData.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.Controller;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

@Config
public class Turret extends Subsystem {
    // BOILERPLATE
    public static final Turret INSTANCE = new Turret();
    private Turret() { }

        // USER CODE
        public MotorEx motor;

        public static double kP = 0.05;
        public static double kI = 0;
        public static double kD = 0;

        public PIDFController controller = new PIDFController(kP, kI, kD, new StaticFeedforward(0.0));

        public String name = "motor";

        public Command run ( double clicks){
            telemetry.addData("clicks: ", clicks);
            return new RunToPosition(motor, // MOTOR TO MOVE
                    motor.getCurrentPosition() + clicks, // TARGET POSITION, IN TICKS
                    controller, // CONTROLLER TO IMPLEMENT
                    this); // IMPLEMENTED SUBSYSTEM
        }

        @Override
        public void initialize () {
            motor = new MotorEx(name);
        }

        @Override
        public Command getDefaultCommand () {
            return new HoldPosition(motor, controller, this);
        }
}