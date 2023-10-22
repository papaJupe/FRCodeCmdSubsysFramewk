package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.OI;
import frc.robot.Robot;


public class DriveWithPercent extends CommandBase {
  // CONSTRUCTOR
  // instance in robot.j (roboIinit) as default for _myDrive
  public DriveWithPercent() {
    addRequirements(Robot._myDrive);
  } // end constructor

  // Called just before this Command runs the very first time
  @Override
  public void initialize() {
    Robot._myDrive.zeroEncoder(0);
    System.out.println("DWPcmd init");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double throttle = OI.deadzone(Robot._operatorInterface.getLeftY());
    double turn = OI.deadzone(Robot._operatorInterface.getLeftX());
    Robot._myDrive.arcaDriv(throttle * 0.7, turn * 0.5);
  }

  // Make this return true when this Command no longer needs to execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) {
    if (endme)
      Robot._myDrive.arcaDriv(0, 0);
  }

}  // end DWP cmd
