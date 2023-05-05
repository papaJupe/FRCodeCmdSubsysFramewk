package frc.robot.commands;

import static frc.robot.Constant.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI {

    public static double deadzone = 0.04;

// stick instance only used here, so why make public ?
    private static Joystick _driverStick = new Joystick(kDriveControlPort);
    
//  or CommandPS4Controller or CommandJoystick or
//   private final CommandXboxController m_driverController =
//   new CommandXboxController(Constant.kDriveControlPort);

// butt binding probably need to be in constructor
public OI() {

  final JoystickButton
      button3 = new JoystickButton(_driverStick, 3),
      button5 = new JoystickButton(_driverStick, 5), 
      button6 = new JoystickButton(_driverStick, 6);

  // Trigger button1 = new JoystickButton(stick, 1), button2 = new JoystickButton(stick, 2),
  //     button3 = new JoystickButton(stick, 3), button4 = new JoystickButton(stick, 4),
  //     button5 = new JoystickButton(stick, 5), button6 = new JoystickButton(stick, 6),
  //     button7 = new JoystickButton(stick, 7), button8 = new JoystickButton(stick, 8);

  button3.onTrue(new zeroDrivEncoder());
  button5.onTrue(new GoToPosition(-24));
  button6.onTrue(new GoToPosition(24));

  // operatorGamepad.getButtonBumperLeft().whilePressed(new ParallelCommandGroup
  // (new ShootWithLimelight(), new MoveBallsToShooter(false)));

  // driverGamepad.getButtonBumperRight().whilePressed(new ParallelCommandGroup
  // (new ShootWithLimelight(), new MoveBallsToShooter(false)));

  // driverGamepad.getButtonY().whenPressed(new SetClimberPosition
  // (Climber.DOWN_HEIGHT, 0.15));
}
  // applies deadzone to a joystick input from -1 to 1
  public static double deadzone(double input) {
    if (deadzone > Math.abs(input)) // if input smaller than deadzone
      return 0;
    return input;
  }

  public double getLeftY() {
    return (_driverStick.getRawAxis(1));
  }

  public double getLeftX() {
    return _driverStick.getRawAxis(0);
  }
}  // end OI class
