// chewbaccArcadeCmd-B | cmd/subs framewk |  autoFwdRotBak.j

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class autoFwdRotBak extends SequentialCommandGroup {

    public autoFwdRotBak() {
        // Add your commands in the addCommands() call, e.g.
        // addCommands(new FooCommand(), new BarCommand());  or

        new  GoToPosition(42)
                .andThen(new WaitCommand(4.0))
                .andThen(new RotateInPlace(24))
                .andThen(new WaitCommand(4.0))
                .andThen(new GoToPosition(42));
                
        // addCommands(
        // new RotateInPlace(14), new WaitCommand(3.0),
        // new GoToPosition(autoDriveInch - 12));
        
    } // end constructor

} // end command class