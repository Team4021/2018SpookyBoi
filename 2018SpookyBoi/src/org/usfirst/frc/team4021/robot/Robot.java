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

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	// final String defaultAuto = "Left";
	// final String midAuto = "Mid";
	// final String rightAuto = "Right";
	// SendableChooser<String> chooser = new SendableChooser<>();
	double Taco;
	double Pizza;
	int Sandwich;
	AnalogGyro Turny;
	Joystick Xbox = new Joystick(1);
	VictorSP Claws = new VictorSP(0);
	WPI_TalonSRX ScissorLift = new WPI_TalonSRX(5);
	WPI_TalonSRX FrontLeft = new WPI_TalonSRX(2);
	WPI_TalonSRX RearLeft = new WPI_TalonSRX(1);
	WPI_TalonSRX FrontRight = new WPI_TalonSRX(3);
	WPI_TalonSRX RearRight = new WPI_TalonSRX(4);
	SpeedControllerGroup Left = new SpeedControllerGroup(FrontLeft, RearLeft);
	SpeedControllerGroup Right = new SpeedControllerGroup(FrontRight, RearRight);
	SpeedControllerGroup Scissors = new SpeedControllerGroup(ScissorLift);
	double angleTurn;
	double angleRate;
	double angle;
	double enkersDistance;
	double onkleDistance;
	double scissorsDistance;
	int RobotSide;
	int ScaleSide;
	Encoder leftEncoder;
	Encoder rightEncoder;
	Encoder scissorsEncoder;
	int AutoStep;
	DifferentialDrive PizzaTacoDrive;
	UsbCamera Cam0;
	UsbCamera Cam1;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		Turny = new AnalogGyro(0);
		leftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
		rightEncoder = new Encoder(2, 3, true, EncodingType.k4X);
		scissorsEncoder = new Encoder(4, 5, false, EncodingType.k4X);
		leftEncoder.setDistancePerPulse(0.0622);
		rightEncoder.setDistancePerPulse(0.0622);
		scissorsEncoder.setDistancePerPulse(0.0622);
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
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if (gameData.charAt(1) == 'L') {
			ScaleSide = 1;
			// Left = 1, Right = 2
		} else {
			ScaleSide = 2;
			// Left = 1, Right = 2

		}
	}

	public void teleipInit() {
		PizzaTacoDrive = new DifferentialDrive(Left, Right);
		PizzaTacoDrive.setSafetyEnabled(false);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		angleRate = Turny.getRate();
		SmartDashboard.putNumber("Auto Step", AutoStep);
		enkersDistance = leftEncoder.getDistance();
		SmartDashboard.putNumber("enkersDistance", enkersDistance);
		onkleDistance = rightEncoder.getDistance();
		SmartDashboard.putNumber("onkleDistance", onkleDistance);
		scissorsDistance = scissorsEncoder.getDistance();
		SmartDashboard.putNumber("ScissorsLift", scissorsDistance);
		angleTurn = Turny.getAngle() % 360;
		SmartDashboard.putNumber("Gyro angle", Math.round(angleTurn * 100.0) / 100.0);
		switch (RobotSide) {
		case 1:// on left, scale left side
			switch (ScaleSide) {
			case 1:
				switch (AutoStep) {
				case 1:
					Claws.set(-5);
					Timer.delay(.5);
					AutoStep++;
				case 2:
					if (enkersDistance <= 265) {
						Left.set(0.5);
						Right.set(-0.5);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					} else {
						AutoStep++;
					}
					break;
				case 3:
					if (angleTurn >= -75) {
						Left.set(0.4);
						Right.set(0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					} else {
						AutoStep++;
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}
					break;
				case 4:
					if (scissorsDistance <= 75) {
						Scissors.set(0.7);
					} else {
						AutoStep++;
						Scissors.set(0);
					}
					break;
				}

				break;

			case 2: // on left, scale right side
				switch (AutoStep) {
				case 1:
					Claws.set(-5);
					Timer.delay(.5);
					AutoStep++;
				case 2:
					if (enkersDistance <= 200) {
						Left.set(0.5);
						Right.set(-0.5);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					} else {
						AutoStep++;
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}
					break;
				case 3:
					if (angleTurn >= -83) {
						Left.set(0.4);
						Right.set(0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						Left.set(0);
						Right.set(0);
						leftEncoder.reset();
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}
					break;
				case 4:
					if (enkersDistance <= 209) {
						Left.set(0.5);
						Right.set(-0.5);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}
					break;
				case 5:
					if (angleTurn <= -5) {
						Left.set(-0.4);
						Right.set(-0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
						// forward, turn right 90 degrees, forward, turn left 90
						// degrees, place
					} else {
						AutoStep++;
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}
					break;
				case 6:
					if (scissorsDistance <= 75) {
						Scissors.set(0.7);
					} else {
						AutoStep++;
						Scissors.set(0);

					}
				}
				break;
			}
			break;
		case 2: // middle scale middle
			switch (ScaleSide) {
			case 1:
				switch (AutoStep) {
				case 1:
					Claws.set(-5);
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
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						leftEncoder.reset();
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}
					break;
				case 4:
					if (enkersDistance <= 125) {
						Left.set(0.4);
						Right.set(-0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						leftEncoder.reset();
						AutoStep++;
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}
					break;
				case 5:
					if (angleTurn >= 15) {
						Left.set(0.4);
						Right.set(0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						leftEncoder.reset();
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					}
					break;
				case 6:
					if (enkersDistance <= 155) {
						Left.set(0.4);
						Right.set(-0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						leftEncoder.reset();
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

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
					if (scissorsDistance <= 75) {
						Scissors.set(0.7);
					} else {
						AutoStep++;
						Scissors.set(0);
					}
					break;
				}
				// pause, turn left 60 degrees, forward, turn left 60 degrees
			case 2: // middle, scale on right side
				switch (AutoStep) {
				case 1:
					Claws.set(-5);
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
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					}

					break;

				case 3:
					if (angleTurn >= -45) {
						Left.set(0.4);
						Right.set(0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");
					} else {
						AutoStep++;
						leftEncoder.reset();
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					}
					break;
				case 4:
					if (enkersDistance <= 125) {
						Left.set(0.4);
						Right.set(-0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						leftEncoder.reset();
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					}
					break;
				case 5:
					if (angleTurn >= -15) {
						Left.set(-0.4);
						Right.set(-0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						leftEncoder.reset();
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					}
					break;
				case 6:
					if (enkersDistance <= 155) {
						Left.set(0.4);
						Right.set(-0.4);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

					} else {
						AutoStep++;
						leftEncoder.reset();
						Left.set(0);
						Right.set(0);
						System.out.println("Robotside [" + RobotSide + "], Scaleside [" + ScaleSide + "], Autostep ["
								+ AutoStep + "], left [" + Left.get() + "], Right [" + Right.get()
								+ "], enkersDistance [" + enkersDistance + "], Angle [" + angleTurn + "]");

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
					if (scissorsDistance <= 75) {
						Scissors.set(0.7);
					} else {
						AutoStep++;
						Scissors.set(0);
					}
					break;
				}
				break;
			case 3:
				switch (ScaleSide) {
				case 1: // right side, scale left side
					switch (AutoStep) {
					case 1:
						Claws.set(-5);
						Timer.delay(.5);
						AutoStep++;
					case 2:
						if (enkersDistance <= 200) {
							System.out.println("This is Right");
							Left.set(0.4);
							Right.set(-0.4);
						} else {
							AutoStep++;
							leftEncoder.reset();
							Left.set(0);
							Right.set(0);
						}
						break;
					case 3:
						if (angleTurn >= -90) {
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
						if (enkersDistance <= 209) {
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
					case 6:
						if (scissorsDistance <= 75) {
							Scissors.set(0.7);
						} else {
							AutoStep++;
							Scissors.set(0);
						}
						break;
					}
					break;
				case 2: // right side, scale right side
					switch (ScaleSide) {
					case 1:
						switch (AutoStep) {
						case 1:
							Claws.set(-5);
							Timer.delay(.5);
							AutoStep++;
						case 2:
							if (enkersDistance <= 265) {
								Left.set(0.4);
								Right.set(-0.4);
							} else {
								AutoStep++;
								Left.set(0);
								Right.set(0);

							}
							break;
						case 3:
							if (angleTurn <= 75) {
								Left.set(-0.4);
								Right.set(-0.4);
							} else {
								AutoStep++;
								Left.set(0);
								Right.set(0);
							}
							break;

						case 4:
							if (scissorsDistance <= 75) {
								Scissors.set(0.7);
							} else {
								AutoStep++;
								Scissors.set(0);
							}
							break;
						}

						break;
					}
					break;
				}
			}
		}
	}
	// if (angleTurn > 360 || angleTurn < -360) {
	// Turny.reset();
	// }

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Pizza = Xbox.getRawAxis(1);
		Taco = Xbox.getRawAxis(0);
		PizzaTacoDrive.arcadeDrive(-Pizza, Taco);
		System.out.println("Teleop");
		angleTurn = Turny.getAngle();
		angleRate = Turny.getRate();
		SmartDashboard.putNumber("Gyro angle", angleTurn);
		SmartDashboard.putNumber("Rate of turning", angleRate);
		SmartDashboard.putNumber("ScissorsLift", scissorsDistance);
		if (Xbox.getRawButton(1)) {
			ScissorLift.set(1);
		} else if (Xbox.getRawButton(2)) {
			ScissorLift.set(-1);
		} else {
			ScissorLift.set(0);
		}
		if (Xbox.getRawButton(3)) {
			Claws.set(-.5);
		} else if (Xbox.getRawButton(4)) {
			Claws.set(.5);
		} else {
			Claws.set(0);
		}

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
