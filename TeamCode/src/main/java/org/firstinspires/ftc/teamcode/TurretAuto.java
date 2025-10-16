package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Turret;

import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.core.units.Angle;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.TurnBy;
import dev.nextftc.extensions.pedro.TurnTo;
import dev.nextftc.ftc.NextFTCOpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "TurretAuto")
public class TurretAuto extends NextFTCOpMode {
    public TurretAuto() {
        addComponents(
                new SubsystemComponent(Turret.INSTANCE),
                new PedroComponent(Constants::createFollower)
        );
    }

    public static Limelight3A limelight;
    public static boolean isStarted = false;
    int id = 0;
    LLResultTypes.FiducialResult lastResult = null;
    int lastId = 0;
    double motorTarget = 0;
    @Override
    public void onInit() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(0); // Switch to pipeline number 0


    }
    @Override
    public void onStartButtonPressed(){
         isStarted = isStarted();
    }

    @Override
    public void onUpdate() {
        if(isStarted()) {

            LLResult result = limelight.getLatestResult();


            if (result != null) {
                if (result.isValid()) {
                    List<LLResultTypes.FiducialResult> feducialResults =  result.getFiducialResults();
                    telemetry.addData("Apriltag Id:", id);
                    telemetry.addData("Tx:", feducialResults.get(0).getTargetXDegrees());

                    id = feducialResults.get(0).getFiducialId();
                    if(id != lastId) {
                        lastResult = feducialResults.get(0);

                        if (lastResult != null){
                            if(lastResult.getTargetXDegrees() < 0){
                                follower().turnDegrees(Math.abs(lastResult.getTargetXDegrees()), true);
                            }
                            else{
                                follower().turnDegrees(Math.abs(lastResult.getTargetXDegrees()), false);
                            }
                            telemetry.addData("last tx:",lastResult.getTargetXDegrees());

                        }
                        lastId = id;
                    }
                }
            }




            telemetry.update();

        }
    }
}



