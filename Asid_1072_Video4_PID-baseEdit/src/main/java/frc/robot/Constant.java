/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Keep all the magic numbers here. At the bare minimum, put the CAN IDs and wiring stuff here.
 */
public class Constant {
// most not used, but might be in future
  public static final int TIMEOUT = 10; // timeout for all CAN commands in ms
  
    // CAN IDs
    public static final int _masterDriveL = 4; // CAN ID of master talon
    public static final int _slaveDriveL = 3; // CAN ID of slave talon

    // Joystick
    public static final int throttle = 1; // Y axis of left stick
    public static final int turn = 0; // X axis of left stick
    public static final int invertLeftY = -1; // whether or not to invert the Y axis stick
    public static final int invertLeftX = 1; // whether or not to invert the X axis stick

    // Controller settings
    public static final boolean invertMaster = false; // invert the output
    public static final boolean invertSlave = false; // invert the output

    // PID settings
    public static final boolean sensorDirection = false;

    public static final double kP_pos = 3.0;
    public static final double kI_pos = 0.01;
    public static final double kD_pos = 45;

    public static final int integral_zone = 1;
  

}
