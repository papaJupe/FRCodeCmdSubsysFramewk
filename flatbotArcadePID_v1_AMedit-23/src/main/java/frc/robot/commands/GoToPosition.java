// flatBotArcadePID_edited | cmd/subsys framewk |  GoToPosition cmd

package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
import frc.robot.*; // things like OI, Constant class

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.can.*;

public class GoToPosition extends CommandBase {
  // value received/sent = inches to drive straight
  int _target;

  // CONSTRUCTOR for cmd, called from OI by button _N_ press,
  // also by auto cmds. recd param in.
  public GoToPosition(int target) {
    _target = target; // could overload to recv [subsys] or drive speed
    addRequirements(Robot._motorSubsys);
  }

  // Called just before this Cmd runs first time (each button press?);
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder here ?; slot likely already set
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot._motorSubsys.zeroEncoder(0);
  }

  // subsyst method called repeatedly when this Cmd is scheduled
  // inch value sent to subsys
  @Override
  public void execute() {
    Robot._motorSubsys.setPosition(_target);
  }

  // ... returns true when this Command no longer needs to run execute();
  // here, when any joystick movement detected, might prefer to poll only
  // one axis to avoid accidental finish
  @Override
  public boolean isFinished() {
    if (Robot._oi.getLeftY() > OI.deadzone || Robot._oi.getLeftX() > OI.deadzone)
      return true;
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // need to do anthing ?
  }

}
