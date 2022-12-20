// flatBotArcadePID edited from asid1072 cmd/subsys framewk   GoToPosition cmd

package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.*;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.can.*;

public class GoToPosition extends CommandBase {
  // inches to drive straight
  int _target;

  // CONSTRUCTOR for cmd, called by button A press from OI
  public GoToPosition(int target) {
    _target = target;
    addRequirements(Robot._motorSubsys);
  }

  // Called just before this Cmd runs first time (each button A press?);
  // data slot 0-3, pid 0-1
  @Override // need to zero encoder here ??
  public void initialize() {
    Robot._motorSubsys._masterTalonL.selectProfileSlot(Robot._motorSubsys._position_slot, 0);
    Robot._motorSubsys.zeroEncoder(0);
    Robot._motorSubsys._masterTalonR.selectProfileSlot(Robot._motorSubsys._position_slot, 0);
    Robot._motorSubsys.zeroEncoder(0);
  }

  // subsyst method called repeatedly when this Cmnd is scheduled
  @Override
  public void execute() {
    Robot._motorSubsys.setPosition(_target);
  }

  // ... returns true when this Command no longer needs to run execute()
  // here, when any joystick movement detected
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

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run -- no longer valid method ??
  // @Override
  // public void interrupted() {
  // end();
  // }
}
