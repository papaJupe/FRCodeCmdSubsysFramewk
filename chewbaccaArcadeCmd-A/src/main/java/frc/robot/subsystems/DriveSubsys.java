// chewbaccaArcadeCmdA        DriveSubsys
// 
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// TO DO tune PID param with REV client

package frc.robot.subsystems;

//import static frc.robot.Constant.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.*;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsys extends SubsystemBase {

  // instance motor, make diff drive bundle
  // drive = 4 Neo motor
  public CANSparkMax _leftMaster = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax _leftSlave = new CANSparkMax(4, MotorType.kBrushless);
  public CANSparkMax _rightMaster = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax _rightSlave = new CANSparkMax(2, MotorType.kBrushless);

  private DifferentialDrive _diffDrive = new DifferentialDrive(_leftMaster, _rightMaster);

  public static SparkMaxPIDController _leftPIDControl;
  public SparkMaxPIDController _rightPIDControl;

  public static RelativeEncoder _leftEncoder; // used in roboInit/Period
  public static RelativeEncoder _rightEncoder;

  public static double kP = 0.15, kI = 0, kD = 0, kIz = 0, kFF = 0, kMaxOutput = 0.5, kMinOutput = -0.5;
  public static int inchTarget = 0; // drive goal inch, var sent to SD too

  /* CONSTRUCT a new Subsystem instance */
  public DriveSubsys() {
    /**
     * RestoreFactoryDefaults method can be used to reset the configuration
     * parameters of the SPARK MAX to their factory default state. If no argument
     * is passed, these parameters will not persist between power cycles. ??? if
     * these are defaults what does persist if these not put here ?
     */
    _leftMaster.restoreFactoryDefaults();
    _leftSlave.restoreFactoryDefaults();
    _rightMaster.restoreFactoryDefaults();
    _rightSlave.restoreFactoryDefaults();

    // invert R side?, example says: it's
    // usual to need to invert one side of drivetrain so positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side.

    _leftMaster.setInverted(false);
    _rightMaster.setInverted(true);

    _leftMaster.setIdleMode(IdleMode.kBrake);
    _rightMaster.setIdleMode(IdleMode.kBrake);

    _leftMaster.setOpenLoopRampRate(0.5);
    _rightMaster.setOpenLoopRampRate(0.5);

    // slaves follow, probably follow setInversion too
    _leftSlave.follow(_leftMaster);
    _rightSlave.follow(_rightMaster);

    /**
     * In order to use PID functionality, a SparkMaxPIDController object
     * is constructed by calling the getPIDController() method on an existing
     * CANSparkMax object
     */
    _leftPIDControl = _leftMaster.getPIDController();
    _rightPIDControl = _rightMaster.getPIDController();

    // relativ Encoder object created to get position values
    _leftEncoder = _leftMaster.getEncoder();
    _rightEncoder = _rightMaster.getEncoder();

     // inch distance = to 1 rotat, if 6 in. diam, geaRatio 1:8
     _leftEncoder.setPositionConversionFactor(Math.PI * 6 / 8); // ~2.2
     _rightEncoder.setPositionConversionFactor(Math.PI * 6 / 8); // ~2.2

    // set PID coefficients
    _leftPIDControl.setP(kP);
    _leftPIDControl.setI(kI);
    _leftPIDControl.setD(kD);
    _leftPIDControl.setIZone(kIz);
    _leftPIDControl.setFF(kFF);
    _leftPIDControl.setOutputRange(kMinOutput, kMaxOutput);

    _rightPIDControl.setP(kP);
    _rightPIDControl.setI(kI);
    _rightPIDControl.setD(kD);
    _rightPIDControl.setIZone(kIz);
    _rightPIDControl.setFF(kFF);
    _rightPIDControl.setOutputRange(kMinOutput, kMaxOutput);

  } // end constructor

  /**
   * controlling a Neo w/ PID requires its PIDcontroller object v.s.;
   * PIDController objects are commanded to a set point using their
   * setReference() method.
   * 
   * The first parameter is the value of the set point, whose units vary
   * depending on the control type set in the second parameter.
   * 
   * The second parameter is the control type -- can be one of four:
   * com.revrobotics.CANSparkMax.ControlType.kDutyCycle
   * com.revrobotics.CANSparkMax.ControlType.kPosition
   * com.revrobotics.CANSparkMax.ControlType.kVelocity
   * com.revrobotics.CANSparkMax.ControlType.kVoltage
   * m_pidController.setReference(rotations, CANSparkMax.ControlType.kPosition);
   */

  // both sides aim for same distance target via their own encoder.
  // param received here in inches
  // called in periodic execute() of GoToPosition cmd, so don't zeroEnco here
  public void goStratPosit(int target) {

    inchTarget = target;
    _leftPIDControl.setReference(inchTarget, CANSparkMax.ControlType.kPosition);
    _rightPIDControl.setReference(inchTarget, CANSparkMax.ControlType.kPosition);
  }
// one forward, other back, same distance
  public void rotate180(int target) {
    inchTarget = target;
    _leftPIDControl.setReference(inchTarget, CANSparkMax.ControlType.kPosition);
    _rightPIDControl.setReference(-inchTarget, CANSparkMax.ControlType.kPosition);
  }

  // method called from zeroDrivEncod cmd & GTP's endme()
  public void zeroEncoder() { // [pos,indx,timeout]
    _leftEncoder.setPosition(0);
    _rightEncoder.setPosition(0);
  }

  // class' method for cmds
  // should have different name than super's method to avoid confusion
  public void arcaDriv(double throttle, double turn) {
    // method from diff drive class works on instance of it
    _diffDrive.arcadeDrive(throttle, turn);
  }

  /**
   * Example command factory method.
   * 
   * @return a command
   */
  // public CommandBase exampleMethodCommand() {
  // // Inline construction of command goes here.
  // // Subsystem::RunOnce implicitly requires `this` subsystem.
  // return runOnce(
  // () -> {
  // /* one-time action goes here */
  // });
  // }

  /**
   * An example method querying a boolean state of the subsystem (for example, a
   * digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  // public boolean exampleCondition() {
  // // Query some boolean state, such as a digital sensor.
  // return false;
  // }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

public Command exampleMethodCommand() {
    return null;
}

  // @Override
  // public void simulationPeriodic() {
  // // This method will be called once per scheduler run during simulation
  // }

} // end subsys class
