package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constant;

// NOTE:  Consider using this command inline, rather than writing a subclass. 
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html

public class autoFwdRotBak extends SequentialCommandGroup {

    public autoFwdRotBak() {
        // Add your commands in the addCommands() call, e.g.
        // addCommands(new FooCommand(), new BarCommand());

        new GoToPosition(Constant.autoDriveInch - 12)
                .andThen(new PrintCommand("fin GTP cmd 1"))
                .andThen(new WaitCommand(5.0))
                .andThen(new PrintCommand("fin wait cmd 2"))
                .andThen(new RotateInPlace(24))
                .andThen(new PrintCommand("fin RIP cmd 3"))
                .andThen(new WaitCommand(5.0))
                .andThen(new PrintCommand("fin wait cmd 4"))
                .andThen(new GoToPosition(Constant.autoDriveInch - 12))
                .andThen(new PrintCommand("fin GTP fin cmd"));

        // addCommands(new GoToPosition(Constant.autoDriveInch), new WaitCommand(3.0),
        // new RotateInPlace(14), new WaitCommand(3.0),
        // new GoToPosition(Constant.autoDriveInch - 12));
        // addCommands( new RotateInPlace(14), new RotateInPlace(-14));
    } // end constructor

} // end comnd class
