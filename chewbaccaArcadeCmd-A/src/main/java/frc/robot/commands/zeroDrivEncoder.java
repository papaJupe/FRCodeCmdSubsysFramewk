/* zeroDrivEncod cmd
 * may not be needed, if button action in OI can call cmd in subsyst directly?
 * other cmd could do that w/o needing this cmd
 */


package frc.robot.commands; 

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;

public class zeroDrivEncoder extends CommandBase {

// called from OI on joy button 3 press, inits then blank 
// execute() so no periodic callback to CS ?
// CONSTRUCTOR
  public zeroDrivEncoder() {
    // Used to declare subsystem dependency
    addRequirements(Robot._myDrive);
  }

  // subsys method 'zeroEnc_' called just before this Command 'runs'
  // first time; but probably all it ever does since execute() empty
  @Override
  public void initialize() {
    System.out.println("zeroing encoders");
    Robot._myDrive.zeroEncoder();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // returns true full time
  // execute() empty so might as well end asap
  @Override
  public boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // what to do?
    System.out.println("enco zeroed");
  }

}


