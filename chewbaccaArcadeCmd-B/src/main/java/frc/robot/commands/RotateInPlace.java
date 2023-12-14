// chewbaccArcadeCmd-B | cmd/subs | rotateInPlace cmd  AKA RIP

//  reads encoder to determine full rotation in place isFinished

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;

public class RotateInPlace extends CommandBase {
  // value received/sent = inches to drive each side fwd/rev
  double halfCirc;
  double leftEnco = 0;
  double riteEnco = 0;

  // CONSTRUCT a new RotateInPlace cmd 
  public RotateInPlace(double target) {
    // Use addRequirements() here to declare subsystem dependency
    addRequirements(Robot.myDrive);
    halfCirc = target; // each side to go bak or fwd this many inch

  } // end constructor

  // Called just before this Cmd runs first time;
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder here; PID slot likely already set
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot.myDrive.zeroEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled
  // subsys method uses SpkMax's PID position control to get exact same
  // rotation from each wheel
  @Override
  public void execute() {
    Robot.myDrive.rotate180(halfCirc);
  }

  // ... returns true when this Command no longer needs to run execute();
  @Override
  public boolean isFinished() {
    // must finish for cmd sequence it's in to advance, param inch
    leftEnco = Robot.myDrive._leftEncoder.getPosition();
    riteEnco = Robot.myDrive._rightEncoder.getPosition();
    boolean _atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 1 >= 
             (2 * Math.abs(halfCirc));

    System.out.println("roTarget is " + halfCirc);
    System.out.println("RtEnco = " + riteEnco);

    if (_atTargetPos) {
      SmartDashboard.putString("RIPcmd fin?","RIP fin");
      return true;
    } else {
      SmartDashboard.putString("RIPcmd fin?","NOT fin");
      return false;
    }
  }  // end isFinished

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {
    Robot.myDrive.zeroEncoder();
    leftEnco = 0;
    riteEnco = 0;
    System.out.println("RIP@end!");
  }

} // end RIP class
