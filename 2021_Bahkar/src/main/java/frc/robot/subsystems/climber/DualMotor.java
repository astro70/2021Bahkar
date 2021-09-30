package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/** Class to control the Falcon/Neo duo in the climber */
public class DualMotor {

    private WPI_TalonFX falcon;
    private CANSparkMax neo;

    /**
     * Constructs a new DualMotor controller.
     * @param falconID CAN ID of the falcon motor in the assembly.
     * @param neoID CAN ID of the neo motor in the assembly.
     */
    public DualMotor(int falconID, int neoID) {
        falcon = new WPI_TalonFX(falconID);
        neo = new CANSparkMax(neoID, MotorType.kBrushless);

        falcon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    }
    
    /**
     * Set the power of each motor.
     * @param percent Power in range [-1.0, 1.0].
     */
    public void set(double percent) {
        falcon.set(percent);
        neo.set(percent);
    }

    /**
     * Control the motor brake modes.
     * @param engaged True if braking, false if coasting.
     */
    public void brake(boolean engaged) {
        falcon.setNeutralMode(engaged ? NeutralMode.Brake : NeutralMode.Coast);
        neo.setIdleMode(engaged ? IdleMode.kBrake : IdleMode.kCoast);
    }

    /**
     * Control the inversion of the motor.
     */
    public void setInverted(boolean inverted) {
        falcon.setInverted(inverted);
        neo.setInverted(inverted);
    }

    /**
     * Control the ramp rate of the motors.
     * @param rate Time to go from 0 to 1.0 power.
     */
    public void setRampRate(double rate) {
        falcon.configOpenloopRamp(rate);
        neo.setOpenLoopRampRate(rate);
    }

    /**
     * Reset the encoder of the falcon motor.
     */
    public void reset() {
        falcon.setSelectedSensorPosition(0.0);
    }

    /**
     * Get the position of the falcon motor.
     */
    public double getPosition() {
        return falcon.getSelectedSensorPosition();
    }

    /**
     * Get the velocity of the falcon motor.
     */
    public double getVelocity() {
        return falcon.getSelectedSensorVelocity();
    }

    /**
     * Return the temperature of the falcon motor in degrees Celsius.
     */
    public double getFalconTemp() {
        return falcon.getTemperature();
    }

    /**
     * Return the temperature of the neo motor in degrees Celsius.
     */
    public double getNeoTemp() {
        return neo.getMotorTemperature();
    }
}