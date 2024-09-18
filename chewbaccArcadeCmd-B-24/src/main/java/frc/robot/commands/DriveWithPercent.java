// chewbaccaArcadeCmd - B        DriveWithPercent.j

// default command for teleOp

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.OI;
import frc.robot.Robot;

public class DriveWithPercent extends Command {
  // CONSTRUCTOR
  // instance in robot.j, roboInit sets default for drive subsys
  public DriveWithPercent() {
    addRequirements(Robot.myDrive);
  } // end constructor

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() { // deadband filter inputs from joystick
    double throttle = OI.deadDrop(Robot.operatorInterface.getLeftY());
    double turn = OI.deadDrop(Robot.operatorInterface.getRightX());

    Robot.myDrive.arcaDriv(throttle * 0.8, turn * 0.6);
  }

  // should return true when this Command no longer needs to execute
  @Override
  public boolean isFinished() { // but default teleOp Cmd does not end
    return false;
  }

  // Called once after isFinished returns true (never)
  @Override
  public void end(boolean endme) {
    if (endme)
      Robot.myDrive.arcaDriv(0, 0);
  }
} // end DriveWithPercent class
