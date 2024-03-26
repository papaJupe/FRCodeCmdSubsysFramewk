// chewbaccaArcadeCmd-B                      Robot.j

// test program for flatterbot/aka chewbacca; uses cmd/subsys framewk
// and class arrangement of 2019-20 season, putting robot specifics
// back in Robot.j and OI.j instead of RC, for simpler code for very
// basic platform. Constants kept close/inside classes where used.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.subsystems.DriveSubsys;

/* The VM is configured to automatically run this class & 
 * call the functions of each mode.
 */
public class Robot extends TimedRobot {

  public static DriveSubsys myDrive = new DriveSubsys();
  public static OI operatorInterface; // joystk, button binding...
  private Command autoCommand;

  private static final double autoDriveInch = 48;

  // declare auto chooser for user to select an auto cmd
  private SendableChooser<Command> autonChooser;

  /* This method runs when the robot is first started and is used for
   * initializations [many things done in RC otherwise].
   */
  @Override
  public void robotInit() {

    // NOT USED - m_robotContainer = new RobotContainer(); instead object
    // instances [drive, OI, autoCmds, chooser for auto] are setup here;
    // things RobotContainer would init, button bindings+, now in OI class;
    // autonomous chooser on SmtDashbd et. al. now here
    System.out.println("start robotInit");

    operatorInterface = new OI(); // joystick defin & button binding

    myDrive.setDefaultCommand(new DriveWithPercent());
    // default commands are commands that are always running -- mainly used
    // for teleOp controls that do not end; attempt to set default cmd in 
    // each subsys to unclutter this class failed

    // DoubleSolenoid _valve = new DoubleSolenoid(PneuModType.REVPH, 0, 4);
    // _valve.set(DoubleSolenoid.Value.kForward);

    // when Auto Mode activated, Robot.autoInit() calls the selected cmd 
    // using its instance from here and schedules cmd it receives

    final Command simpleAuto = new GoToPosition(autoDriveInch); // 24
    final Command simplePlus = new GoToPosition(autoDriveInch + 24);
    final SequentialCommandGroup autoSequence1 = new autoFwdRotBak();

    autonChooser = new SendableChooser<>();
    autonChooser.setDefaultOption("simpleAuto", simpleAuto);
    autonChooser.addOption("goFarther", simplePlus);
    autonChooser.addOption("fwd-Rot180-bak", autoSequence1);

    // if SD not enabled, does this appear in LV-dash's chooser / DS? NO
    // v. tip doc how to add to LV dashbd chooser instead
    SmartDashboard.putData("Auton Selector", autonChooser);
    // sending data to chooser may require
    // NetworkTableInstance.getDefault().setUpdateRate(0.02);

    System.out.println("robot initialized");

  } // end robotInit()

  /* This function is called every 20 ms, no matter the mode. Use for
   * things like diagnostics that you want run during disabled et.al.
   *
   * This runs after the mode specific periodics, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled commands, running already-scheduled commands, removing
    // finished or interrupted commands, and running subsystem periodic() 
    // methods -- must be called from this periodic block in order for
    //  anything in the Command-based framework to work properly.
    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    autoCommand = autonChooser.getSelected();
    // schedule the autonomous command user has selected
    if (autoCommand != null) {
      autoCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override   // empty if scheduled Cmd does everything needed itself
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous cmd stops running when
    // teleop starts running. If you want the autonomous cmd to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autoCommand != null) {
      autoCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  // This function is called once when the robot enters Disabled mode.
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

}     // end robot.j class

