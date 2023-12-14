// Autos.java  example code , not used in this project

package frc.robot.commands;

import frc.robot.subsystems.DriveSubsys;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static CommandBase exampleAuto(DriveSubsys subsystem) {
    return Commands.sequence(subsystem.exampleMethodCommand(), new GoToPosition(42));
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
