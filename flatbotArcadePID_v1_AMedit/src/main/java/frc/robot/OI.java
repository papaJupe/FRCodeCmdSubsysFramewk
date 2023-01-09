/*-flatbotArcadePID v1 --AMedit        OI.j -----*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;
//import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.*;

// import frc.robot.Constant;

/**
 * OI class is the glue that binds controls on the physical operator
 * interface to the commands and command groups that control the robot
 */
public class OI {
  // private static OI oi;

  public static double deadzone = 0.04;

  public Joystick stick = new Joystick(0);;

  // CONSTRUCTOR
  public OI() {
    Button button1 = new JoystickButton(stick, 1), button2 = new JoystickButton(stick, 2),
        button3 = new JoystickButton(stick, 3), button4 = new JoystickButton(stick, 4),
        button5 = new JoystickButton(stick, 5), button6 = new JoystickButton(stick, 6),
        button7 = new JoystickButton(stick, 7), button8 = new JoystickButton(stick, 8);

    button3.whenPressed(new zeroDrivEncoder());
    button5.whenPressed(new GoToPosition(-24));
    button6.whenPressed(new GoToPosition(24));

    // operatorGamepad.getButtonBumperLeft().whilePressed(new ParallelCommandGroup
    // (new ShootWithLimelight(), new MoveBallsToShooter(false)));

    // driverGamepad.getButtonBumperRight().whilePressed(new ParallelCommandGroup
    // (new ShootWithLimelight(), new MoveBallsToShooter(false)));

    // driverGamepad.getButtonY().whenPressed(new SetClimberPosition
    // (Climber.DOWN_HEIGHT, 0.15));
  } // end constructor

  // public static OI getInstance(){
  // if(oi == null)
  // oi = new OI();
  // return oi;
  // }

  // applies deadzone to a joystick, input from -1 to 1
  public static double deadzone(double input) {
    if (deadzone > Math.abs(input)) // if input smaller than deadzone
      return 0;
    return input;
  }

  public double getLeftY() {
    return (-1 * stick.getRawAxis(1));
  }

  public double getLeftX() {
    return stick.getRawAxis(0);
  }
}
