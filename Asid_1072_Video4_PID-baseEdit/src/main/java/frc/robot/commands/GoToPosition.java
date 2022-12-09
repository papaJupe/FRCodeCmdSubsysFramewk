/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
 //import com.ctre.phoenix.motorcontrol.can.*;

public class GoToPosition extends CommandBase {
  // inches to drive straight
  int _target; 
  public GoToPosition(int target) {
    _target = target;
    addRequirements(Robot._motorSubsys);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot._motorSubsys._masterTalonL.selectProfileSlot(Robot._motorSubsys._position_slot, 0);
    Robot._motorSubsys._masterTalonR.selectProfileSlot(Robot._motorSubsys._position_slot, 0);
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    Robot._motorSubsys.setPosition(_target);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    if(Robot._oi.getLeftY() > OI.deadzone || Robot._oi.getLeftX() > OI.deadzone)
      return true;
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // what to do?
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
//  @Override
//   public void interrupted() {
//      end();
//   }
}
