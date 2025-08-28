import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

@Autonomous(name = "NextFTC Autonomous Program Java")
public class AutonomousProgram extends NextFTCOpMode {
    public AutonomousProgram() {
        super(Lift.INSTANCE);
    }

    public Command firstRoutine(double clicks) {
        return new SequentialGroup(
                Lift.INSTANCE.toMiddle(clicks)
        );
    }

    @Override
    public void onUpdate() {
        firstRoutine(500).invoke();
    }
}