/*---------------------------------------------------------------------------
*  DriveMotorSubsys  edited from Asid1072 video 4
*  -- Cmd/Subsy framewk, PID for position
* all periodic in Robot.j, oper input in OI, const in Map                                                           
----------------------------------------------------------------------------*/

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

import frc.robot.Constant;

public class DriveMotorSubsys extends SubsystemBase {
  // CAN ID for flatbot
  public final WPI_TalonSRX _masterTalonL = new WPI_TalonSRX(4);;
  public final WPI_TalonSRX _slaveTalonL = new WPI_TalonSRX(3);;
  public final WPI_TalonSRX _masterTalonR = new WPI_TalonSRX(2);
  public final WPI_TalonSRX _slaveTalonR = new WPI_TalonSRX(1);

  public int _position_slot = 0; // used by PID init in GoToPos cmd

  // CONSTRUCTOR
  public DriveMotorSubsys() {
    controllerInit();
  }

  void controllerInit() { // initializes motor controller settings
    // set all to default
    _masterTalonL.configFactoryDefault();
    _slaveTalonL.configFactoryDefault();
    _masterTalonR.configFactoryDefault();
    _slaveTalonR.configFactoryDefault();

    // set the behavior when 0 output
    _masterTalonL.setNeutralMode(NeutralMode.Brake);
    _slaveTalonL.setNeutralMode(NeutralMode.Brake);
    _masterTalonR.setNeutralMode(NeutralMode.Brake);
    _slaveTalonR.setNeutralMode(NeutralMode.Brake);

    // set if the output direction is to be inverted
    _masterTalonL.setInverted(false);
    _slaveTalonL.follow(_masterTalonL); // follow the master
    _slaveTalonL.setInverted(InvertType.FollowMaster);

    _masterTalonR.setInverted(true);
    _slaveTalonR.follow(_masterTalonR);
    _slaveTalonR.setInverted(InvertType.FollowMaster);

    _masterTalonR.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constant.TIMEOUT);
    _masterTalonL.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constant.TIMEOUT);

    // should follow motor setting
    _masterTalonL.setSensorPhase(false);
    _masterTalonR.setSensorPhase(false);

    // need these for R & L masters, assume followers don't need
    // config min/max speeds, ramp, active kI zone for PID control

    // _masterTalonL.config_kP(_position_slot, Constant.kP_pos, Constant.TIMEOUT);
    // _masterTalonL.config_kI(_position_slot, Constant.kI_pos, Constant.TIMEOUT);
    // _masterTalonL.config_kD(_position_slot, Constant.kD_pos, Constant.TIMEOUT);
    // _masterTalonL.config_IntegralZone(_position_slot, Constant.integral_zone,
    // Constant.TIMEOUT);
    // _masterTalonL.configClosedloopRamp(0.5, 10);

    // _masterTalonR.config_kP(_position_slot, Constant.kP_pos, Constant.TIMEOUT);
    // _masterTalonR.config_kI(_position_slot, Constant.kI_pos, Constant.TIMEOUT);
    // _masterTalonR.config_kD(_position_slot, Constant.kD_pos, Constant.TIMEOUT);
    // _masterTalonR.config_IntegralZone(_position_slot, Constant.integral_zone,
    // Constant.TIMEOUT);
    // _masterTalonR.configClosedloopRamp(0.5, 10);

    // add these to set speed limit hi/lo
    // Config the peak and nominal outputs, 12V = 1
    // _masterTalonL.configNominalOutputForward(0, Constant.TIMEOUT);
    // _masterTalonL.configNominalOutputReverse(0, Constant.TIMEOUT);
    // _masterTalonL.configPeakOutputForward(1, Constant.TIMEOUT);
    // _masterTalonL.configPeakOutputReverse(-1, Constant.TIMEOUT);

    // Config the peak and nominal outputs, 12V = 1
    // _masterTalonR.configNominalOutputForward(0, Constant.TIMEOUT);
    // _masterTalonR.configNominalOutputReverse(0, Constant.TIMEOUT);
    // _masterTalonR.configPeakOutputForward(1, Constant.TIMEOUT);
    // _masterTalonR.configPeakOutputReverse(-1, Constant.TIMEOUT);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be
     * neutral within this range. See Table in Section 17.2.1 for native
     * units per rotation. ? like kI integ zone for all, or can use both?
     * presumably in encoder tick ?
     */
    // _masterTalonL.configAllowableClosedloopError(0, Constant.kPIDLoopIdx,
    // Constants.TIMEOUT);

    // _masterTalonR.configAllowableClosedloopError(0, Constant.kPIDLoopIdx,
    // Constants.TIMEOUT);

  } // end controllerInit

  // needed to instance wpi_talonsrx to work here
  DifferentialDrive _diffDrive = new DifferentialDrive(_masterTalonL, _masterTalonR);

  // both sides aim for same distance target via their own internal encoder
  // param received here in inches, so multiply by inch2tick factor for .set
  // called in periodic execute() of GoToPosition cmd, so don't zeroEnc here
  public void setPosition(int target) {
    // tick per wheelRot / inch per wheelRot
    int tickTarget = (int) (10700 / (6 * Math.PI));
    _masterTalonL.set(ControlMode.Position, tickTarget);
    _masterTalonR.set(ControlMode.Position, tickTarget);
  }

  public void zeroEncoder(int pos) {
    _masterTalonL.setSelectedSensorPosition(pos, 0, Constant.TIMEOUT);
    _masterTalonR.setSelectedSensorPosition(pos, 0, Constant.TIMEOUT);
  }

  // method from diff drive class works on instance of it
  public void arcadeDrive(double throttle, double turn) {
    _diffDrive.arcadeDrive(throttle, turn);
  }

  // @Override // set in Robot.j to keep all together; could be here instead
  // the command that is run when no other command is running
  // public void initDefaultCommand() {
  // setDefaultCommand(new DriveWithPercent());

}
