/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autogroups;

import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Sensors;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.trajectory.RotationTest;
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class RotationTestGroup extends CspSequentialCommandGroup {
  /**
   * Creates a new OneMeterTestGroup.
   */
  public RotationTestGroup(Drivetrain drivetrain, Sensors sensors) {

    RotationTest rt = new RotationTest(drivetrain.getConfig());

    addCommands(
      new FollowTrajectory(drivetrain, sensors, rt.getTrajectory(), rt.getAngles(), rt.getTimes()
      )
    );
  }
}