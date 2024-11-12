package frc.robot.lib;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Logger {

    static NetworkTable table = NetworkTableInstance.getDefault().getTable("logger");
    
    public static void log(String key, double value) {
        table.getEntry(key).setDouble(value);
    }

    public static void log(String key, boolean value) {
        table.getEntry(key).setBoolean(value);
    }
}
