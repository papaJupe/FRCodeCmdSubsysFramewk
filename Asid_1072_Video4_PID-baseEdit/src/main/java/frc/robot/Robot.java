
// edited from Asid1072 video 4 -- Cmd/Subsy framewk, PID for position
// all periodic in Robot.j, oper input in OI, Const
// asid,like 1072-22, eliminated RC; purpose here -- demo this
// framewk for arcade drive, choose auto w/ PID, display data to dashbd

// todo: use Phoe Tune to set kP etc, then put into code, check joystick
// for axis 1 = throttle, 0 = turn, else change constant

package frc.robot;

// imports et al from team 1072_22 <--uses Robot.j for Periodics etc
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.CommandBase;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

  // operator interface is in OI vs. 'configButton' in RC
  public static OI _oi; // shift these to roboinit?
  // declare various local vars & declare then instance
  // needed subsystem here in rInit vs. RC
  public static DriveMotorSubsys _motorSubsys;
  // public static boolean thirdPoint = false;
  // private boolean wasAuto = false;
  // private ParallelCommandGroup autoShooter;
  // declare auto chooser, define auto option, sched choice autoInit?
  // private SendableChooser<CommandBase> _autonChooser;

  /**
   * robotInit runs when the robot is started up and should be
   * used for initialization.
   */
  @Override
  public void robotInit() {
    System.out.println("start robotinit");
    _motorSubsys = new DriveMotorSubsys();
    _oi = new OI();

    // default commands are commands that are always running on the robot
    // seems like every subsys needing a Periodic should get one;
    // done here vs. RC, also others in RC constructor shift here
    // didn't like TestMotors.getInstance() even tho one existed
    CommandScheduler.getInstance().setDefaultCommand(_motorSubsys, new DriveWithPercent());
    // CommandScheduler.getInstance().setDefaultCommand(Indexer.getInstance(), new
    // IndexerManual());
    // CommandScheduler.getInstance().setDefaultCommand(Intake.getInstance(), new
    // IntakeManual());

    // OI.getInstance(); //instanced already, not sure purpose of this
    // DoubleSolenoid _pressure = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0,
    // 4);
    // _pressure.set(DoubleSolenoid.Value.kForward);

    // _autonChooser = new SendableChooser<>();
    // _autonChooser.setDefaultOption("Five Ball Auton", Autons.FIVE_BALL_AUTO);
    // _autonChooser.addOption("Three Ball Auton", Autons.THREE_BALL_AUTO);
    // _autonChooser.addOption("Two Ball Auton", Autons.TWO_BALL_AUTO);
    // _autonChooser.addOption("One Ball Auton", Autons.ONE_BALL_AUTO);
    // SmartDashboard.putData("Auton Selector", _autonChooser);

    // // NetworkTableInstance.getDefault().setUpdateRate(0.02);

    System.out.println("robot initialized");
  } // end robotInit

  /**
   * robotPeriodic is called every robot packet, no matter the mode.
   * Use for items like diagnostics that you want run during disabled,
   * autonomous, teleoperated and test, send info to DS etc
   * Sched. call REQUIRED if using RC class, not sure if you don't
   * This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating,
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); // not sure needed
    // 1072 gathers updated sensor from subsyst puts on dashbd,
    // et al then sends to DS, not sure how putData works
    // SmartDashboard.putData(Drivetrain.getInstance());
    // SmartDashboard.putData(Climber.getInstance());
    // SmartDashboard.putData(Intake.getInstance());
  } // end robotPeriodic

  // autonomousInit is called at the start of autonomous
  @Override
  public void autonomousInit() {
    // chooser instanced above in roboInit
    // consider if _getSelected() != null, then
    // _autonChooser.getSelected().schedule();
    // for test just go to position ()
    final Command _simpleAuto = new GoToPosition(48);
    _simpleAuto.schedule();

  }

  // autonomousPeriodic is called (~20ms) during autonomous.
  // ?? all __Periodics expect call to CS() in them
  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  }

  // teleopInit is called at the start of operator control
  @Override
  public void teleopInit() {

  }

  // teleopPeriodic is called every (~20ms) during operator control
  @Override
  public void teleopPeriodic() {
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
