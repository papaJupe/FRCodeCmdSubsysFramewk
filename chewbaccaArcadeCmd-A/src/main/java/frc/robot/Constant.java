// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constant {

  // leftMasterID = 3;
  // leftSlaveID = 4;
  // rightMasterID = 1;
  // rightSlaveID = 2;

  public static final double TIMEOUT = 20;

  public static final int kDriveControlPort = 0;
// vals of flatbot, need update
  public static final int autoDriveInch = 48;
  public static final double kDriveInch2Tick = 32768 / (6 * Math.PI);
  public static final double kDriveTick2Inch = (6 * Math.PI) / 32768;

  // public static class OperCons {
  //   public static final int kDriveControlPort = 0;
  // }


} // end Constant
