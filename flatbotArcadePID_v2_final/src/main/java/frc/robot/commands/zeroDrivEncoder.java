/*-----------------------------------------------------------------
  zeroDrivEncoder cmd     not really necessary when button press
   can use Instant Command    kept for other possible use                                          
------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;

// called from OI on joy button 3 press, inits then blank 
// execute() but likely has persistent action while pressed
// unless isFinished stops after one cycle
//CONSTRUCTOR
public class zeroDrivEncoder extends CommandBase {
  public zeroDrivEncoder() {
    // Used to declare subsystem dependency
    addRequirements(Robot.motorSubsys);
  }

  // subsys method 'zeroEnc_' called just before this Command 'runs'
  // first time; probably all it ever does since execute() empty
  @Override
  public void initialize() {
    System.out.println("zeroing encoders");
    Robot.motorSubsys.zeroEncoder();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // returns true when this Command no longer needs to run execute()
  // I guess immediately since execute() is empty?
  @Override
  public boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // nothing to do?
  }

}
