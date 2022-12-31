// flatbotArcadePID_v1_AMedit

// edited from Asid_1072 video 4 -- Cmd/Subsy framewk, PID for position
// all periodic in Robot.j, oper input in OI, Const for flatbot, gamepd
// asid,like 1072-22, eliminated RC; purpose here -- demo w/ flatbot
// framewk for arcade drive, simple auto w/ PID, chooser using dashbd

// todo in v.2 multiple auto cmd (chooser selected) sequential, (re)turn

// toDo here: check joystick for axis 1 = throttle, 0 = turn, button 
// addr, encod direction; test kP etc on bench, re-tune on ground PRN

package frc.robot;

// imports et al from team 1072_22 <--uses Robot.j for Periodics etc
// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

// import frc.robot.auto.Autons; // define many cmdSequence
// one will be selected by autonChooser

import frc.robot.subsystems.*;
import frc.robot.commands.*;

/**
 * This class is instanced by Main.j & automatically run
 * -- don't change name
 */
public class Robot extends TimedRobot {
  // no RContainer used, defaultCmd etc. set in robotInit
  // operator interface is in OI vs. 'configButton' if RC used
  // declare local vars & const, later shift to Constant
  // instance subsystem here in roboInit vs. RC, detail in subsys
  public static DriveMotorSubsys _motorSubsys;

  public static OI _oi; // instance joystk & button bind

  // PID param now in drive subsys class var. to keep Robot.j uncluttered
  // static final double kP = 0.3 ....

  // param for auto drive to position (in.), set here for testing
  int targetDriveInch = 54;
  // unit conversion for flatbot, 1 wheel rot, 18.7in = 10700 tick
  // 4.5 ft = 54in = ~32000
  public static final double kDriveInch2Tick = 10700 / (6 * Math.PI);
  public static final double kDriveTick2Inch = (6 * Math.PI) / 10700;

  private final Command _simpleAuto = new GoToPosition(targetDriveInch);
  private final Command _simplePlus = new GoToPosition(targetDriveInch + 24);
  // private ParallelCommandGroup autoParallel;

  // declare auto chooser, define auto option, sched choice in autoInit
  private SendableChooser<Command> _autonChooser;

  /**
   * robotInit runs when the robot is first started up and should be
   * used for initialization, instance basic subsys needed by cmd, configs
   */
  @Override
  public void robotInit() {

    System.out.println("start robotInit");
    _motorSubsys = new DriveMotorSubsys();
    _oi = new OI();

    // default commands are commands that are always running; 1072 sets
    // here vs. in RC, also others in RC constructor shift here; I prefer
    // to set default cmd in each subsys to unclutter this class.
    // CommandScheduler.getInstance().setDefaultCommand(_motorSubsys,
    // new DriveWithPercent());
    // CommandScheduler.getInstance().setDefaultCommand(Indexer.getInstance(),
    // new IndexerManual());

    // OI.getInstance();// instanced & named already, not sure why again?
    // DoubleSolenoid _pressure = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0,
    // 4);
    // _pressure.set(DoubleSolenoid.Value.kForward);

    _autonChooser = new SendableChooser<>();
    _autonChooser.setDefaultOption("simpleAuto", _simpleAuto);
    _autonChooser.addOption("go Farther", _simplePlus);
    // _autonChooser.addOption("Two Ball Auton", Autons.TWO_BALL_AUTO);
    // _autonChooser.addOption("One Ball Auton", Autons.ONE_BALL_AUTO);

    // if not sent to SD's chooser field, does it appear in DS field?
    SmartDashboard.putData("Auton Selector", _autonChooser);
    // sending data to chooser may require
    NetworkTableInstance.getDefault().setUpdateRate(0.02);

    System.out.println("robot initialized");
  } // end robotInit

  /**
   * robotPeriodic is called every robot packet, no matter the mode.
   * Use for items like diagnostics that you want run during disabled,
   * autonomous, teleoperated, test, send info to DS etc.
   * Sched. call REQUIRED
   * This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating,
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); // yes: needed!
    // 1072 gathers updated sensor from subsyst, puts on dashbd,
    // et al, not sure how putData works
    // SmartDashboard.putData(Drivetrain.getInstance());
    // SmartDashboard.putData(Climber.getInstance());
    // SmartDashboard.putData(Intake.getInstance());
  } // end robotPeriodic

  // autonomousInit is called at the start of autonomous
  @Override
  public void autonomousInit() {
    // chooser instanced above in roboInit
    // consider
    if (_autonChooser.getSelected() != null) {
      _autonChooser.getSelected().schedule();
    }

    // for test just go to position(inch)
    // final Command _simpleAuto = new GoToPosition(targetDriveInch);
    // _simpleAuto.schedule();
  }

  // autonomousPeriodic is called (~50 hz) during autonomous.
  // ?? if Periodics need call to CS() in them or if
  // this is redundant
  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  }

  // teleopInit is called at the start of operator control
  @Override // no obvious function
  public void teleopInit() {

  }

  // teleopPeriodic is called every (~20ms) during operator control
  @Override
  public void teleopPeriodic() {
    // is this line redundant ?; should run default cmd w/o this
    CommandScheduler.getInstance().run();
  }

  /**
   * disabledInit is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  // disabledPeriodic is called every robot packet while disabled
  @Override
  public void disabledPeriodic() {
  }

  // testPeriodic is called periodically during test mode
  @Override
  public void testPeriodic() {
  }

}
