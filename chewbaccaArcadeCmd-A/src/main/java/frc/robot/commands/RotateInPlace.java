// chewbaccaArcadeCmd-A| cmd/subsys framewk |  rotate in place cmd   AKA RIP

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PrintCommand;

import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsys;
import static frc.robot.Constant.*; 
//import static frc.robot.subsystems.DriveSubsys.*;

public class RotateInPlace extends CommandBase {
  // value received/sent = inches to drive each side fwd/rev
  int _halfCirc;
  int tickTarget;

  /** Create a new RotateInPlace cmd */
  public RotateInPlace(int target) {
    // Use addRequirements() here to declare subsystem dependency
    addRequirements(Robot._myDrive);
    _halfCirc = target; // each side to go bak or fwd this many inch
    tickTarget = (int) (target * kDriveInch2Tick);
  } // end constructor

 // Called just before this Cmd runs first time;
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder here ?; pos slot likely already set
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot._myDrive.zeroEncoder(0);
  }

  // Called every time the scheduler runs while the command is scheduled
  @Override
  public void execute() {
    Robot._myDrive.rotate180(_halfCirc);
  }


  // ... returns true when this Command no longer needs to run execute();
  // could add when major joystick movement detected, as in auto to abort?
  @Override
  public boolean isFinished() {
     // must finish for cmd sequence to advance, number as tick
     int leftEnco = (int) DriveSubsys._leftEncoder.getPosition();
     int riteEnco = (int) DriveSubsys._rightEncoder.getPosition();
     boolean _atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 50 >= (2 * tickTarget);
 
  new PrintCommand("target x2 is " + String.valueOf(tickTarget * 2));
     new PrintCommand("rt+leftEnco abs = " + (String.valueOf((leftEnco + riteEnco))));

    if (_atTargetPos)  {   
      new PrintCommand("finish RIP");
      return true;
    }
     else {
      new PrintCommand("no fin to RIP");
      return false; }
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {
    Robot._myDrive.zeroEncoder(0);
    new PrintCommand("RIP says endme!");
  }

}  // end RIP class



 