// flatbotArcadePID_v2_AM_final     Robot.j

// edited from Asid_1072 video 4 -- Cmd/Subsy framewk, PID for position
// all periodic in Robot.j, oper input in OI, Const for flatbot, gamepd
// as did in 1072-22, eliminated RC; purpose here -- demo that framework
// on flatbot: arcade drive, simple auto w/ PID, chooser using smtDashbd

// all OK [check joystick for axis 1 = throttle, 0/4 = turn, button 
// addr, encod direction; test kP etc on bench, auto chooser on SmtDash] 
// does auto chooser appear in LV default dashbd? no; PID OK on ground Y
// added in v.2 multiple auto cmd (chooser selected), seq of: drive, rot 180,
// drive back. edited to fix cmd in seq. not ending, test on flatbot OK.
// [lots of param value display in SmtDash to debug sequential auto cmd]

/* OI codes these button for manual straight drive to pre-set positions
button1.onTrue(new zeroDrivEncoder());
button3....(new GoToPosition(-24));
button2....(new GoToPosition(24));*/

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.subsystems.*;
import frc.robot.commands.*;
//import static frc.robot.Constant.*;

/**
 * This class is instanced by Main.j & automatically run
 * -- don't change name; 'Robot' works as instance name for
 * other class' reference to objects here 
 */
public class Robot extends TimedRobot {
  // no RContainer used; main instance configs set in robotInit;
  // operator interface is in OI vs. 'configButtonB()' when RC used;
  // declare local vars & const here, later could shift to Constant;
  // instance subsystem here (in roboInit vs. RC), config detail in subsys
  public static DriveMotorSubsys motorSubsys = new DriveMotorSubsys();

  public static OI oi; // define operatorInput, joystk defin & button bind

  // PID param in subsystem; backup also in Constant.j:
  // unit conversion for flatbot, 1 wheel rot, 18.7in = 10700 tick
  // 4.5 ft = 54in = ~32000
  // public static final double kDriveInch2Tick = 10700 / (6 * Math.PI);
  // public static final double kDriveTick2Inch = (6 * Math.PI) / 10700;

  // declare auto chooser, auto option in roboInit; sched choice in autoInit
  private SendableChooser<Command> autonChooser;

  /* robotInit runs when the robot is first started up and should be
   * used for initialization, instance basic subsys needed by cmd, configs
   * things done in RobotContainer before
   */
  @Override
  public void robotInit() {

    System.out.println("start robotInit");

    // default commands are commands that are always running;
    // only works here, tried in subsyst et. al. <-- fail on deploy
    motorSubsys.setDefaultCommand(new DriveWithPercent());

     oi = new OI();  // operator interface = user controls

 
    // also others in RC constructor shift here

    //... when Auto_ Mode enabled, Robot.auto_Init() calls selected cmd 
    // using its instance made here and schedules cmd it receives
    double autoDriveInch = 42;
    final Command simpleAuto = new GoToPosition(autoDriveInch);
    final Command simplePlus = new GoToPosition(autoDriveInch + 24);
    final SequentialCommandGroup autoSequence1 = new autoFwdRotBak();

    autonChooser = new SendableChooser<>();
    autonChooser.setDefaultOption("simpleAuto", simpleAuto);
    autonChooser.addOption("goFarther", simplePlus);
    autonChooser.addOption("fwd-Rot180-bak", autoSequence1);

    // if SD not enabled, does this appear in LV-DS's chooser? NO
    // v. tips doc how to add to LV dashbd chooser
    SmartDashboard.putData("Auton Selector", autonChooser);
    // sending data to chooser may require
    // NetworkTableInstance.getDefault().setUpdateRate(0.02);

    System.out.println("robot initialized");
  } // end robotInit

  /* robotPeriodic is called every robot packet, in all mode.
   * Use for data display / diagnostics to run during disabled,
   * autonomous, teleoperated, test -- send info to DS etc.
   * Scheduler call REQUIRED for any periodic to run
   * This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); // yes, needed!
    SmartDashboard.putNumber("leftEncode", GoToPosition.leftEnco);
    SmartDashboard.putNumber("rightEncod", GoToPosition.riteEnco);

    // 1072 gathers sensor data ?? from subsyst, view on dashbd
    // SmartDashboard.putData(Drivetrain.getInstance());
    // SmartDashboard.putData(Climber.getInstance());
    // SmartDashboard.putData(Intake.getInstance());
  } // end robotPeriodic

  // autonomousInit is called when autonomous first Enabled
  @Override
  public void autonomousInit() {
    // chooser instance created in roboInit
    if (autonChooser.getSelected() != null) {
      autonChooser.getSelected().schedule();
    }

    // for test can just go to position(inch)
    // final Command simpleAuto = new GoToPosition(targetDriveInch);
    // simpleAuto.schedule();
  } // end autoInit

  // autonomousPeriodic is called (~50 hz) during autonomous.
  // may be empty if cmds methods do all that's required
  @Override
  public void autonomousPeriodic() { 
  }

  // teleopInit is called at the start of operator control
  @Override // no obvious function
  public void teleopInit() {
  }

  // teleopPeriodic is called every (~20ms) during operator control
  @Override
  public void teleopPeriodic() {
  // can be empty if default cmd execute() does what's needed
  }

  /* disabledInit is called once each time the robot enters Disabled mode.
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

}  // end robot.j class
