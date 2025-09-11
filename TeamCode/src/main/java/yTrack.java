import com.bylazar.graph.GraphManager;
import com.bylazar.graph.PanelsGraph;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import dev.nextftc.ftc.NextFTCOpMode;
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
@Autonomous(name = "yTrack")
public class yTrack extends NextFTCOpMode {
    Limelight3A limelight;

    public static double KP = .005;
    public static double KI = 0;
    public static double KD = 0;
    ControlSystem yLinearControl;
    MotorEx yLinear;

    @Override
    public void onInit() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        yLinear = new MotorEx("yLinear");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(0); // Switch to pipeline number 0

        ControlSystem yLinearControl = ControlSystem.builder()
                .posPid(KP, KI, KD)
                .elevatorFF(0)
                .build();

    }

    @Override
    public void onUpdate() {
        LLResult result = limelight.getLatestResult();

        if (result != null) {
            if (result.isValid()) {
                yLinearControl.setGoal(new KineticState(result.getTy()));
                telemetry.addData("ty", result.getTy());
            }
        }
    }

}
