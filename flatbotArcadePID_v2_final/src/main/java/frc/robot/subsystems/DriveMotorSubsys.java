/* flatbotArcadePID v2

*  DriveMotorSubsys edited from Asid1072 video 4
*  -- Cmd/Subsy framewk, PID for position
* all periodic in Robot.j, oper input in OI.j, constant in C.j                                                           
--------------------------------------------------------------*/

package frc.robot.subsystems;

/** subsystems import Classes that have methods they
 * can use themselves or provide to commands that
 * require them when controlling this subsystem
 */

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.*;

// static import Const reduces code volume; but 
// for sparsest code, few are used, most specified inline here
// import static frc.robot.Constant.*;

public class DriveMotorSubsys extends SubsystemBase {
  // CAN ID for flatbot
  public final WPI_TalonSRX leftMaster = new WPI_TalonSRX(3);
  private final WPI_TalonSRX leftSlave = new WPI_TalonSRX(4);

  public final WPI_TalonSRX rightMaster = new WPI_TalonSRX(1);
  private final WPI_TalonSRX rightSlave = new WPI_TalonSRX(2);

   // unit conversion for flatbot, 1 wheel rot = 18.7in = 10700 tick
  // 4.5 ft = 54in = ~32000
   private static final double kDriveInch2Tick = 10700 / (6 * Math.PI);
  // public static final double kDriveTick2Inch = (6 * Math.PI) / 10700;
// used by PID init & GoToPos cmd
  private static final int position_slot = 0; 
  private static final int TIMEOUT = 20;

  // needed to instance motor as wpi_talonSRX to work as DD param
  DifferentialDrive diffDrive = new DifferentialDrive(leftMaster, rightMaster);

  // set here after tuning in PhoeTuner; could use from Constant.j, but
  // only ever used for this subsystem, so this keeps code the simplest
  static final double kP_pos = 0.3;
  static final double kI_pos = 0.00015;
  static final double kD_pos = 50.0;
  static final double kF_pos = 0.0;
  static final double kIzone = 1200;

  // CONSTRUCTOR
  public DriveMotorSubsys() {
    controllerInit(); // ?? why cI() code not put in constructor
   }

  void controllerInit() { // initialize motor controller settings
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
 
 // all motors need enableCurrentLimit and ConfigContinuousCurrentLimit()
 // and ConfigPeakCurrentLimit(0) at //https://api.ctr-electronics.com/phoenix/release/java/com/ctre/phoenix/motorcontrol/can/TalonSRX.html#configContinuousCurrentLimit(int)
    // slave should follow
    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    // set if the output direction needs to be inverted
    leftMaster.setInverted(true);
    leftSlave.setInverted(InvertType.FollowMaster);
    rightMaster.setInverted(false);
    rightSlave.setInverted(InvertType.FollowMaster);

    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
        0, TIMEOUT);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
        0, TIMEOUT);

    // should follow motor setting; unclear if true means it does or not
    leftMaster.setSensorPhase(true);
    rightMaster.setSensorPhase(true);

    // reset encoders to zero
    leftMaster.setSelectedSensorPosition(0, 0, 20);
    rightMaster.setSelectedSensorPosition(0, 0, 20);
    // armMotor.setSelectedSensorPosition(0, 0, 10);

    // config for R & L masters, assume followers will follow
    // some redundant, same as defaults, here to adjust PRN
    // config min/max speeds, ramp, active kI zone for PID control

    leftMaster.config_kP(position_slot, kP_pos, TIMEOUT);
    leftMaster.config_kI(position_slot, kI_pos, TIMEOUT);
    leftMaster.config_kD(position_slot, kD_pos, TIMEOUT);
    leftMaster.config_IntegralZone(position_slot, kIzone, TIMEOUT);

    rightMaster.config_kP(position_slot, kP_pos, TIMEOUT);
    rightMaster.config_kI(position_slot, kI_pos, TIMEOUT);
    rightMaster.config_kD(position_slot, kD_pos, TIMEOUT);
    rightMaster.config_IntegralZone(position_slot, kIzone, TIMEOUT);

    // Config the peak and nominal (min) outputs, full 12V = 1.0
    // peak #'s small for practice purposes
    leftMaster.configNominalOutputForward(0, TIMEOUT);
    leftMaster.configNominalOutputReverse(0, TIMEOUT);
    leftMaster.configPeakOutputForward(0.5, TIMEOUT);
    leftMaster.configPeakOutputReverse(-0.5, TIMEOUT);

    rightMaster.configNominalOutputForward(0, TIMEOUT);
    rightMaster.configNominalOutputReverse(0, TIMEOUT);
    rightMaster.configPeakOutputForward(0.5, TIMEOUT);
    rightMaster.configPeakOutputReverse(-0.5, TIMEOUT);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will
     *  be neutral within this range. 
     */
    // leftMaster.configAllowableClosedloopError(slot, dbl N, int ms.);
    leftMaster.configAllowableClosedloopError
              (0, 10, 20);
    rightMaster.configAllowableClosedloopError
              (0, 10, 20);

    // dampen abrupt starts in manual control
    rightMaster.configOpenloopRamp(0.4, 20);
    leftMaster.configOpenloopRamp(0.4, 20);

    // set in OI
    // drive.setDeadband(0.04);
  } // end controllerInit

  // both sides aim for same distance target via their own external encoder.
  // param received here in inches, so multiply by inch2tick factor for .set
  // called in init() of GoToPosition cmd, so don't zeroEnco here
  public void goStraightPosition(double target) {
    // tick per wheelRot / inch per wheelRot
    double tickTarget = target * kDriveInch2Tick;
    leftMaster.set(ControlMode.Position, tickTarget);
    rightMaster.set(ControlMode.Position, tickTarget);
  }

  public void rotate180(double target) {
    // tick per wheelRot / inch per wheelRot; L fwd, R bak,
    double tickTarget = target * kDriveInch2Tick;
    leftMaster.set(ControlMode.Position, tickTarget);
    rightMaster.set(ControlMode.Position, -tickTarget);
  }

  // method called from zeroDrivEncod cmd & GTP's end()
  public void zeroEncoder() { // [pos,indx,timeout]
    leftMaster.setSelectedSensorPosition(0, 0, TIMEOUT);
    rightMaster.setSelectedSensorPosition(0, 0, TIMEOUT);
  }

  // class' method for cmds to call on an instance of it
  // should have different name than super's method 
  public void arcaDriv(double throttle, double turn) {
    // method from diff drive class works on instance of it;
    // need to invert turn polarity as aD method inverts again
    // -- this makes Rt push of Rt paddle turn CW
    diffDrive.arcadeDrive(throttle, -turn);
  }

} // end DrivMotoSubs
