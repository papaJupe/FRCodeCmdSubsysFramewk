package frc.robot.commands;

import static frc.robot.Constant.*;
import edu.wpi.first.wpilibj.Joystick;


public class OI {

    public static double deadzone = 0.04;

    public static Joystick _driverStick = new Joystick(kDriveControlPort);
    
//  or CommandPS4Controller or CommandJoystick or
//   private final CommandXboxController m_driverController =
//   new CommandXboxController(Constant.kDriveControlPort);


  // applies deadzone to a joystick, input from -1 to 1
  public static double deadzone(double input) {
    if (deadzone > Math.abs(input)) // if input smaller than deadzone
      return 0;
    return input;
  }

  public double getLeftY() {
    return (-_driverStick.getRawAxis(1));
  }

  public double getLeftX() {
    return _driverStick.getRawAxis(0);
  }
}
