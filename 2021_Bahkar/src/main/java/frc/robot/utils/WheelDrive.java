package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import frc.robot.Constants;

public class WheelDrive {

  //Unassigned motor objects.
  private TalonFX angleMotor;
  private TalonFX speedMotor;
  private CANCoder angleEncoder;

  private PIDController anglePID = new PIDController(1.0, 0.0, 0.0);

  public WheelDrive(TalonFX angleMotor, TalonFX speedMotor, CANCoder angleEncoder) {
    //Assign the motor objects.
    this.angleMotor = angleMotor;
    this.speedMotor = speedMotor;

    configSensors();
  }

    /**
   * Set properties of the motors.
   */
  private void configSensors() {
    //Select sensor for motors (integrated sensor).
    speedMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    //Call PIDConfig method for each motor and set ramp rates.
    DrivePIDConfig(angleMotor);
    speedMotor.configClosedloopRamp(1.0);

    //Call reset methods.
    resetEncoders();
  }
  
  /**
   * Reset all encoders to position 0.
   */
  public void resetEncoders() {
    //Set encoder positions to 0
    angleMotor.setSelectedSensorPosition(0);
    speedMotor.setSelectedSensorPosition(0);
  }

  /**
   * Method to set P, I, and D terms for drive motor closed loop control.
   * @param motor Motor which is being configured.
   */
  public void DrivePIDConfig(TalonFX motor) {
    //Assign the values.
    motor.config_kP(0, 1.0, 10);
    motor.config_kI(0, 0.0, 10);
    motor.config_kD(0, 0.0, 10);
  }

  /**
  * Method to set SwerveModules based on speed input of M/S and angle input between 0 and 360.
  * @param speed Speed of the wheel in M/S.
  * @param angle Angle for wheel to be turned to.
  */
  public void convertedDrive(double speed, double angle) {
    //Convert M/S to ticks per 100ms and set motor to it.
    speed *= Constants.DRIVE_COUNTS_PER_METER / 10;
    speedMotor.set(ControlMode.Velocity, speed);

    //Find the current angle of the wheel.
    double currentAngle = angleMotor.getSelectedSensorPosition() / Constants.ANGLE_RATIO;

    //Use the current angle to find the position in the current rotation (-180:180) and the number of rotations taken so far.
    double position = (360 % (currentAngle + 180)) - 180;
    double rotIn = (currentAngle - position) / 360;

    //Find the closest equivelant set point.
    double diff = Math.abs(position - angle);
    double SetAngle;
    if (diff < 90) {
      SetAngle = diff + (position + (rotIn * 360));
    } else if (position < -90) {
      SetAngle = diff + (position + ((rotIn - 1) * 360));
    } else if (position > 90) {
      SetAngle = diff + (position + ((rotIn + 1) * 360));
    } else {
      SetAngle = 0;
    }

    //Convert angle to encoder ticks and set the motor to that position.
    SetAngle *= Constants.ANGLE_RATIO;
    angleMotor.set(ControlMode.PercentOutput ,anglePID.calculate(angleEncoder.getPosition(), SetAngle));
  }

  public SwerveModuleState updateModuleState(SwerveModuleState state) {
    double Speed = speedMotor.getSelectedSensorVelocity() / (Constants.DRIVE_COUNTS_PER_METER / 10);
    double Angle = angleMotor.getSelectedSensorPosition() / Constants.ANGLE_RATIO;

    double position = 360 % Angle;

    position = (position > 180) ? (-360 + position) : position;
    position = (position < -180) ? (360 + position) : position;

    state.speedMetersPerSecond = Speed;
    state.angle = Rotation2d.fromDegrees(Angle);

    return state;
  }
}
