/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4021.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.*;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	double output;
	double Taco;
	double Pizza;
	int Sandwich;
	AnalogGyro Turny;
	Joystick Xbox = new Joystick(1);
	WPI_TalonSRX Claws = new WPI_TalonSRX(7);
	WPI_TalonSRX Tilt = new WPI_TalonSRX(8);
	WPI_TalonSRX ScissorLift = new WPI_TalonSRX(5);
	WPI_TalonSRX ScissorLift2 = new WPI_TalonSRX(6);
	WPI_TalonSRX FrontLeft = new WPI_TalonSRX(2);
	WPI_TalonSRX RearLeft = new WPI_TalonSRX(1);
	WPI_TalonSRX FrontRight = new WPI_TalonSRX(3);
	WPI_TalonSRX RearRight = new WPI_TalonSRX(4);
	SpeedControllerGroup Left = new SpeedControllerGroup(FrontLeft, RearLeft);
	SpeedControllerGroup Right = new SpeedControllerGroup(FrontRight, RearRight);
	SpeedControllerGroup Scissors = new SpeedControllerGroup(ScissorLift, ScissorLift2);
	double angleTurn;
	double angleRate;
	double angle;
	double enkersDistance;
	double onkleDistance;
	double scissorDistance;
	int RobotSide;
	int ScaleSide;
	Encoder leftEncoder;
	Encoder rightEncoder;
	Encoder scissorEncoder;
	int AutoStep;
	DifferentialDrive PizzaTacoDrive;
	UsbCamera Cam0;
	UsbCamera Cam1;
	String gameData;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		Turny = new AnalogGyro(0);
		leftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
		rightEncoder = new Encoder(2, 3, true, EncodingType.k4X);
		leftEncoder.setDistancePerPulse(0.0622);
		rightEncoder.setDistancePerPulse(0.0622);
		// Cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		// Cam1 = CameraServer.getInstance().startAutomaticCapture(1);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		Turny.reset();
		AutoStep = 1;
		RobotSide = 1;

		
	}

	public void teleopInit() {
		PizzaTacoDrive = new DifferentialDrive(Left, Right);
		PizzaTacoDrive.setSafetyEnabled(false);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if (gameData == null) {
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if (gameData.charAt(1) == 'L') {
				ScaleSide = 1;
				// Left = 1, Right = 2
			} else {
				ScaleSide = 2;
				// Left = 1, Right = 2

			}
		} else {
			angleRate = Turny.getRate();
			SmartDashboard.putNumber("Auto Step", AutoStep);
			enkersDistance = leftEncoder.getDistance();
			scissorDistance = scissorEncoder.getDistance();
			SmartDashboard.putNumber("enkersDistance", enkersDistance);
			onkleDistance = rightEncoder.getDistance();
			SmartDashboard.putNumber("onkleDistance", onkleDistance);
			angleTurn = Turny.getAngle() % 360;
			SmartDashboard.putNumber("Gyro angle", Math.round(angleTurn * 100.0) / 100.0);
			switch (RobotSide) {
			case 1:// on left, scale left side
				switch (ScaleSide) {
				case 1:
					switch (AutoStep) {
					case 1:
						if (enkersDistance <= 230) {
							Left.set(0.5);
							Right.set(-0.5);
						} else {
							AutoStep++;
							Left.set(0);
							Right.set(0);
						}
						break;
					case 2:
						if (angleTurn >= -75) {
							Left.set(0.4);
							Right.set(0.4);
						} else {
							AutoStep++;
						}
						break;
					case 3:
						if (enkersDistance <= 24) {
							Left.set(.5);
							Right.set(-.5);
						} else {
							AutoStep++;
							Left.set(0);
							Right.set(0);
						}
					case 4:
						if (scissorDistance <= 84) {
							Scissors.set(1);
						} else {
							AutoStep++;
						}
						break;
					case 5:
						Claws.set(1);
						Timer.delay(.5); 
					}

					break;

				case 2: // on left, scale right side
					switch (AutoStep) {
					case 1:
						if (enkersDistance <= 200) {
							Left.set(0.5);
							Right.set(-0.5);

						} else {
							AutoStep++;
							Left.set(0);
							Right.set(0);

						}
						break;
					case 2:
						if (angleTurn >= -83) {
							Left.set(0.4);
							Right.set(0.4);

						} else {
							AutoStep++;
							Left.set(0);
							Right.set(0);
							leftEncoder.reset();

						}
						break;
					case 3:
						if (enkersDistance <= 209) {
							Left.set(0.5);
							Right.set(-0.5);

						} else {
							AutoStep++;
							Left.set(0);
							Right.set(0);

						}
						break;
					case 4:
						if (angleTurn <= -5) {
							Left.set(-0.4);
							Right.set(-0.4);
							
							// forward, turn right 90 degrees, forward, turn
							// left 90
							// degrees, place
						} else {
							AutoStep++;
							Left.set(0);
							Right.set(0);
							
						}
						break;
					case 5:
						if (scissorDistance <= 84) {
							Scissors.set(1);
						} else {
							AutoStep++;
						}
						break;
					case 6:
						Claws.set(1);
						Timer.delay(.5);*/
					} 
					break;
				}
				break;
				
			case 2:
				switch (ScaleSide) {
				case 1: // middle scale left
					switch (AutoStep) {
					case 1:
						Claws.set(-.5);
						Timer.delay(.5);
						AutoStep++;
					case 2:

						if (enkersDistance <= 30) {
							Left.set(0.5);
							Right.set(-0.5);
						} else {
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
							AutoStep++;
						}
						break;

					case 3:
						if (angleTurn <= 45) {
							Left.set(-0.4);
							Right.set(-0.4);
							
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
							
						}
						break;
					case 4:
						if (enkersDistance <= 125) {
							Left.set(0.4);
							Right.set(-0.4);
							
						} else {
							leftEncoder.reset();
							AutoStep++;
							Left.set(0);
							Right.set(0);
							
						}
						break;
					case 5:
						if (angleTurn >= 15) {
							Left.set(0.4);
							Right.set(0.4);
							
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
							
						}
						break;
					case 6:
						if (enkersDistance <= 155) {
							Left.set(0.4);
							Right.set(-0.4);
							
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
							

						}
						break;
					case 7:
						if (angleTurn >= -75) {
							Left.set(0.4);
							Right.set(0.4);
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
						}
						break;
					case 8:
						if (enkersDistance <= 10) {
							Left.set(0.4);
							Right.set(-0.4);
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
						}
						break;
					case 9:
						if (forwardLimitSwitch.get()) {
							Scissors.set(0);
						} else {
							Scissors.set(.5);
						}
						break;
					}
		
				case 2: // middle, scale on right side
					switch (AutoStep) {
					case 1:
						Claws.set(-.5);
						Timer.delay(.5);
						AutoStep++;
					case 2:
						if (enkersDistance <= 30) {
							Left.set(0.5);
							Right.set(-0.5);
						} else {
							leftEncoder.reset();
							AutoStep++;
							Left.set(0);
							Right.set(0);
						}
						break;

					case 3:
						if (angleTurn >= -45) {
							Left.set(0.4);
							Right.set(0.4);
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
						}
						break;
					case 4:
						if (enkersDistance <= 125) {
							Left.set(0.4);
							Right.set(-0.4);

						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);

						}
						break;
					case 5:
						if (angleTurn >= -15) {
							Left.set(-0.4);
							Right.set(-0.4);

						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);

						}
						break;
					case 6:
						if (enkersDistance <= 155) {
							Left.set(0.4);
							Right.set(-0.4);

						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
						}
						break;
					case 7:
						if (angleTurn <= 75) {
							Left.set(-0.4);
							Right.set(-0.4);
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);

						}
						break;

					case 8:
						if (enkersDistance <= 10) {
							Left.set(0.4);
							Right.set(-0.4);
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
						}
						break;
					case 9:
						if (forwardLimitSwitch.get()) {
							Scissors.set(0);
						} else {
							Scissors.set(.5);
						}
						break;
					}
					break;
				
				case 3:
					switch (ScaleSide) {
					case 1: // right side, scale left side
						switch (AutoStep) {

						case 1:
							if (enkersDistance <= 200) {
								Left.set(0.5);
								Right.set(-0.5);
							} else {
								AutoStep++;
								leftEncoder.reset();
								Left.set(0);
								Right.set(0);
							}
							break;
						case 2:
							if (angleTurn >= -75) {
								Left.set(-0.4);
								Right.set(-0.4);
							} else {
								AutoStep++;
								leftEncoder.reset();
								Left.set(0);
								Right.set(0);
							}
							break;
						case 3:
							if (enkersDistance <= 200) {
								Left.set(0.4);
								Right.set(-0.4);
							} else {
								AutoStep++;
								leftEncoder.reset();
								Left.set(0);
								Right.set(0);
							}
							break;
						case 4:
							if (angleTurn >= 5) {
								Left.set(-0.4);
								Right.set(-0.4);
							} else {
								AutoStep++;
								leftEncoder.reset();
								Left.set(0);
								Right.set(0);
							}
							break;
						case 5:
							if (scissorDistance <= 84) {
								Scissors.set(1);
							} else {
								AutoStep++;
							}
							break;
						case 6:
							Claws.set(1);
							Timer.delay(.5);
						}
						break;
						}
						break;
					case 2: // right side, scale right side
						switch (ScaleSide) {
						case 1:
							switch (AutoStep) {
							case 1:
								if (enkersDistance <= 265) {
									Left.set(0.5);
									Right.set(-0.5);
								} else {
									AutoStep++;
									Left.set(0);
									Right.set(0);
								}
								break;
							case 2:
								if (angleTurn <= 75) {
									Left.set(-0.4);
									Right.set(-0.4);
								} else {
									AutoStep++;
									Left.set(0);
									Right.set(0);
								}
								break;
							case 3:
								if (scissorDistance <= 84) {
									Scissors.set(1);
								} else {
									AutoStep++;
								}
								break;

							case 4:
								Claws.set(1);
								Timer.delay(.5);
								break;
							}
							break;
						}
						break;
					}
				}
			}

	/**
	 * This function is called periodically during operator control.
	 */
	public void teleopPeriodic() {
		Pizza = Xbox.getRawAxis(1);
		Taco = Xbox.getRawAxis(0);
		angleTurn = Turny.getAngle();
		angleRate = Turny.getRate();
		SmartDashboard.putNumber("Gyro angle", angleTurn);
		SmartDashboard.putNumber("Rate of turning", angleRate);
		PizzaTacoDrive.arcadeDrive(-Pizza, Taco);
		if (Xbox.getRawButton(1)) {
			Scissors.set(1);
		} else if (Xbox.getRawButton(2)) {
			Scissors.set(-1);
		} else {
			Scissors.set(0);
		}
		if (Xbox.getRawButton(5)) {
			Tilt.set(-1);
		} else if (Xbox.getRawButton(6)) {
			Tilt.set(1);
		} else {
			Tilt.set(0);
		}
		if (Xbox.getRawButton(3)) {
			Claws.set(-.5);
		} else if (Xbox.getRawButton(4)) {
			Claws.set(.5);
		} else {
			Claws.set(0);
		}
	}