// flatBotArcadePID_v.2           commands/DriveWithPercent

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;
import frc.robot.Robot;


public class DriveWithPercent extends CommandBase {
  // CONSTRUCTOR
  // set in robot.j (r.init) as default for motorSubsys
  public DriveWithPercent() {
    addRequirements(Robot.motorSubsys);
  } // end constructor

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double throttle = OI.deadzone(OI.getLeftY());
    double turn = OI.deadzone(OI.getRightX());
    Robot.motorSubsys.arcaDriv(throttle * 0.7, -turn * 0.5);
  }

  // Make this return true when this Command no longer needs to execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true (? never)
  @Override
  public void end(boolean endme) {
    if (endme)
      Robot.motorSubsys.arcaDriv(0, 0);
  }

}
