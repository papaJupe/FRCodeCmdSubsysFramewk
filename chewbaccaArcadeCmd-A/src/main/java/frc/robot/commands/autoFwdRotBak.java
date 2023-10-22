package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constant;

public class autoFwdRotBak extends SequentialCommandGroup {

    public autoFwdRotBak() {
        // Add your commands in the addCommands() call, e.g.
        // addCommands(new FooCommand(), new BarCommand());

        new GoToPosition(Constant.autoDriveInch - 12)
                .andThen(new WaitCommand(5.0))
                .andThen(new RotateInPlace(24))
                .andThen(new WaitCommand(5.0))
                .andThen(new GoToPosition(Constant.autoDriveInch - 12));
                
        // addCommands(
        // new RotateInPlace(14), new WaitCommand(3.0),
        // new GoToPosition(Constant.autoDriveInch - 12));
        
    } // end constructor

} // end comnd class
