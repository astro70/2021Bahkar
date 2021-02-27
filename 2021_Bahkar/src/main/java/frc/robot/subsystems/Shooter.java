package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.CSPMath;
import frc.robot.utils.components.CSPFalcon;

public class Shooter extends SubsystemBase {

    private final CSPFalcon upperShooterMotor = new CSPFalcon(10);
    private final CSPFalcon lowerShooterMotor = new CSPFalcon(11);

    private Notifier shuffle;

    private Sensors sensors;
    
    //https://www.omnicalculator.com/physics/projectile-motion

    public Shooter(Sensors sensors) {
        motorInits();

        SmartDashboard.putNumber("Set Shooter Velocity", 0.0);
        SmartDashboard.putNumber("Set Shooter Power", 0.0);
        SmartDashboard.putNumber("Set Initial Velocty", 0.0);

        SmartDashboard.putNumber("Shooter kP", 0.325);
        SmartDashboard.putNumber("Shooter kI", 0.0);
        SmartDashboard.putNumber("Shooter kD", 0.55);
        SmartDashboard.putNumber("Shooter kF", 0.0);

        shuffle = new Notifier(() -> updateShuffleboard());
        shuffle.startPeriodic(0.1);

        this.sensors = sensors;
    }

    @Override
    public void periodic() {
    }

    public void motorInits() {
        lowerShooterMotor.setPIDF(Constants.shooter.kP, Constants.shooter.kI, Constants.shooter.kD, Constants.shooter.kF);

        lowerShooterMotor.setNeutralMode(NeutralMode.Coast);

        lowerShooterMotor.configClosedloopRamp(Constants.shooter.RAMP_RATE);
        lowerShooterMotor.configOpenloopRamp(Constants.shooter.RAMP_RATE);

        lowerShooterMotor.setInverted(true);
        upperShooterMotor.setInverted(InvertType.FollowMaster);

        lowerShooterMotor.configClosedloopRamp(Constants.shooter.RAMP_RATE);

        upperShooterMotor.follow(lowerShooterMotor);
    }

    private void updateShuffleboard() {
        SmartDashboard.putNumber("Shooter Speed", getLowerVelocity());
        SmartDashboard.putNumber("Initial Velocity", CSPMath.Shooter.rpmToVel(getLowerVelocity()));
        SmartDashboard.putNumber("Shooter Voltage", lowerShooterMotor.get() * RobotController.getInputVoltage());
    }

    public void setPIDF(double kP, double kI, double kD, double kF) {
        lowerShooterMotor.setPIDF(kP, kI, kD, kF);
    }

    public void closeNotifier() {
        shuffle.close();
    }

    public void openNotifier() {
        shuffle.startPeriodic(0.1);
      }

    /**
     * Sets shooter motors to a given percentage [-1.0, 1.0].
     */
    public void setPercentage(double percent) {
        lowerShooterMotor.set(percent);
    }

    /**
     * Sets shooter motors to a given velocity in rpm.
     */
    public void setVelocity(double velocity) {
        lowerShooterMotor.setVelocity(velocity);
    }

    public void setZoneVelocity() {
    }

    /**
     * Gets left shooter motor velocity in rpm.
     */
    public double getLowerVelocity() {
        return lowerShooterMotor.getVelocity();
    }

    /**
     * Gets right shooter motor velocity in rpm.
     */
    public double getUpperVelocity() {
        return upperShooterMotor.getVelocity();
    }

    /**
     * Returns left shooter motor temperature in Celcius.
     */
    public double getLowerTemp() {
        return lowerShooterMotor.getTemperature();
    }

    /**
     * Returns right shooter motor temperature in Celcius.
     */
    public double getUpperTemp() {
        return upperShooterMotor.getTemperature();
    }

    public boolean isReady() {
        return false; //Math.abs(getLowerVelocity() - Constants.Shooter.SHOOTING_VEL) < Constants.Shooter.SHOOTING_TOLERANCE;
    }
}