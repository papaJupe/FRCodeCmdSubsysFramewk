// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.commands.*;
import frc.robot.subsystems.DriveSubsys;
import static frc.robot.subsystems.DriveSubsys.*;

/**
 * The VM is configured to automatically run this class, call the functions of
 * each mode, as described in the TimedRobot documentation. If you change name
 * of this class or the package after creating this project, you must also
 * update the build.gradle file in the project.
 */
public class Robot extends TimedRobot {

  private static final int autoDriveInch = 24;
  public static DriveSubsys _myDrive = new DriveSubsys();
  public static OI _operatorInterface;

 // private Command _autoCommand;  // if using LV dashbd, instance in autoInit.switch

  // declare auto chooser, auto option in roboInit; sched choice in autoInit
  private SendableChooser<Command> _autonChooser;
 
// or to send /get choice from LV dash
// SmartDashboard.putStringArray("Auto List", {"drive Forward", "drive Back", "somethinElse"};
// then in ? AutoInit? v.i.

  /**
   * This function is run when the robot is first started and should be used for
   * initialization.
   */
  @Override
  public void robotInit() {
    // RobotContainer formerly held OI instance, button bindings (now in OI class),
    // and put autonomous chooser on the dashboard (now in Robot.j, autoInit).
    // m_robotContainer = new RobotContainer();
    // object instances [drive, OI, autoCmd, chooser for auto] now setup here
    
    System.out.println("start robotInit");

     _operatorInterface = new OI(); // hold oper Input, joystk defin & button bind

     // only works here, tried in subsyst, others -- fail on deploy to RRio
     _myDrive.setDefaultCommand(new DriveWithPercent());
     // default commands are commands that are always running; 1072 sets
    // here vs. in RC, but this syntax fails in '22-23; also attempt
    // to set default cmd in each subsys to unclutter this class failed:
    // CommandScheduler.getInstance().setDefaultCommand(_motorSubsys,
    // new DriveWithPercent());  maybe old syntax
    // CommandScheduler.getInstance().setDefaultCommand(Indexer.getInstance(),
    // new IndexerManual());

    // DoubleSolenoid _valve = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 4);
    // _valve.set(DoubleSolenoid.Value.kForward);

    // when Auto_ Mode activated, Robot.auto_Init() call selected cmd using its
    // instance from here and schedules cmd it receives
    
    final Command _simpleAuto = new GoToPosition(autoDriveInch);
    final Command _simplePlus = new GoToPosition(autoDriveInch + 24);
    final SequentialCommandGroup _autoSequence1 = new autoFwdRotBak();

    _autonChooser = new SendableChooser<>();
    _autonChooser.setDefaultOption("simpleAuto", _simpleAuto);
    _autonChooser.addOption("goFarther", _simplePlus);
    _autonChooser.addOption("fwd-Rot180-bak", _autoSequence1);
    
        // sending data to chooser may require
        NetworkTableInstance.getDefault();

    // if SD not enabled, does this appear in LV-dash's chooser / DS? NO
    // v. mannCode-19 how to add to LV dash chooser instead
    SmartDashboard.putData("Auton Selector", _autonChooser);

    // display PID coefficients on SmartDashboard
  // SmartDashboard.putNumber("P Gain", kP);
  // SmartDashboard.putNumber("I Gain", kI);
  // SmartDashboard.putNumber("D Gain", kD);
  // SmartDashboard.putNumber("I Zone", kIz);
  // SmartDashboard.putNumber("Feed Forwd", kFF);
  // SmartDashboard.putNumber("Max Output", kMaxOutput);
  // SmartDashboard.putNumber("Min Output", kMinOutput);
  // SmartDashboard.putNumber("Set Rotations", 0);

   // to follow live value of position would need in roboPeriod:
   SmartDashboard.putNumber("SetPoint", tickTarget);
   SmartDashboard.putNumber("L encod Pos", _leftEncoder.getPosition());

    System.out.println("robot initialized");

  } // end robotInit()

  /**
   * This function is called every 20 ms, no matter the mode. Use this for tasks
   * like diagnostics that you want ran during disabled, autonomous, teleoperated
   * and test. it runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled commands, running already-scheduled commands, removing
    // finished or interrupted commands, and running subsystem periodic() methods.
    // CS must be called from the robot's periodic block in order for anything 
    // in the Command-based framework to work.
    SmartDashboard.putNumber("L encod Pos", _leftEncoder.getPosition());
    CommandScheduler.getInstance().run();
  }

      //to reset pid values while running; would this work in disabled mode?
      // // read PID coefficients from SmartDashboard
      // double p = SmartDashboard.getNumber("P Gain", 0);
      // double i = SmartDashboard.getNumber("I Gain", 0);
      // double d = SmartDashboard.getNumber("D Gain", 0);
      // double iz = SmartDashboard.getNumber("I Zone", 0);
      // double ff = SmartDashboard.getNumber("Feed Forward", 0);
      // double max = SmartDashboard.getNumber("Max Output", 0);
      // double min = SmartDashboard.getNumber("Min Output", 0);
  
      // if PID coefficients on SmartDashboard have changed, write new values
      // if((p != kP)) { m_pidController.setP(p); kP = p; }
      // if((i != kI)) { m_pidController.setI(i); kI = i; }
      // if((d != kD)) { m_pidController.setD(d); kD = d; }
      // if((iz != kIz)) { m_pidController.setIZone(iz); kIz = iz; }
      // if((ff != kFF)) { m_pidController.setFF(ff); kFF = ff; }
      // if((max != kMaxOutput) || (min != kMinOutput)) { 
      //   m_pidController.setOutputRange(min, max); 
      //   kMinOutput = min; kMaxOutput = max; }
  
  
  @Override
  public void autonomousInit() {
    // chooser instance placed on SmtDash by roboInit
    if (_autonChooser.getSelected() != null) {
      _autonChooser.getSelected().schedule();
    }
    // for LV chooser instead
  //String autoChoice = SmartDashboard.getString("Auto Selector", "drive Forward");
  // would make dF the default choice
  // switch(autoChoice) {
  //case "drive Forward":
  // set cmd var here
  //case "drive Back":
  // other choice
  //case "somethinElse":
  // turd choic

  }  //end roboInit

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the auto CMD stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    // if (_autoCommand != null) {
    //   _autoCommand.cancel();
    // }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

  }

    /** This function is called once each time the robot enters Disabled mode. */
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

}  // end robot.j class
