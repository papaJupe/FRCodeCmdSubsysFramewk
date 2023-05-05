// flatBotArcadePID_v1_final | cmd/subsys framewk | commands/DriveWithPercent

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;
import frc.robot.Robot;

public class DriveWithPercent extends CommandBase {
  // CONSTRUCTOR  called in roboInit as default Cmd
    // maybe thereafter by teleopPeriodic but not seen there
  // failed in [subsyst].setDefaultCommand(_motorSubsys, new DriveWithPercent())

  public DriveWithPercent() {
    addRequirements(Robot._motorSubsys);
  } // end constructor

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double throttle = OI.deadzone(Robot._oi.getLeftY());
    double turn = OI.deadzone(Robot._oi.getLeftX());
    Robot._motorSubsys.arcaDriv(throttle * 0.7, turn * 0.5);
  }

  // Make this return true when this Command no longer needs to execute()
  @Override  // ever return true?
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {
    if (endme)
      Robot._motorSubsys.arcaDriv(0, 0);
  }

}
