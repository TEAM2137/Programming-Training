# Simulated Elevator Exercise

The simulated elevator exercise is intended to give a first experience of working with commands. It also provides some experience implemented feedforward and PID control, though this is a secondary benefit.

This exercise provides a hypothetical motor attached to an elevator mechanism. Your task is to program this mechanism, starting from simple joystick control. This motor class is documented [here](elevator-motor.md).

This exercise also provides a simple logging library, emulating what we would use on an actual robot. This is very important to output information for visualizing and debugging the mechanism. This is documented [here](logger.md).

## Instructions

### 1. Log the position of the motor

Using the logger class provided, go to the subsystem periodic and use `Logger.log()` to log the position of the elevator over time.
You will need to provide a key (name) for the log entry. Use something simple like 'position'.

<details> 
  <summary>Solution</summary>

   `Logger.log("position", motor.getPositionMeters());`
</details><br>

To get the position of the elevator, use the method in the motor object which has already been constructed.
From this, you should run the simulation and use AdvantageScope to see the output. Make sure to connect AdvantageScope before starting the simulation. 
You will have to start the simulator, connect to it via AdvantageScope (under file), then turn the simulator to disabled and drag the value over. You may have to restart the simulator to fully see the elevator move (use the green arrow symbol at the top of vscode).

You should see the elevator fall initially due to gravity. Make sure to turn the robot to 'disabled' in the simulation panel, otherwise no code will be run.

### 2. Create a joystick default command

In the provided blank method in the Elevator subsystem, find fill in the driveJoystickCommand method. This should return a command that uses the axis value supplier given to the command. To get the value, use `axisValue.getAsDouble()`.
You will want to use the motor's `setVoltage` function to set the applied voltage. Note that the motor voltage supplied ranges from -12V to 12V, while the axis value ranges from -1 to 1. You will want to scale to correct for this (simple multiplication will do).
Use the `run()` method in the subsystem to create the command. The code contained inside will run every loop (you will have to do `run(() -> code)`, as this accepts an anonymous function). This code should set the output of the motor.

<details> 
  <summary>Solution</summary>

   `return run(() -> motor.setVoltage(axisValue.getAsDouble() * -12));`
</details><br>

Once the method is complete, you must set it up as the subsystem's default command. This command will run any time there isn't another command controlling the subsystem. To do this, you must run `setDefaultCommand(command)` in the constructor of the subsystem, giving the command you want inside. So for this case, `setDefaultCommand(driveJoystickCommand(manualControlSupplier))`.

Now, try running code in the simulator. Make sure to enable the robot to teleop for this code to function. You should be able to see the elevator move up and down.

### 3. Create a set voltage command, bind to buttons

First, comment out (// at start of line) the setDefaultCommand line in the elevator constructor. This is necessary so that the following commands will work.

In the subsystem periodic method, add a line to log the applied voltage using `motor.getAppliedVoltage()`.

Using the blank method provided in the elevator subsystem, create a method that will set the voltage of the elevator. You will want to use the `runOnce()` method to create the command. Inside, put code to set the voltage of the motor, using the given voltage for the command.

<details> 
  <summary>Solution</summary>

   `return runOnce(() -> motor.setVoltage(voltage));`
</details><br>

Next, we want to bind this to buttons. Open RobotContainer.java. Inside, you will find an initialize method. Below the line of code already in there, write code to bind commands to buttons. You will do this by doing `controller.[insert button you want]().onTrue(elevator.setVoltageCommand([voltage you want]));`. For example, `controller.y().onTrue(elevator.setVoltageCommand(12));` will bind a command that set the voltage to 12V to the Y button. Do this for the A, B, X, and Y buttons, binding four separate commands: +12V, -12V, +3V, -3V.

<details> 
  <summary>Solution</summary>

   ```
   controller.y().onTrue(elevator.setVoltageCommand(12));
controller.x().onTrue(elevator.setVoltageCommand(-12));
controller.b().onTrue(elevator.setVoltageCommand(3));
controller.a().onTrue(elevator.setVoltageCommand(-3));
```
</details><br>

### 4. Create an autonomous routine

Now that we have some commands to control the elevator, let's create a simple autonomous routine. We can do this using the command we just create and wait commands. We want this routine to move the elevator up and down at different voltages for a second or two each time.

In the getAutonomousCommand method of RobotContainer, you need to return a command. Replace the code there with a line that will generate a sequence of commands to move the elevator at +3V for 2 seconds, then -3V for 1 second, then set 0V.
To do this, use the `Commands.sequence(...)` factory. Inside this, you can provide a list of commands to be run, in order. You will want to use a combination of setVoltageCommand and WaitCommand. To create the setVoltageCommands, do `elevator.setVoltageCommand(voltage)`. To create the WaitCommands, do `new WaitCommand(time)`.

<details> 
  <summary>Solution</summary>

   `return Commands.sequence(elevator.setVoltageCommand(3), new WaitCommand(2), elevator.setVoltageCommand(-3), new WaitCommand(1), elevator.setVoltageCommand(0));`
</details><br>

### 5. Setup feedforward

The next goal is to control the mechanism with PID, so that we can tell the elevator where to go and it will do that, rather than having to directly control the motor. Being able to control the end state of the mechanism is extremely helpful, as that's what the rest of the robot depends on to function properly. Feedforward is the first step towards getting position control of a mechanism, as feedforward allows the mechanism to hold a certain state (in this case, a position) accurately.

For an elevator, there are three external forces acting on it. The first force is the force of gravity, which is constantly pulling the elevator down. This force is the same no matter where the elevator is in its travel. The second force is the force of the motor, which pulls the elevator up and down. This is the force we have control over (though not directly, as we have control of the motor voltage, which doesn't correspond directly to the force). The third force is the force of static friction. Typically, there will be some friction in the system, from bearings, gears, pulleys, etc. However, this isn't modeled in the elevator simulation used, so we'll ignore it for now.

WPILib includes an ElevatorFeedforward class which helps with calculating the feedforward required on an elevator. This accepts 3 values as inputs: kS, kG, and kV. kS corresponds to the static friction - it will always be added in the direction that the elevator is moving. In this case, we'll set it to 0 since it won't be used. kG is the gravity term - it's always added in the upwards direction, since gravity is constant no matter how the elevator is moving. kV is the velocity term - if we want the elevator to be moving at a certain velocity, that velocity * kV will be added to the motor. We'll start with kV at 0 for now as well, and we'll tune this later.

To tune the kG (gravity term) of the feedforward, we need to find a voltage which can be applied to the motor where it holds whatever position it's left at well, with as little motion as possible. To do this, uncomment the line in the elevator subsystem constructor which sets the default command of the subsystem. This will allow us to modify the manual control command to have a kG term added to it. In the elevator subsystem, create a member variable (a double) called kG, and set it to 0 to start. In the manual control command, add this voltage to the voltage produced by the joystick input. This has effectively programmed the feedforward into the manual control command. From there, play around to find a value where when the elevator is left at a position in the middle of its travel (not at the top or bottom end of travel) where the elevator moves as little as possible.

<details> 
  <summary>Solution</summary>

   To create the kG variable: `private double kG = 0.0;`

   To modify the manual control command: `return run(() -> motor.setVoltage(axisValue.getAsDouble() * -12 + kG));`
</details><br>

<details> 
  <summary>Value of kG</summary>

   TBD
</details><br>


### 6. Set up PID control

Coming soon!

### 7. Create a custom trigger for automation

Coming soon!

### 8. Motion profiling

Coming soon!