/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4021.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.*;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	// private static final String kDefaultAuto = "Default";
	// private static final String kCustomAuto = "My Auto";
	// private String m_autoSelected;
	// private SendableChooser<String> m_chooser = new SendableChooser<>();
	double Taco;
	double Pizza;
	AnalogGyro Turny;
	Joystick Xbox = new Joystick(1);
	WPI_TalonSRX FrontLeft = new WPI_TalonSRX(2);
	WPI_TalonSRX RearLeft = new WPI_TalonSRX(1);
	SpeedControllerGroup Left = new SpeedControllerGroup(FrontLeft, RearLeft);
//	WPI_TalonSRX FrontRight = new WPI_TalonSRX(3);
//	WPI_TalonSRX RearRight = new WPI_TalonSRX(4);
	//SpeedControllerGroup Right = new SpeedControllerGroup(FrontRight, RearRight);
	double Kp = 0.03;
	double angleTurn;
	double angleRate;
	double angle;
	DifferentialDrive PizzaTacoDrive = new DifferentialDrive(Left, Left);

	UsbCamera Cam0;
	UsbCamera Cam1;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		// m_chooser.addDefault("Default Auto", kDefaultAuto);
		// m_chooser.addObject("My Auto", kCustomAuto);
		// SmartDashboard.putData("Auto choices", m_chooser);
		Turny = new AnalogGyro(0);

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
		// m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		// System.out.println("Auto selected: " + m_autoSelected);
		// Turny = new AnalogGyro(0);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		angleTurn = Turny.getAngle() % 360;
		SmartDashboard.putNumber("Gyro angle", Math.round(angleTurn* 100.0)/100.0);
		if (angleTurn < 90 && angleTurn > 0) {
		//	FrontLeft.set(.4);
		} else if (angleTurn > 90) {
			//FrontLeft.set(1);
		} else
			FrontLeft.set(0);
		if (angleTurn > 360 || angleTurn < -360) {
			Turny.reset();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Pizza = Xbox.getRawAxis(1);
		Taco = Xbox.getRawAxis(0);
		PizzaTacoDrive.arcadeDrive(-Pizza, Taco);
		//angleTurn = Turny.getAngle();
		//angleRate = Turny.getRate();
		//SmartDashboard.putNumber("Gyro angle", angleTurn);
		//SmartDashboard.putNumber("Rate of turning", angleRate);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
