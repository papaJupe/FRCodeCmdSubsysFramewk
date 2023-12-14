// chewbaccaArcadeCmd-B               DriveSubsys.j

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.*;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsys extends SubsystemBase {

  //   CAN bus ID for flatterbot aka chewbacca
  // leftMasterID = 3;
  // leftSlaveID = 4;
  // rightMasterID = 1;
  // rightSlaveID = 2;

  // instance motor, joystick, drive objects
  // 4 Neo motor on SparkMax controllers
  private CANSparkMax leftMaster = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax leftSlave = new CANSparkMax(4, MotorType.kBrushless);
  private CANSparkMax rightMaster = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax rightSlave = new CANSparkMax(2, MotorType.kBrushless);

  private DifferentialDrive _diffDrive = 
                         new DifferentialDrive(leftMaster, rightMaster);

  private SparkMaxPIDController leftPIDControl;
  private SparkMaxPIDController rightPIDControl;

  public RelativeEncoder leftEncoder;
  public RelativeEncoder rightEncoder;

  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
  

  // these were good numbers for flatbot's' CIM+talonSRX
  // static final double kP_pos = 0.3;
  // static final double kI_pos = 0.00015;
  // static final double kD_pos = 50.0;
  // static final double kF_pos = 0.0;
  // static final double kIzone = 1200;

  //  new #'s set below after tuning in revHardwrClient

  
  /** CONSTRUCT the new Subsystem. */
  public DriveSubsys() {
 /**
     * RestoreFactoryDefaults method can be used to reset the config
     * parameters of the SPARK MAX to their factory default. If no argument
     * is passed, these parameters will not persist between power cycles ???
     */
    leftMaster.restoreFactoryDefaults();
    leftSlave.restoreFactoryDefaults();
    rightMaster.restoreFactoryDefaults();
    rightSlave.restoreFactoryDefaults();

    // invert R side?, example says:
    // usual to need to invert one side of drivetrain so positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side.

    leftMaster.setInverted(false);
    rightMaster.setInverted(true);

    leftMaster.setIdleMode(IdleMode.kBrake);
    rightMaster.setIdleMode(IdleMode.kBrake);

  // gentle start from joystick movement
    leftMaster.setOpenLoopRampRate(0.5);
    rightMaster.setOpenLoopRampRate(0.5);

   // slaves follow, probably follow setInversion too
   leftSlave.follow (leftMaster);
   rightSlave.follow(rightMaster);

    // Encoder object determines position values
    leftEncoder = leftMaster.getEncoder();
    rightEncoder = rightMaster.getEncoder();

    // to convert SparkMax tick count to distance inches of one revol.
    // divide 6" wheel circumf. by gear ratio 8:1:
    leftEncoder.setPositionConversionFactor(Math.PI * 6/8);
    rightEncoder.setPositionConversionFactor(Math.PI * 6/8);

    /* In order to use PID functionality, a SparkMaxPIDController 
     * object is constructed by calling the getPIDController()  
     * method on an existing CANSparkMax object (motor)
     */
    leftPIDControl = leftMaster.getPIDController();
    rightPIDControl = rightMaster.getPIDController();

    // PID coefficients untuned for drive; edit here
    kP = 0.1; 
    kI = 1e-4;
    kD = 1; 
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = 0.5; 
    kMinOutput = -0.5;

    // set PID coefficients
    leftPIDControl.setP(kP);
    leftPIDControl.setI(kI);
    leftPIDControl.setD(kD);
    leftPIDControl.setIZone(kIz);
    leftPIDControl.setFF(kFF);
    leftPIDControl.setOutputRange(kMinOutput, kMaxOutput);

    rightPIDControl.setP(kP);
    rightPIDControl.setI(kI);
    rightPIDControl.setD(kD);
    rightPIDControl.setIZone(kIz);
    rightPIDControl.setFF(kFF);
    rightPIDControl.setOutputRange(kMinOutput, kMaxOutput);

    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forwd", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
    SmartDashboard.putNumber("Set Rotations", 0);

  } // end constructor

   /* PIDController objects are commanded to a set point using the 
     * SetReference() method.
     * 
     * The first parameter is the value of the set point, whose units vary
     * depending on the control type you set in the second parameter.
     * 
     * The second parameter is the control type -- can be set to one of four 
     * values:
     *  com.revrobotics.CANSparkMax.ControlType.kDutyCycle
     *  com.revrobotics.CANSparkMax.ControlType.kPosition
     *  com.revrobotics.CANSparkMax.ControlType.kVelocity
     *  com.revrobotics.CANSparkMax.ControlType.kVoltage
     *  example: m_pidControl.setReference(rotations,
                                      CANSparkMax.ControlType.kPosition);
     */
    
    // SmartDashboard.putNumber("SetPoint", rotations);
    // SmartDashboard.putNumber("ProcessVariable", m_encoder.getPosition());
  
  // both sides aim for same distance target via their own encoder.
  // param received here in inches, controller config. converts to ticks
  // called in periodic execute() of GoToPosition cmd, so don't zeroEnco here
  public void goStraightPosition(double target) {
      leftPIDControl.setReference(target,CANSparkMax.ControlType.kPosition);
      rightPIDControl.setReference(target,CANSparkMax.ControlType.kPosition);
  }

  // left goes fwd, right back
  public void rotate180(double target) {    
    leftPIDControl.setReference(target, CANSparkMax.ControlType.kPosition);
    rightPIDControl.setReference(-target, CANSparkMax.ControlType.kPosition);
  }

  // method called from zeroDrivEncod cmd & GTP's end()
  public void zeroEncoder() { // [pos,indx,timeout]
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }











  // for clarity, a class method for operations on a class instance
  // should have different name than its super's method;
  // diffDrive aD method internally inverts turn sign, so I invert here so 
  // that positive rt. stick movement turns CW
  public void arcaDriv(double throttle, double turn) {
    // method from diff drive super class
    _diffDrive.arcadeDrive(throttle, -turn);
  }

  /**
   * example method querying boolean state of a subsystem
   * @return 
   * value of some boolean subsystem state, such as a digital sensor
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  
  /** not used in this subsystem
   * Example command factory method, but 'this' is not valid in test below;
   * so would this be used for a subsyst to cmd another subsys. operation?
      * @return a command  */
 // public CommandBase exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem
    // but code below fails.
    // return runOnce(   /* one-time action goes here */
    //    // () -> {m_Subsystem.zeroEncoder();}, RCstub.m_Subsystem);
    //    this::zeroEncoder, this);
  //}

}  // end class
