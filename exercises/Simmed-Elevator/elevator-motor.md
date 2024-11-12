# ElevatorMotor Documentation

The ElevatorMotor class contains 5 useful methods. There is an addition runSim() method, but that should not be touched.

The ElevatorMotor internally contains a simulation of an elevator, including gravity. This simulation will, similar to real FRC robots, not apply any power when the robot is disabled.

## setVoltage(double volts) - void

The setVoltage method applies the given voltage to the motor. This method does not have to be continuously called; the motor will remember what voltage was last applied, and continue to apply that voltage.

## getAppliedVoltage() - returns double

This method returns the current applied voltage of the motor.

## getPositionMeters() - returns double

This method returns the current position of the elevator in meters.

## getVelocityMetersPerSecond() - returns double

This method returns the current velocity of the elevator in meters per second.

## getCurrentAmps() - returns double

This method returns the current being drawn by the motor in amps.