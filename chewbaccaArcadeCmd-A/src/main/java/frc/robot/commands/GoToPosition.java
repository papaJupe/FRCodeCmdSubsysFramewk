// chewbaccArcadeCmd                  GoToPosition cmd  aka GTP

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsys;

public class GoToPosition extends CommandBase {

  int _target;
  int leftEnco = 0;
  int riteEnco =0;

  /** Constructor creates a new GoToPosition cmd */
  public GoToPosition(int targetPos) {

    _target = targetPos;
    // Use addRequirements() here to declare subsystem dependencies
    addRequirements(Robot._myDrive);
  }

  // Called just before this Cmd runs very first time after new deploy ;
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder; PID slot likely already def. to 0
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot._myDrive.zeroEncoder();
    SmartDashboard.putString("GTPcmdFin?", "1stInit");
  }

  // subsyst method called repeatedly when this Cmd is scheduled
  // param in inch
  @Override
  public void execute() {
    Robot._myDrive.goStratPosit(_target);
  }

  // ... returns true when this Command no longer needs to run execute();
  // here, when signif. joystick movement detected or target pos reached
  @Override
  public boolean isFinished() { // must finish for sequence it's in to advance
    // encod value tell if it's getting close to target w/ pid #'s'
    leftEnco = (int) DriveSubsys._leftEncoder.getPosition();
    riteEnco = (int) DriveSubsys._rightEncoder.getPosition();

    boolean _atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 1 >= (2 * Math.abs(_target));
    System.out.println("target is " + _target);
    System.out.println("rt+leftEnco = " + (leftEnco + riteEnco));

    if (_atTargetPos) {
      SmartDashboard.putString("GTPcmdFin?", "true");
      return true;
    } else {
      SmartDashboard.putString("GTPcmdFin?", "not fin");
      return false;
    }

    // return (Math.abs(Robot._oi.getLeftY()) > (OI.deadzone * 3) || _atTargetPos);
  } // end isFinished

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { 
    Robot._myDrive.zeroEncoder();
    leftEnco = 0;
    riteEnco = 0;

    System.out.println("GTP says endme!");
    SmartDashboard.putString("GTPcmdFin?", String.valueOf(endme));
  }

} // end GTP class
