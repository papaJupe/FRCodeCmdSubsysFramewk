// seems to be 'convenience' way to make auto sequence from existing
// commands involving a subsystem and some params to send them? Haven't 
// tried to use

package frc.robot.commands;

import frc.robot.subsystems.DriveSubsys;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public final class AutoStub {
  /** Example static factory for an autonomous command. */
  public static Command exampleAuto(DriveSubsys subsystem, double target) {
    return Commands.sequence(new RotateInPlace(4), new GoToPosition(target));
  }

  private AutoStub() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
