// chewbaccaArcadeCmd - B                       GoToPosition.j

// called from Robot.j's simple auto and sequential routines
// expects a double param for number of inches to drive straight.
// calls driveSubsys goStraightPosition(double target) method 
// which uses SpkMax's PID position mode to go there; however this
// cmd's isFinished() condx becomes true when encoders say it is

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;

import frc.robot.Robot;

public class GoToPosition extends Command {
 
  double target;

// constructor
  public GoToPosition(double targetPos) {

    target = targetPos;
    // Use addRequirements() here to declare subsystem dependency
    addRequirements(Robot.myDrive);
  }

 // Called just before this Cmd runs first time (each Enable Auto?);
  // data slot 0-3, pid loop 0-1
  @Override // need to zero encoder; PID slot likely already set to default
  public void initialize() {
    // Robot._motorSubsys.leftMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    // Robot._motorSubsys.rightMaster.selectProfileSlot(Robot._motorSubsys
    // ._position_slot, 0);
    Robot.myDrive.zeroEncoder();
  }

  // subsyst method called repeatedly when this Cmd is scheduled
  // -- distance param in inches sent to subsys
  @Override
  public void execute() { // subsyst method uses PID position control
            // method built in to the SparkMax/Neo
    Robot.myDrive.goStraightPosition(target);
  }

  // returns true when this Command no longer needs to run execute();
  // here, when target pos reached
  @Override
  public boolean isFinished() { // must 'finish' for auto sequence it's in
  // to advance
    double leftEnco = Robot.myDrive.leftEncoder.getPosition();
    double riteEnco = Robot.myDrive.rightEncoder.getPosition();
    
    boolean atTargetPos = Math.abs(leftEnco) + Math.abs(riteEnco) + 50 >= 
           (2 * Math.abs(target));
    new PrintCommand("target is " + String.valueOf(target));
    new PrintCommand("rt+leftEnco avg = " + 
                    (String.valueOf((leftEnco + riteEnco)/2)));

    if (atTargetPos) {
      new PrintCommand("at targetPos, fin GTPcmd");
      return true;
    } else {
      new PrintCommand("not at Target yet");
    return false;
    }
  }  // end isFinished

  // Called once after isFinished returns true
  @Override
  public void end(boolean endme) { // final things to do

    Robot.myDrive.zeroEncoder();
    new PrintCommand("GTP at end!");
  }

}  //end GTP class
