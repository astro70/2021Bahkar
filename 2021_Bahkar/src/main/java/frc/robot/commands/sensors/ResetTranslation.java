// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sensors;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.drive.Odometry;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ResetTranslation extends InstantCommand {

  Odometry odometry = Odometry.getInstance();
  Translation2d translation;

  public ResetTranslation(Translation2d translation) {
    this.translation = translation;
  }

  public ResetTranslation() {
    this(new Translation2d());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    odometry.setPose(new Pose2d(translation, odometry.getPose().getRotation()));
  }
}
