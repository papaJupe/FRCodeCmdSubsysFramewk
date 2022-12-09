/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;
//import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// commands
import frc.robot.commands.*;
// import frc.robot.Constant;

/**
 * OI class is the glue that binds controls on the physical operator
 * interface to the commands and command groups that control the robot.
 */
public class OI {

  public static double deadzone = 0.1;

  public Joystick stick = new Joystick(0);
  Button buttonA = new JoystickButton(stick, 1), buttonB = new JoystickButton(stick, 2),
      buttonX = new JoystickButton(stick, 3), buttonY = new JoystickButton(stick, 4),
      button5 = new JoystickButton(stick, 5), button6 = new JoystickButton(stick, 6),
      button7 = new JoystickButton(stick, 7), button8 = new JoystickButton(stick, 8);

  public OI() {  // in inches
    buttonA.whenPressed(new GoToPosition(24));
    buttonY.whenPressed(new zeroTestEncoders());
  }

  public static double deadzone(double input) { // applies deadzone to a joystick, input from -1 ro 1
    if (deadzone > Math.abs(input)) // if input is smaller than deadzone
      return 0;
    return input;
  }

  public double getLeftY() {
    return stick.getRawAxis(Constant.throttle) * Constant.invertLeftY;
  }

  public double getLeftX() {
    return stick.getRawAxis(Constant.turn) * Constant.invertLeftX;
  }

}
