// flatBotArcadePID edited from asid1072 cmd/subsys framewk   command/DriveWithPercent

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;

//CONSTRUCTOR, called in Robot.j .setDefaultCommand(_motorSubsys, new DriveWithPercent())
public class DriveWithPercent extends CommandBase {
  public DriveWithPercent() {
    addRequirements(Robot._motorSubsys);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double throttle = OI.deadzone(Robot._oi.getLeftY());
    double turn = OI.deadzone(Robot._oi.getLeftX());
    Robot._motorSubsys.arcadeDrive(throttle, turn);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {
    if (endme) Robot._motorSubsys.arcadeDrive(0, 0);
  }

}
