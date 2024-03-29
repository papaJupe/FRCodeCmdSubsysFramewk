/*-flatbotArcadePID v1 final        OperatorInput.j  AKA OI      -----*/
// button binding here in style of 7048 (newer?), in constr. but may
// work before that; previously I used (asid 1072) Button type as class var,
// assigned cmd in constructor .whenPressed vs. onTrue here
// v2 I changed Trig to JB type

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;
import frc.robot.commands.*;

/**
 * OI class is the glue that binds controls on the physical operator
 * interface to the commands and command groups that control the robot
 */
public class OI {
  // private static OI oi;

  public static double deadzone = 0.04;

  public Joystick stick = new Joystick(0); // if only used here, why public?

  // CONSTRUCTOR
  public OI() {

    Trigger // or final JoystickButton
        button3 = new JoystickButton(stick, 3),
        button5 = new JoystickButton(stick, 5), 
        button6 = new JoystickButton(stick, 6);

// Trigger button1 = new JoystickButton(stick, 1), button2 = new JB(stick, 2),
    //     button3 = new JoystickButton(stick, 3), button4 = new JB(stick, 4),
    //     button5 = new JoystickButton(stick, 5), button6 = new JB(stick, 6),
    //     button7 = new JoystickButton(stick, 7), button8 = new JB(stick, 8);

    button3.onTrue(new zeroDrivEncoder());
    button5.onTrue(new GoToPosition(-24));
    button6.onTrue(new GoToPosition(24));

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
  // return oi; as static pvt var, will refer to new OI or any made before
  // by this method. w/ public constructor, not sure if this qualifies as
  // singleton or if some class can make another (named or not) instance?

  // applies deadzone to a joystick, input from -1 to 1
  public static double deadzone(double input) {
    if (deadzone > Math.abs(input)) // if input smaller than deadzone
      return 0;
    return input;
  }
  // method used by DriveWithPercent
  public double getLeftY() {
    return (-1 * stick.getRawAxis(1));
  }
  public double getLeftX() {
    return stick.getRawAxis(0);
  }
}  // end OI class