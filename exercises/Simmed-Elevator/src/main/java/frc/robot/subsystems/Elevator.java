package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.ElevatorMotor;
import frc.robot.lib.Logger;

public class Elevator extends SubsystemBase {

    private ElevatorMotor motor;

    public Elevator(DoubleSupplier manualControlSupplier) {
        motor = new ElevatorMotor();
    }

    @Override
    public void periodic() {
        // Do not remove the following line, as it runs the simulation periodic
        motor.runSim();
    }

    public Command driveJoystickCommand(DoubleSupplier axisValue) {
        return null;    
    }

    public Command setVoltageCommand(double voltage) {
        return null;
    }

    public Command setPositionCommand(double positionMeters) {
        return null;
    }


}
