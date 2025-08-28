import static com.rowanmcalpin.nextftc.ftc.OpModeData.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
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
        public MotorEx xLinear;

        public MotorEx yLinear;


        public PIDFController yLinearController = new PIDFController(.007, 0, 0, new StaticFeedforward(0.0));

        public PIDFController xLinearController = new PIDFController(.007, 0, 0, new StaticFeedforward(0.0));


        public Command runYLinear( double degrees){
        double clicksDegreeProportion = 1440.0/360.0;
        double clicks = clicksDegreeProportion * degrees;
        telemetry.addData("clicks: ", clicks);
        return new RunToPosition(yLinear, // MOTOR TO MOVE
                clicks, // TARGET POSITION, IN TICKS
                yLinearController, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
        }
        public Command runXLinear( double degrees){
            double clicksDegreeProportion = 1440.0/360.0;
            double clicks = clicksDegreeProportion * degrees;
            telemetry.addData("clicks: ", clicks);
            return new RunToPosition(xLinear, // MOTOR TO MOVE
                    clicks, // TARGET POSITION, IN TICKS
                    xLinearController, // CONTROLLER TO IMPLEMENT
                    this); // IMPLEMENTED SUBSYSTEM
        }

        @Override
        public void initialize () {
            xLinear = new MotorEx("xLinear");
            yLinear = new MotorEx("yLinear");
        }

        @Override
        public Command getDefaultCommand () {
            return new ParallelGroup(
                    new HoldPosition(xLinear, xLinearController, this),
                    new HoldPosition(yLinear, yLinearController, this)
                    );
        }
}