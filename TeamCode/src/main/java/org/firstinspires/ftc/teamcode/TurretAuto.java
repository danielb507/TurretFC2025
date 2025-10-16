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
         follower().turnDegrees(90,true);
    }

    @Override
    public void onUpdate() {
        if(isStarted()) {

            LLResult result = limelight.getLatestResult();
            LLResult latestResult = null;
            int lastId = 0;
/*
            if (result != null) {
                if (result.isValid()) {
                    List<LLResultTypes.FiducialResult> feducialResults =  result.getFiducialResults();
                    int id = feducialResults.get(0).getFiducialId();
                    telemetry.addData("Apriltag Id:", id);
                    telemetry.addData("Tx:", feducialResults.get(0).getTargetXDegrees());
                    telemetry.update();
                    if(id != lastId){
                        follower().turnDegrees(Math.abs(feducialResults.get(0).getTargetXDegrees()), true);

                        lastId = id;
                    }

                }
            }
            
 */


        }
    }
}



