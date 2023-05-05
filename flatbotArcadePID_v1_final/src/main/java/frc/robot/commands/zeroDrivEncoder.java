/*-----------------------------------------------------------------
      zeroDrivEncoders cmd                                           */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
//import frc.robot.*;
import frc.robot.Robot;

// called from OI on joy button 3 press, inits then blank 
// execute() so disabling (?) periodic callback to CS ?
//CONSTRUCTOR
public class zeroDrivEncoder extends CommandBase {
  public zeroDrivEncoder() {
    // Used to declare subsystem dependency
    addRequirements(Robot._motorSubsys);
  }

  // subsys method 'zeroEnc_' called just before this Command runs
  // first time; probably all it ever does since execute() empty
  @Override
  public void initialize() {
    System.out.println("zeroing encoders");
    Robot._motorSubsys.zeroEncoder(0);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // returns true when this Command no longer needs to run execute()
  // not clear how that need determined, ie execute() being empty?
  @Override
  public boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // what to do?
  }
} // end class
