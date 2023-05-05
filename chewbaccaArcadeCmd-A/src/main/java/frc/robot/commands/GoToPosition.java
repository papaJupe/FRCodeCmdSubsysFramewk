// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PrintCommand;

import frc.robot.Robot;
import static frc.robot.Constant.*;
import frc.robot.subsystems.DriveSubsys;


public class GoToPosition extends CommandBase {
  /** Creates a new GoToPosition. */
  int _target;

  public GoToPosition(int targetPos) {

    _target = targetPos;
    // Use addRequirements() here to declare subsystem dependencies
    addRequirements(Robot._myDrive);
  }

 // Called just before this Cmd runs first time (each button press?);
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder; PID slot likely already set
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot._myDrive.zeroEncoder(0);
  }

  // subsyst method called repeatedly when this Cmd is scheduled
  // inch param sent to subsys
  @Override
  public void execute() {
    Robot._myDrive.goStraightPosition(_target);
  }

  // ... returns true when this Command no longer needs to run execute();
  // here, when signif. joystick movement detected or target pos reached
  @Override
  public boolean isFinished() { // must finish for sequence it's in to advance
    // need to see value to tell if it's getting close to target w/ pid #'s'
    int leftEnco = (int) DriveSubsys._leftEncoder.getPosition();
    int riteEnco = (int) DriveSubsys._rightEncoder.getPosition();
    
    boolean _atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 50 >= (2 * Math.abs(_target * kDriveInch2Tick));
    new PrintCommand("target is " + String.valueOf(_target));
    new PrintCommand("rt+leftEnco avg = " + (String.valueOf((leftEnco + riteEnco)/2)));

    if (_atTargetPos) {
      new PrintCommand("at _targetPos, fin GTPcmd");
      return true;
    } else {
      new PrintCommand("not _aTarget yet");
    return false;
    }

    // return (Math.abs(Robot._oi.getLeftY()) > (OI.deadzone * 3) || _atTargetPos);
  }  // end isFinished

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // need to do anything ?

    Robot._myDrive.zeroEncoder(0);
    new PrintCommand("GTP says endme!");

  }

}  //end GTP class
