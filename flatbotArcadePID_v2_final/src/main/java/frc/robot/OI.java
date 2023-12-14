// flatbotArcadePID v2   OperatorInput   aka   OI.j 
 
package frc.robot;

// import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.*;
//import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.*;

/**
 * OI class is the glue that binds controls on the physical Operator
 * Interface to the commands and command groups that control the robot
 */
public class OI {
  // private static OI oi; used before with getInstance, not now

  public static double deadzone = 0.04;

  public static XboxController stick = new XboxController(0);;

  // CONSTRUCTOR
  public OI() {
    // instead of Trigger, clearer to use BoolSupplier JB, interface to it

    final JoystickButton 
        button1 = new JoystickButton(stick, 1),
        button2 = new JoystickButton(stick, 2),
        button3 = new JoystickButton(stick, 3);
        // button4 = new JoystickButton(stick, 4),
        // button5 = new JoystickButton(stick, 5),
        // button6 = new JoystickButton(stick, 6);

    // Trigger button1 = new JoystickButton(stick, 1), button2 = new
    // JoystickButton(stick, 2), button3 = new JoystickButton(stick, 3),
    // button4 = new JoystickButton(stick,
    // 4), button5 = new JoystickButton(stick, 5), button6 = new
    // JoystickButton(stick, 6), button7 = new JoystickButton(stick, 7),
    // button8 = new JoystickButton(stick, 8);

    //button1.onTrue(new zeroDrivEncoder()); <-- works but simpler to
    button1.onTrue(new InstantCommand(() -> Robot.motorSubsys.zeroEncoder()));
    button2.onTrue(new GoToPosition(24));
    button3.onTrue(new GoToPosition((-24)));
    //button4.onTrue(new RotateInPlace(32)); // about 180 deg

    // driverGamepad.getButtonY().whenPressed(new SetClimberPosition
    // (Climber.DOWN_HEIGHT, 0.15));
  } // end constructor

  // public static OI getInstance(){
  // if(oi == null)  // code can use gI() method instead of named instance
  // oi = new OI(); // not clear why or how calling hidden instance worked
  // return oi;

  // applies deadzone (v.s.) to a joystick input from -1 to 1
  public static double deadzone(double input) {
    if (deadzone > Math.abs(input)) // if input smaller than deadzone
      return 0;
    return input;
  }

  public static double getLeftY() {
    return (-stick.getRawAxis(1));
  }

  public static double getRightX() {
    return stick.getRawAxis(4);
  }
}  // end OI class
