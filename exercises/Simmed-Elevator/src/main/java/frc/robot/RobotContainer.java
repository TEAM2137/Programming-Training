package frc.robot;


import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.*;

public class RobotContainer {
    
    Elevator elevator;

    CommandXboxController controller = new CommandXboxController(0);

    public void initialize() {
        elevator = new Elevator(controller::getLeftY);
    }

        
    public Command getAutonomousCommand() {
        return new Command() {};
    }

}
