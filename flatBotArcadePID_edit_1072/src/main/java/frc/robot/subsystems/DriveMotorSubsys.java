/*---------------------------------------------------------------------------
*  DriveMotorSubsys   from
// edited from Asid1072 video 4 -- Cmd/Subsy framewk, PID for position
// all periodic in Robot.j, oper input in OI, const in Map                                                           
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

  public int _position_slot = 0; //used by PID init in GoToPos cmd

//CONSTRUCTOR
  public DriveMotorSubsys() {
    controllerInit();
  }

  void controllerInit() { // initializes motor controller settings
   
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

  // need same for R & L
   
    // _masterTalon.config_kP(_position_slot, Constant.kP_pos, Constant.TIMEOUT);
    // _masterTalon.config_kI(_position_slot, Constant.kI_pos, Constant.TIMEOUT);
    // _masterTalon.config_kD(_position_slot, Constant.kD_pos, Constant.TIMEOUT);
    // _masterTalon.config_IntegralZone(_position_slot, Constant.integral_zone, Constant.TIMEOUT);
    // _masterTalon.configClosedloopRamp(0.5, 10);

  } // end controllerInit

   // needed to instance wpi_talonsrx to work here
  DifferentialDrive _diffDrive = new DifferentialDrive(_masterTalonL, _masterTalonR);

// both sides aim for same distance target via their own integral encoder
// param received here in inches, so multiply by inch2tick factor for .set
  public void setPosition(int target) {
    // tick per wheelRot / inch per wheelRot
    int tickTarget = (int)(10700 / (6 * Math.PI));
    _masterTalonL.set(ControlMode.Position, tickTarget);
    _masterTalonR.set(ControlMode.Position, tickTarget);
  }

  public void zeroEncoder(int pos)
  {
    _masterTalonL.setSelectedSensorPosition(pos, 0, Constant.TIMEOUT);
    _masterTalonR.setSelectedSensorPosition(pos, 0, Constant.TIMEOUT);
  }
    // method from diff drive class works on that instance
  public void arcadeDrive(double throttle, double turn) {
    _diffDrive.arcadeDrive(throttle, turn);
  }
 
  // @Override  // set in Robot.j to keep all together; could be here instead
  // the command that is run when no other command is running
  // public void initDefaultCommand() { 
  //   setDefaultCommand(new DriveWithPercent());

}
