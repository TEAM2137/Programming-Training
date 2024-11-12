package frc.robot.lib;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;

public class ElevatorMotor {
    
    private ElevatorSim sim = new ElevatorSim(DCMotor.getKrakenX60(1), 5, 7, 
                    Units.inchesToMeters(0.75), 0, Units.inchesToMeters(60), true, Units.inchesToMeters(10));

    private double appliedVoltage = 0;

    public ElevatorMotor() {

    }

    public void runSim() {
        if (DriverStation.isDisabled()) {
            appliedVoltage = 0;
        }

        sim.setInputVoltage(appliedVoltage);
        sim.update(1.0/50);
    }

    public void setVoltage(double voltage) {
        appliedVoltage = MathUtil.clamp(voltage, -12, 12);
    }

    public double getPositionMeters() {
        return sim.getPositionMeters();
    }

    public double getVelocityMetersPerSecond() {
        return sim.getVelocityMetersPerSecond();
    }

    public double getCurrentAmps() {
        return sim.getCurrentDrawAmps();
    }

    public double getAppliedVoltage() {
        return appliedVoltage;
    }

}
