// chewbaccArcadeCmd-A | cmd/subs framewk |  rotateInPlace cmd   AKA RIP

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsys;

public class RotateInPlace extends CommandBase {
  // value received/sent = inches to drive each side fwd/rev
  int _halfCirc;
  int leftEnco = 0;
  int riteEnco = 0;

  /** CONSTRUCT a new RotateInPlace cmd */
  public RotateInPlace(int target) {
    // Use addRequirements() here to declare subsystem dependency
    addRequirements(Robot._myDrive);
    _halfCirc = target; // each side to go bak or fwd this many inch

  } // end constructor

  // Called just before this Cmd runs first time;
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder here; pos PID slot likely already set
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
    // must finish for cmd sequence to advance, param inch
    leftEnco = (int) DriveSubsys._leftEncoder.getPosition();
    riteEnco = (int) DriveSubsys._rightEncoder.getPosition();
    boolean _atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 1 >= (2 * Math.abs(_halfCirc));

    System.out.println("roTarget is " + _halfCirc);
    System.out.println("RtEnco = " + riteEnco);

    if (_atTargetPos) {
      SmartDashboard.putString("RIPcmd fin?","RIP fin");
      return true;
    } else {
      SmartDashboard.putString("RIPcmd fin?","NOT fin");
      return false;
    }
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {
    Robot._myDrive.zeroEncoder(0);
    leftEnco = 0;
    riteEnco = 0;
    System.out.println("RIP@end!");
  }

} // end RIP class
