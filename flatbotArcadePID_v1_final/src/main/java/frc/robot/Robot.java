// flatbotArcadePID_v1_final  ~2303

// edited from Asid_1072 video 4 -- Cmd/Subsy framewk, PID for position
// all periodic in Robot.j, oper input in OI, Const for flatbot, gamepd
// asid-like 1072-22, eliminated RC; purpose here -- demo framework on
// flatbot: arcade drive, simple auto w/ PID, chooser using dashbd

// 2303 -- all working
// todo in v.2 multiple auto cmd (chooser selected), sequential

/* OI has  button3.onTrue(new zeroDrivEncoder());
button5....(new GoToPosition(-24));
button6....(new GoToPosition(24)); */

package frc.robot;

// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.Command;
//import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.subsystems.*;
import frc.robot.commands.*;
import static frc.robot.Constant.*;

/**
 * This class is instanced by Main.j & automatically run
 * -- don't change name
 */
public class Robot extends TimedRobot {
  // no RContainer used, main instance config set in robotInit
  // operator interface is in OI vs. 'configButton' when RC used
  // declare local vars & const, later shift to Constant
  // instance subsystem here in roboInit vs. RC, detail in subsys file
  public static DriveMotorSubsys _motorSubsys = new DriveMotorSubsys();

  public static OI _oi; // has joystk defin & button bind

  // PID param now in Constant to keep Robot.j uncluttered
  // static final double kP = 0.3 ....

  // unit conversion for flatbot, 1 wheel rot, 18.7in = 10700 tick
  // public static final double kDriveInch2Tick = 10700 / (6 * Math.PI);
  // public static final double kDriveTick2Inch = (6 * Math.PI) / 10700;

  // declare auto chooser, define auto option, sched choice in autoInit
  private SendableChooser<Command> _autonChooser; //note Cmd typing

   // robotInit runs when the robot is first started up and should be
  //used for initialization, instance basic subsys needed by cmd, configs
   
  @Override
  public void robotInit() {

    System.out.println("start robotInit");
    // only works here, tried in subsyst, others fail on deploy to RIO
    _motorSubsys.setDefaultCommand(new DriveWithPercent());
    _oi = new OI();

    // default commands are commands that are always running; 1072 sets
    // here vs. in RC, also others in RC constructor shift here; I'd like
    // to set default cmd in subsys() constr? to unclutter this class. Failed:
    // CommandScheduler.getInstance().setDefaultCommand(_motorSubsys,
    // new DriveWithPercent());
    // CommandScheduler.getInstance().setDefaultCommand(Indexer.getInstance(),
    // new IndexerManual());

    // OI.getInstance();// instanced & named already, not sure why repeat?
   // DoubleSolenoid _pressure = new DoubleSolenoid(PneumModuleType.REVPH, 0, 4);
    // _pressure.set(DoubleSolenoid.Value.kForward);

    // param for auto drive to position (in.), import from Constant.j
    final Command _simpleAuto = new GoToPosition(autoDriveInch);
    final Command _simplePlus = new GoToPosition(autoDriveInch + 24);
    // private ParallelCommandGroup autoParallel;
    _autonChooser = new SendableChooser<>();
    _autonChooser.setDefaultOption("simpleAuto", _simpleAuto);
    _autonChooser.addOption("goFarther", _simplePlus);
    // _autonChooser.addOption("Two Ball Auton", Autons.TWO_BALL_AUTO);
    // _autonChooser.addOption("One Ball Auton", Autons.ONE_BALL_AUTO);

    // if SD not enabled, does this appear in LV-dash's chooser field? no
    SmartDashboard.putData("Auton Selector", _autonChooser);
    // sending data to chooser may require
    // NetworkTableInstance.getDefault().setUpdateRate(0.02);

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
    // 1072 gathers updated sensor from subsyst, puts on dashbd, etc.
    // SmartDashboard.putData(Drivetrain.getInstance());
    // SmartDashboard.putData(Climber.getInstance());
    // SmartDashboard.putData(Intake.getInstance());
  } // end robotPeriodic

  // autonomousInit is called at the start of autonomous
  @Override
  public void autonomousInit() {
    // chooser instanced above in roboInit, returns a Cmd
    if (_autonChooser.getSelected() != null) {
      _autonChooser.getSelected().schedule();
    }

    // for test just go to position(inch)
    // final Command _simpleAuto = new GoToPosition(targetDriveInch);
    // _simpleAuto.schedule();
  } // end autoInitß

  // autonomousPeriodic is called (~50 hz) during autonomous.
  // ?? if Periodics need explicit call to CS() in them or if
  // this is redundant; does auto cmd run despite this being empty? Y
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
    // if not, should some cmd go here;
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

} // end robo.j
