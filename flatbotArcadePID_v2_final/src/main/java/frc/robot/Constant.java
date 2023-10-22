//  flatbotArcadePID -- AM_V2              Constant.j

package frc.robot;

/**
 * Keep your magic numbers here. At the bare minimum, put the CAN IDs
 * and timeout.
 */
public class Constant {
  public static final int autoDriveInch = 48;
  public static final double kDriveInch2Tick = 10700 / (6 * Math.PI);
  public static final double kDriveTick2Inch = (6 * Math.PI) / 10700;

  public static final int TIMEOUT = 20; // timeout for all CAN command in ms

  // CAN IDs
  public static final int _masterDriveL = 3; // CAN ID of master talonL
  public static final int _slaveDriveL = 4; // CAN ID of slave talonL
  public static final int _masterDriveR = 1; // CAN ID of master talonL
  public static final int _slaveDriveR = 2; // CAN ID of slave talonL

  // // Joystick -- might be helpful to define here if different stix used
  // public static final int throttle = 1; // Y axis of left stick
  // public static final int turn = 0; // X axis of left stick
  // public static final int invertLeftY = -1; // whether or not to invert the Y
  // axis stick
  // public static final int invertLeftX = 1; // whether or not to invert the X
  // axis stick

  // Moto Control settings
  public static final boolean invertMasterL = true; // invert stick output
  public static final boolean invertMasterR = false;
  // can set to true if not reporting correct direction
  public static final boolean sensorDirection = false;

  // now PID var set in subsys
  // later: set here after final, call as static var in subsys
  // public static final double kP_pos = 0.3;
  // public static final double kI_pos = 0.00015;
  // public static final double kD_pos = 50.0;
  // public static final double kF_pos = 0.0;
  // public static final double kIzone = 1200;

}
