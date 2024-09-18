// chewbaccArcadeCmd-B-24                       OI.j
// Operator Interface class -- configures user controls

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class OI {

  public static double deadzone = 0.04;
  // x box control on USB port 0
  public static XboxController controller = new XboxController(0);

  // applies deadzone to joystick values, input from -1 to 1
  public static double deadDrop(double input) {
    if (Math.abs(input) < deadzone) // if input smaller than deadzone
      return 0;
    return input;
  }
// joystick Y axis needs inverting to make forward push positive
  public double getLeftY() {
    return (-controller.getRawAxis(1));
  }

  public double getRightX() { // needs inverting too, but done at later
    // point as param is sent to drive method --explained in subsys 
        return controller.getRawAxis(4);
  }

} // end OI class
