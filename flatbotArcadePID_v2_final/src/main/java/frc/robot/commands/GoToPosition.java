// flatBotArcadePID_V2       GoToPosition cmd  AKA GTP

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
// import frc.robot.*; // things like OI, Constant class
import static frc.robot.Constant.*;

public class GoToPosition extends CommandBase {
  // value received/sent = inches to drive straight
  private double target;
  // these value in tick
  private double leftEnco = 0;
  private double riteEnco = 0;
  private double loopCt = 0;

  // CONSTRUCTOR for cmd, called from OI by button _N_ press,
  // also by auto cmds.; recd param inch
  public GoToPosition(double autoDriveInch) {
    target = autoDriveInch; // could overload to recv [subsys] or drive speed
    addRequirements(Robot.motorSubsys);
  }

  // Called just before this Cmd runs first time (each button press?);
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder; PID slot likely already set
  public void initialize() {
    // Robot.motorSubsys.leftMaster.selectProfileSlot(Robot.motorSubsys
    // ._position_slot, 0);
    // Robot.motorSubsys.rightMaster.selectProfileSlot(Robot.motorSubsys
    // ._position_slot, 0);
    Robot.motorSubsys.zeroEncoder();
    SmartDashboard.putString("GTPcmdFin?", "1stInit");

  }

  // subsyst method called repeatedly when this Cmd is scheduled
  // inch param sent to subsys
  @Override
  public void execute() {
    Robot.motorSubsys.goStraightPosition(target);
  }

  // ... returns true when this Command no longer needs to run execute();
  // here this is when target pos reached
  @Override
  public boolean isFinished() { // must finish for sequence it's in to advance
    // check value to see if it's getting close to target
    leftEnco = Robot.motorSubsys.leftMaster.getSelectedSensorPosition();
    riteEnco = Robot.motorSubsys.rightMaster.getSelectedSensorPosition();
    boolean atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 1 >= 
                (2 * Math.abs(target * kDriveInch2Tick));

    if (loopCt++ >= 30) {
      System.out.println("'encoTarg' is " + target * kDriveInch2Tick);
      System.out.println("leftEnco = " + leftEnco);
      System.out.println("riteEnco = " + riteEnco);
      loopCt = 0;
    }

    if (atTargetPos) { // display on SD for debugging purpose
      SmartDashboard.putString("GTPcmdFin?", "true");
      return true;
    } else {
      SmartDashboard.putString("GTPcmdFin?", "not Fin");
      return false;
    }
    // this option interrupted cmd if joystick moved
    // return (Math.abs(Robot._oi.getLeftY()) > (OI.deadzone * 3) || _atTargetPos);

  } // end isFinished

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {

    SmartDashboard.putString("GTPcmdFin?", String.valueOf(endme));
    leftEnco = 0;
    riteEnco = 0;

  } // end me

} // end GTP class
