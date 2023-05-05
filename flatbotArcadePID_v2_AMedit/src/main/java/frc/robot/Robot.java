// flatbotArcadePID_v2_AMedit     Robot.j

// edited from Asid_1072 video 4 -- Cmd/Subsy framewk, PID for position
// all periodic in Robot.j, oper input in OI, Const for flatbot, gamepd
// asid-like 1072-22, eliminated RC; purpose here -- demo framework on
// flatbot: arcade drive, simple auto w/ PID, chooser using dashbd

// done - OK [check joystick for axis 1 = throttle, 0 = turn, button 
// addr, encod direction; test kP etc on bench, auto chooser on SmtDash] 
// does auto chooser appear in DS's default dashbd? no; PID OK on ground? Y
// added in v.2 multiple auto cmd (chooser selected), sequential drive, (re)turn
// sequ cmd group not working as is, possibly cmd not finishing cleanly

/* OI has  
button3.onTrue(new zeroDrivEncoder());
button5....(new GoToPosition(-24));
button6....(new GoToPosition(24));
*/

package frc.robot;

// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.Command;
//import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.subsystems.*;
import frc.robot.commands.*;
import static frc.robot.Constant.*;

/**
 * This class is instanced by Main.j & automatically run
 * -- don't change name; 'Robot' works as instance name for
 * other class' reference 
 */
public class Robot extends TimedRobot {
  // no RContainer used; main instance config set in robotInit;
  // operator interface is in OI vs. 'configButton' when RC used;
  // declare local vars & const here, later shift to Constant;
  // instance subsystem here in roboInit vs. RC, config detail in subsys
  public static DriveMotorSubsys _motorSubsys = new DriveMotorSubsys();
// not clear why I can instance motor subsyst and not others here, eg OI?

  public static OI _oi; // hold operatorInput, joystk defin & button bind

  // PID param (later) in Constant to keep Robot.j uncluttered
  // static final double kP = 0.3 ....
// these in Constant.j now:
  // // unit conversion for flatbot, 1 wheel rot, 18.7in = 10700 tick
  // // 4.5 ft = 54in = ~32000 -- moved to subsystem class var
  // public static final double kDriveInch2Tick = 10700 / (6 * Math.PI);
  // public static final double kDriveTick2Inch = (6 * Math.PI) / 10700;

  // declare auto chooser, auto option in roboInit; sched choice in autoInit
  private SendableChooser<Command> _autonChooser;

  /**
   * robotInit runs when the robot is first started up and should be
   * used for initialization, instance basic subsys needed by cmd, configs
   * same things done in RoboCont when it was used
   */
  @Override
  public void robotInit() {

    System.out.println("start robotInit");

    // only works here, tried in subsyst, others -- fail on deploy to RRio
    _motorSubsys.setDefaultCommand(new DriveWithPercent());

    _oi = new OI();

    // default commands are commands that are always running; 1072 sets
    // here vs. in RC, also others in RC constructor shift here; attempt
    // to set default cmd in each subsys to unclutter this class failed:
    // CommandScheduler.getInstance().setDefaultCommand(_motorSubsys,
    // new DriveWithPercent());  maybe old syntax
    // CommandScheduler.getInstance().setDefaultCommand(Indexer.getInstance(),
    // new IndexerManual());

    // OI.getInstance();// instanced & named above, why repeat?
    // DoubleSolenoid _pressure = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0,
    // 4);
    // _pressure.set(DoubleSolenoid.Value.kForward);

    //... when Auto_ Mode activated, Robot.auto_Init() call selected cmd using its
    // instance from here and schedules cmd it receives
    
    final Command _simpleAuto = new GoToPosition(autoDriveInch);
    final Command _simplePlus = new GoToPosition(autoDriveInch + 24);
    final SequentialCommandGroup _autoSequence1 = new autoFwdRotBak();

    _autonChooser = new SendableChooser<>();
    _autonChooser.setDefaultOption("simpleAuto", _simpleAuto);
    _autonChooser.addOption("goFarther", _simplePlus);
    _autonChooser.addOption("fwd-Rot180-bak", _autoSequence1);

    // if SD not enabled, does this appear in LV-dash's chooser / DS? NO
    // v. mannCode-19 how to add to LV dash chooser instead
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
    CommandScheduler.getInstance().run(); // yes, needed!
    // 1072, et mann-19 gathers sensor data from subsyst, puts on dashbd
    // SmartDashboard.putData(Drivetrain.getInstance());
    // SmartDashboard.putData(Climber.getInstance());
    // SmartDashboard.putData(Intake.getInstance());
  } // end robotPeriodic

  // autonomousInit is called at the start of autonomous
  @Override
  public void autonomousInit() {
    // chooser instance comes from roboInit
    if (_autonChooser.getSelected() != null) {
      _autonChooser.getSelected().schedule();
    }

    // for test just go to position(inch)
    // final Command _simpleAuto = new GoToPosition(targetDriveInch);
    // _simpleAuto.schedule();
  } // end autoInit

  // autonomousPeriodic is called (~50 hz) during autonomous.
  // ?? if Periodics need explicit call to CS() in them or if
  // this is redundant; does auto cmd run despite this being empty?
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
    // is this line redundant ?; would it run default cmd w/o this? Y
   // CommandScheduler.getInstance().run();

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
