
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.graph.GraphManager;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.graph.PanelsGraph;
import com.bylazar.telemetry.PanelsTelemetry;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
@Config
public class Turret implements Subsystem {
    // BOILERPLATE


    public static double KP = .005;
    public static double KI = 0;
    public static double KD = 0;
    public static final Turret INSTANCE = new Turret();
    private Turret() { }

        // USER CODE
        public KineticState state = new KineticState(0, 0, 0);
        public MotorEx xLinear = new MotorEx("xLinear");
        public MotorEx yLinear = new MotorEx("yLinear");

        private ControlSystem yLinearControl = ControlSystem.builder()
                .posPid(KP, KI, KD)
                .elevatorFF(0)
                .build();

        public void setYLinear(double ty){
            state = new KineticState(yLinear.getCurrentPosition(), yLinear.getVelocity());
            yLinear.setPower(
                    yLinearControl.calculate(
                            state
                            )
            );
        }

        @Override
        public void initialize () {
        }
        @Override
        public void periodic(){
        }

}