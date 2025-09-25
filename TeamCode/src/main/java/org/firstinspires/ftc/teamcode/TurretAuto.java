package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Turret;

import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;

@Autonomous(name = "TurretAuto")
public class TurretAuto extends NextFTCOpMode {
    public TurretAuto() {
        addComponents(
                new SubsystemComponent(Turret.INSTANCE)
        );
    }
    Limelight3A limelight;
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
    }

    @Override
    public void onUpdate() {
        if(isStarted()) {
            LLResult result = limelight.getLatestResult();
            if (result != null) {
                if (result.isValid()) {
                    Turret.INSTANCE.setYLinear(result.getTy());
                    Turret.INSTANCE.setXLinear(result.getTx());
                }
            }
        }
    }
}



