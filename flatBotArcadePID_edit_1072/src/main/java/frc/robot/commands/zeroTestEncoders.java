/*----------------------------------------------------------------------------*/
/*      zeroTestEncoders cmd                                                          */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;

// called from OI on joy button 4 press, inits then empty 
// execute so no periodic callback?
//CONSTRUCTOR
public class zeroTestEncoders extends CommandBase {
  public zeroTestEncoders() {
    // Use requires(sic) here to declare subsystem dependency
    // e.g. requires(chassis);
    addRequirements(Robot._motorSubsys);
  }

  // subsys method called just before this Command runs first time
  @Override
  public void initialize() {
    System.out.println("zeroing encoders");
    Robot._motorSubsys.zeroEncoder(0);

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // what to do?
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run, now not allowed, why?
  // @Override
  // public void interrupted() {
  // }
}
