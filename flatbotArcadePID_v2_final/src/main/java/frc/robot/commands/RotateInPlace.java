// flatBotArcadePID_v2      | cmd/subsys framewk |  rotate in place cmd   aka RIP
 
package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
import static frc.robot.Constant.*; // things like OI, Constant class

public class RotateInPlace extends CommandBase {
  // value received/sent = inches to drive each side fwd/rev
  double halfCirc;
  double tickTarget;
  double leftEnco = 0;
  double riteEnco = 0;
  double loopCt = 0;

  /** Construct a new RotateInPlace cmd */
  public RotateInPlace(double target) {
    // Use addRequirements() to declare subsystem dependency
    addRequirements(Robot.motorSubsys);
    halfCirc = target; // each side to go bak or fwd this many inch
    tickTarget = (target * kDriveInch2Tick);
  }

  // Called just before this Cmd runs very first time;
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder here ; pos slot likely already set
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot.motorSubsys.zeroEncoder();
    SmartDashboard.putString("RIPcmdFin?", "1stInit");
  }

  // Called every time the scheduler runs while the command is scheduled
  @Override
  public void execute() {
    Robot.motorSubsys.rotate180(halfCirc);
  }

  // ... returns true when this Command no longer needs to run execute();
  // could add when major joystick movement detected, as in auto to abort?
  @Override
  public boolean isFinished() {
     // must finish for cmd sequence to advance, number as tick
     // end point just tracking one encoder for simplicity
     leftEnco = Robot.motorSubsys.leftMaster.getSelectedSensorPosition();
     riteEnco = Robot.motorSubsys.rightMaster.getSelectedSensorPosition();
     boolean atTargetPos = Math.abs(leftEnco) >= Math.abs(tickTarget);
 
      if (loopCt++ >= 30) {
       System.out.println("'encoTarg' is " + tickTarget);
       System.out.println("leftEnco = " + leftEnco);
       System.out.println("riteEnco = " + riteEnco);
       loopCt = 0;
     }
 
     if (atTargetPos) {
       SmartDashboard.putString("RIPcmdFin?", "true");
       return true;
     } else {
       SmartDashboard.putString("RIPcmdFin?", "not Fin");
       return false;
     }
    }
  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {
    Robot.motorSubsys.zeroEncoder();
    SmartDashboard.putString("RIPcmdFin?", String.valueOf(endme));
    leftEnco = 0;
    riteEnco = 0;
  }

}  // end RIP cmd class