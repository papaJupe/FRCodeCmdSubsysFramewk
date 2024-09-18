// armProto-A-cmd                             ArmSubsys.j

/* sourceType: subsystem for armProto-A-cmd

* sourceStatus: functional

* useContext: define objects for PWM motor control, abs encoder, PID and other param,
* single subsys method called by robot.j

package frc.robot.subsystems;

import edu.wpi.fiorst.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class ArmSubsys extends SubsystemBase {

     // single brushed motor, PWM control (port) by RoboRIO
     //private final Spark armMotor = new Spark(0);
     public final Spark armMotorSpark = new Spark(0);

     // if reading Quad encoder on arm, hardwire to RIO DIO port 0,1
     // public final Encoder armEncoder = new Encoder(0,1);
 
     // for REV thru bore 11-1271 using abs. mode full 360° rot = 1024 count
     // remote absol. encod on arm axis, wired to RIO port
     public final DutyCycleEncoder absolArmEncod = new DutyCycleEncoder(0);

  // angle control setpoints for possible button triggers
  // not used now -- setpt coded in each PIDcommand;
  // would prefer to have each button set a param before making
  // a new PIDcommand using it, might need named PIDcmd for that
  public static double setpointA = 0;
  public static double setpointB = 60;
  public static double setpointX = 90;
  public static double setpointY = 120; // end limit ~180°

  // angle controller PID param

  public static double kP = 0.32;
  public static double kI = 0.15;
  public static double kD = 0.0;
  // not used yet
  // public static double kMaxOutput = 0.2;
  // public static double kMinOutput = 0.2;

  // Constructor
  public ArmSubsys() {

    // invert / or not, so that positive V (to red wire) results in
    // arm moving "forward'. Normal polarity goes fwd on prototype but
    // absolute encoder reads 'fwd' as negative, so for RIO control I need
    // to invert. I.e. fwd for Spark limit is reverse in code for
    // RIO & gamepad when this code is in control
    armMotorSpark.setInverted(true);

    // set 'distance' (angle°) conversion factor for absol. encod.
    absolArmEncod.setDistancePerRotation(360.0);

    // to read angle in rad.
    // absolArmEncod.setDistancePerRotation(2 * Math.PI);

// how do I setTol for inline created PIDcontroller?
    // setpoint before it's counted as at the reference (setpt)
    // angleControl.setTolerance(2);
    // deg, deg/sec (dist , vel)

  } // end constructor

  public double getAngle() {
    return Math.round(absolArmEncod.getDistance());
  }
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
