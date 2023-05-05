// flatBotArcadePID_edited | cmd/subsys framewk |  GoToPosition cmd

package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.*; // things like OI, Constant class

public class GoToPosition extends CommandBase {
  // value received/sent = inches to drive straight
  int _target;

  // CONSTRUCTOR for cmd, called from OI by button _N_ press,
  // also by auto cmds; recd param inch
  public GoToPosition(int target) {
    _target = target; // could overload to recv [subsys] or drive speed
    addRequirements(Robot._motorSubsys);
  }

  // Called just before this Cmd runs first time (each button press?);
  // data slot 0-3, pid loop 0-1 ; repeat presses don't seem to run this
  @Override // need to zero encoder here; slot 0 likely already set
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot._motorSubsys.zeroEncoder(0);  // set each to 0z
  }

  // subsyst method called repeatedly when this Cmd is scheduled
  // inch value sent to subsys
  @Override
  public void execute() {
    Robot._motorSubsys.setPosition(_target);
  }

  // ... returns true when this Command no longer needs to run execute();
  // -- was when any joystick movement detected, now trying to detect if
  // at target by sensor polling or large L joy movement
  @Override
  public boolean isFinished() {
    boolean atTarget = Math.abs(Robot._motorSubsys.leftMaster.
      getSelectedSensorPosition() + 50)
        >= Math.abs(_target * Constant.kDriveInch2Tick);
        // put some text about target on SD
        if (atTarget) SmartDashboard.putString("GTP cmd fin", "true");
        else SmartDashboard.putString("GTP cmd fin", "not fin");
     return (Math.abs(Robot._oi.getLeftY()) > OI.deadzone || atTarget);
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // need to do anthing ? write to SD?
    SmartDashboard.putString("GTP cmd fin","ended");
  }

}
