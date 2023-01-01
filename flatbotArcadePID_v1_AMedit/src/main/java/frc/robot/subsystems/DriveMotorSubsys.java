/*----------------------------------------------------------
*  DriveMotorSubsys  edited from Asid1072 video 4
*  -- Cmd/Subsy framewk, PID for position
* all periodic in Robot.j, oper input in OI.j, constant in Constant.j                                                           
--------------------------------------------------------------*/

package frc.robot.subsystems;

/**  subsystems import Classes for hardware and action bundles
 *   that have methods needed to make hardware do things
 */

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.*;

// import frc.robot.Robot; static import would reduce code volume
import frc.robot.Constant;
import frc.robot.commands.*;

public class DriveMotorSubsys extends SubsystemBase {
  // CAN ID for flatbot
  public final WPI_TalonSRX leftMaster = new WPI_TalonSRX(3);
  public final WPI_TalonSRX leftSlave = new WPI_TalonSRX(4);

  public final WPI_TalonSRX rightMaster = new WPI_TalonSRX(1);
  public final WPI_TalonSRX rightSlave = new WPI_TalonSRX(2);

  public int _position_slot = 0; // used by PID init in GoToPos cmd

  // needed to instance motor as wpi_talonSRX to work for DD param
  DifferentialDrive _diffDrive = new DifferentialDrive(leftMaster, rightMaster);

  // set here after tuning in PT; could shift to Constant when finalized
  static final double kP_pos = 0.3;
  static final double kI_pos = 0.00015;
  static final double kD_pos = 50.0;
  static final double kF_pos = 0.0;
  static final double kIzone = 1200;

  // CONSTRUCTOR
  public DriveMotorSubsys() {
    controllerInit();
  }

  void controllerInit() { // initializes motor controller settings
    // set all to default
    leftMaster.configFactoryDefault();
    leftSlave.configFactoryDefault();
    rightMaster.configFactoryDefault();
    rightSlave.configFactoryDefault();

    // set the behavior when resting
    leftMaster.setNeutralMode(NeutralMode.Brake);
    leftSlave.setNeutralMode(NeutralMode.Brake);
    rightMaster.setNeutralMode(NeutralMode.Brake);
    rightSlave.setNeutralMode(NeutralMode.Brake);

    // slave should follow
    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    // set if the output direction needs to be inverted
    leftMaster.setInverted(true);
    leftSlave.setInverted(InvertType.FollowMaster);
    rightMaster.setInverted(false);
    rightSlave.setInverted(InvertType.FollowMaster);

    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
        0, Constant.TIMEOUT);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
        0, Constant.TIMEOUT);

    // should follow motor setting; unclear if true means it does or not
    leftMaster.setSensorPhase(true);
    rightMaster.setSensorPhase(true);

    // reset encoders to zero
    leftMaster.setSelectedSensorPosition(0, 0, 20);
    rightMaster.setSelectedSensorPosition(0, 0, 20);
    // armMotor.setSelectedSensorPosition(0, 0, 10);

    // need these for R & L masters, assume followers don't need
    // likely redudant, same as defaults, here to adjust PRN
    // config min/max speeds, ramp, active kI zone for PID control

    leftMaster.config_kP(_position_slot, kP_pos, Constant.TIMEOUT);
    leftMaster.config_kI(_position_slot, kI_pos, Constant.TIMEOUT);
    leftMaster.config_kD(_position_slot, kD_pos, Constant.TIMEOUT);
    leftMaster.config_IntegralZone(_position_slot, kIzone, Constant.TIMEOUT);
    leftMaster.configClosedloopRamp(0.2, 20);

    rightMaster.config_kP(_position_slot, kP_pos, Constant.TIMEOUT);
    rightMaster.config_kI(_position_slot, kI_pos, Constant.TIMEOUT);
    rightMaster.config_kD(_position_slot, kD_pos, Constant.TIMEOUT);
    rightMaster.config_IntegralZone(_position_slot, kIzone, Constant.TIMEOUT);
    rightMaster.configClosedloopRamp(0.2, 20);

    // Config the peak and nominal (min) outputs, 12V = 1.0
    leftMaster.configNominalOutputForward(0, Constant.TIMEOUT);
    leftMaster.configNominalOutputReverse(0, Constant.TIMEOUT);
    leftMaster.configPeakOutputForward(0.3, Constant.TIMEOUT);
    leftMaster.configPeakOutputReverse(-0.3, Constant.TIMEOUT);

    rightMaster.configNominalOutputForward(0, Constant.TIMEOUT);
    rightMaster.configNominalOutputReverse(0, Constant.TIMEOUT);
    rightMaster.configPeakOutputForward(0.3, Constant.TIMEOUT);
    rightMaster.configPeakOutputReverse(-0.3, Constant.TIMEOUT);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be
     * neutral within this range. ? like kI integ zone for all, or can use
     * both? presumably in encoder tick ?
     */
    // leftMaster.configAllowableClosedloopError(slot kPIDLoopIdx, dbl N,
    // int ms.);
    leftMaster.configAllowableClosedloopError(0, 50, 20);
    rightMaster.configAllowableClosedloopError(0, 50, 20);

    // dampen abrupt starts in manual control
    leftMaster.configOpenloopRamp(0.2, 20);
    rightMaster.configOpenloopRamp(0.2, 20);
    // overrides default of 0.02 I think ? put in OI?
    // drive.setDeadband(0.04);
  } // end controllerInit

  // both sides aim for same distance target via their own external encoder.
  // param received here in inches, so multiply by inch2tick factor for .set
  // called in periodic execute() of GoToPosition cmd, so don't zeroEnco here
  public void setPosition(int target) {
    // tick per wheelRot / inch per wheelRot
    int tickTarget = (int) (target * 10700 / (6 * Math.PI));
    leftMaster.set(ControlMode.Position, tickTarget);
    rightMaster.set(ControlMode.Position, tickTarget);
  }

  public void zeroEncoder(int pos) { // [pos,indx,timeout]
    leftMaster.setSelectedSensorPosition(pos, 0, Constant.TIMEOUT);
    rightMaster.setSelectedSensorPosition(pos, 0, Constant.TIMEOUT);
  }

  // class method for cmds to call on an instance of 'this' --
  // should have different name than super's method to avoid confusion
  public void arcaDriv(double throttle, double turn) {
    // method from diff drive class works on instance of it
    _diffDrive.arcadeDrive(throttle, turn);
  }

  // @Override // was set in Robot.j now here.
  // the command that is run when no other command is
  public void initDefaultCommand() {
    setDefaultCommand(new DriveWithPercent());
  }
} // end DrivMotoSubs
