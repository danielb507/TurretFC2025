import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.core.command.Command;

@Autonomous(name = "PIDTest")
public class PIDTest extends NextFTCOpMode {
    public PIDTest() {
        super(Turret.INSTANCE);
    }

    public Command firstRoutine() {
        return new SequentialGroup(
                Turret.INSTANCE.toHigh(),
                new ParallelGroup(
                        Turret.INSTANCE.toMiddle()
                ),
                new Delay(0.5),
                new ParallelGroup(
                )
        );
    }

    @Override
    public void onStartButtonPressed() {
        firstRoutine().invoke();
    }
}
