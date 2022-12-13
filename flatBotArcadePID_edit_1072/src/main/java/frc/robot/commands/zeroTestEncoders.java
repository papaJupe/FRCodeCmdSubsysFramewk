/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;

// called from OI on joy button 4 press, inits then empty 
// execute so no periodic callback?
public class zeroTestEncoders extends CommandBase {
  public zeroTestEncoders() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    addRequirements(Robot._motorSubsys);
  }

  // Called just before this Command runs the first time
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
  // subsystems is scheduled to run
  // @Override
  // public void interrupted() {
  // }
}
