// flatBotArcadePID_edited | cmd/subsys framewk |  GoToPosition cmd  AKA GTP

package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
// import frc.robot.*; // things like OI, Constant class
import static frc.robot.Constant.*;

public class GoToPosition extends CommandBase {
  // value received/sent = inches to drive straight
  int _target;

  // CONSTRUCTOR for cmd, called from OI by button _N_ press,
  // also by auto cmds.; recd param in.
  public GoToPosition(int target) {
    _target = target; // could overload to recv [subsys] or drive speed
    addRequirements(Robot._motorSubsys);
  }

  // Called just before this Cmd runs first time (each button press?);
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder; PID slot likely already set
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot._motorSubsys.zeroEncoder(0);
  }

  // subsyst method called repeatedly when this Cmd is scheduled
  // inch param sent to subsys
  @Override
  public void execute() {
    Robot._motorSubsys.goStraightPosition(_target);
  }

  // ... returns true when this Command no longer needs to run execute();
  // here, when signif. joystick movement detected or target pos reached
  @Override
  public boolean isFinished() { // must finish for sequence it's in to advance
    // need to see value to tell if it's getting close to target w/ pid #'s'
    int leftEnco = (int) Robot._motorSubsys.leftMaster.getSelectedSensorPosition();
    int riteEnco = (int) Robot._motorSubsys.rightMaster.getSelectedSensorPosition();
    boolean _atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 50 >= (2 * Math.abs(_target * kDriveInch2Tick));
    new PrintCommand("target is " + String.valueOf(_target * kDriveInch2Tick * 2));
    new PrintCommand("rt+leftEnco = " + (String.valueOf(Math.abs(leftEnco) + Math.abs(riteEnco))));

    if (_atTargetPos) {
      new PrintCommand("at _targetPos, fin GTPcmd");
      return true;
    } else {
      new PrintCommand("not aTarget yet");
      return false;
    }
    // return (Math.abs(Robot._oi.getLeftY()) > (OI.deadzone * 3) || _atTargetPos);

  } // end isFinished

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // need to do anything ?

    Robot._motorSubsys.zeroEncoder(0);
    new PrintCommand("GTP says endme!");

  }

} // end GTP class
