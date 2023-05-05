// chewbaccaArcadeCmdA   DriveSubsys
// 
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// TO DO tune PID param with REV client

package frc.robot.subsystems;

import static frc.robot.Constant.*;

import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMax.ControlType.*;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.*;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constant;


public class DriveSubsys extends SubsystemBase {

  // CAN bus ID
  // leftMasterID = 3;
  // leftSlaveID = 4;
  // rightMasterID = 1;
  // rightSlaveID = 2;

  // instance motor, make diff drive bundle
  // drive = 4 Neo motor
  public CANSparkMax _leftMaster = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax _leftSlave = new CANSparkMax(4, MotorType.kBrushless);
  public CANSparkMax _rightMaster = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax _rightSlave = new CANSparkMax(2, MotorType.kBrushless);

  private DifferentialDrive _diffDrive = new DifferentialDrive(_leftMaster, _rightMaster);

  private SparkMaxPIDController _leftPIDControl;
  private SparkMaxPIDController _rightPIDControl;

  public static RelativeEncoder _leftEncoder; // used in roboInit/Period
  public static RelativeEncoder _rightEncoder;

  public static double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
  public static double tickTarget = 0;  // drive goal in ticks, var sent to SD too

  //  set here after tuning in REVclient; could shift to Constant
  // these came from flatbot & talonSRX
  // static final double kP_pos = 0.3;
  // static final double kI_pos = 0.00015;
  // static final double kD_pos = 50.0;
  // static final double kF_pos = 0.0;
  // static final double kIzone = 1200;

  /* CONSTRUCT a new __Subsystem. */
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
        _leftSlave.follow (_leftMaster);
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

    // example PID coeffic untuned for this drive
    kP = 0.1; 
    kI = 1e-4;
    kD = 1; 
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = 0.3; 
    kMinOutput = -0.3;

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
     * PIDController objects are commanded to a set point using the 
     * setReference() method.
     * 
     * The first parameter is the value of the set point, whose units vary
     * depending on the control type set in the second parameter.
     * 
     * The second parameter is the control type -- can be one of four 
     * parameters:
     *  com.revrobotics.CANSparkMax.ControlType.kDutyCycle
     *  com.revrobotics.CANSparkMax.ControlType.kPosition
     *  com.revrobotics.CANSparkMax.ControlType.kVelocity
     *  com.revrobotics.CANSparkMax.ControlType.kVoltage
     * m_pidController.setReference(rotations, CANSparkMax.ControlType.kPosition);
     */
    
  // both sides aim for same distance target via their own encoder.
  // param received here in inches, so multiply by inch2tick factor for .set
  // called in periodic execute() of GoToPosition cmd, so don't zeroEnco here
  public void goStraightPosition(int target) {
    // tick per wheelRot / inch per wheelRot
    tickTarget = (target * Constant.kDriveInch2Tick);
    _leftPIDControl.setReference(tickTarget, CANSparkMax.ControlType.kPosition);
    _rightPIDControl.setReference(tickTarget, CANSparkMax.ControlType.kPosition);
  }

  public void rotate180(int target) {
    // tick per inch of wheelRot
    tickTarget = (target * kDriveInch2Tick);
    _leftPIDControl.setReference(tickTarget, CANSparkMax.ControlType.kPosition);
    _rightPIDControl.setReference(-tickTarget, CANSparkMax.ControlType.kPosition);
  }

  // method called from zeroDrivEncod cmd & GTP's endme()
  public void zeroEncoder(int pos) { // [pos,indx,timeout]
    _leftEncoder.setPosition(pos);
    _rightEncoder.setPosition(pos);
  }

  // class' method for cmds to call on an instance of 'this' --
  // should have different name than super's method to avoid confusion
  public void arcaDriv(double throttle, double turn) {
    // method from diff drive class works on instance of it
    _diffDrive.arcadeDrive(throttle, turn);
  }

  /**
   * Example command factory method.
   * @return a command
   */
  // public CommandBase exampleMethodCommand() {
  //   // Inline construction of command goes here.
  //   // Subsystem::RunOnce implicitly requires `this` subsystem.
  //   return runOnce(
  //       () -> {
  //         /* one-time action goes here */
  //       });
  // }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  // public boolean exampleCondition() {
  //   // Query some boolean state, such as a digital sensor.
  //   return false;
  // }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  // @Override
  // public void simulationPeriodic() {
  //   // This method will be called once per scheduler run during simulation
  // }

}  // end subsys class
