package org.usfirst.frc.team4021.robot;


	
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.ctre.phoenix.motorcontrol.can.*;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	//private static final String kDefaultAuto = "Default";
	//private static final String kCustomAuto = "My Auto";
	//private String m_autoSelected;
	//private SendableChooser<String> m_chooser = new SendableChooser<>();
	double Taco;
	double Pizza;
	double useless;
	Joystick Xbox = new Joystick(1);
	WPI_TalonSRX FrontLeft = new WPI_TalonSRX(2);
	WPI_TalonSRX RearLeft = new WPI_TalonSRX(1);
		SpeedControllerGroup Left = new SpeedControllerGroup(FrontLeft, RearLeft);
	WPI_TalonSRX FrontRight = new WPI_TalonSRX(3);
	WPI_TalonSRX RearRight = new WPI_TalonSRX(4);
		SpeedControllerGroup Right = new SpeedControllerGroup(FrontRight, RearRight);
		
	DifferentialDrive PizzaTacoDrive = new DifferentialDrive(Left, Right);
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	 Encoder sampleEncoder1;
	// WPI_TalonSRX talon1 = new WPI_TalonSRX(2);
	// Joystick Joy = new Joystick(1);
	// boolean Yeet = Joy.getRawButton(1);
	double pls = 5;
	private double distance;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	//@override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		sampleEncoder1 = new Encoder ( 0, 1, false, EncodingType.k4X);
		sampleEncoder1.setMaxPeriod(.1);
		sampleEncoder1.setMinRate(.01);
		sampleEncoder1.setDistancePerPulse(.062);
		sampleEncoder1.setReverseDirection(false);
		sampleEncoder1.setSamplesToAverage(7);
		//sampleEncoder1.reset();

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	//@override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	//@override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				sampleEncoder1.setMaxPeriod(.1);
				System.out.println("MaxPeriod");
				sampleEncoder1.setMinRate(10);
				// System.out.println("MinRate");
				sampleEncoder1.setDistancePerPulse(.062);
				sampleEncoder1.setReverseDirection(false);
				sampleEncoder1.setSamplesToAverage(7);
				double distance = sampleEncoder1.getDistance();
				int count = sampleEncoder1.get();
				double period = sampleEncoder1.getPeriod();
				double rate = sampleEncoder1.getRate();
				boolean direction = sampleEncoder1.getDirection();
				boolean stopped = sampleEncoder1.getStopped();
				SmartDashboard.putNumber("pls", pls);
				SmartDashboard.putNumber("Distance", distance);
				if (distance < 46)
				{
					System.out.println(distance);
					Left.set(0.2);
					Right.set(-0.2);
				}
				else
				{
					Left.set(0);
					Right.set(0);		
					//sampleEncoder1.reset();
					}
				
			case kDefaultAuto:
			default:
				
				break;
				
				
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	//@override
	public void teleopPeriodic() {
		Pizza = Xbox.getRawAxis(1);
		Taco = Xbox.getRawAxis(0);
		PizzaTacoDrive.arcadeDrive(-Pizza, Taco);
		double distance = sampleEncoder1.getDistance();
		SmartDashboard.putNumber("pls", pls);
		SmartDashboard.putNumber("Distance", distance);
		/*if  (Joy.getRawButton(1) == true) 
		{
			talon1.set(0.5);
		}
		else 
		{
			talon1.set(0);
		}
		
		int count = sampleEncoder1.get();
		System.out.println("Count " + count);
		double distance = sampleEncoder1.getDistance();
		System.out.println("Distance " + distance);
		double period = sampleEncoder1.getPeriod();
		System.out.println("Period " + period);
		double rate = sampleEncoder1.getRate();
		System.out.println("Rate " + rate);
		boolean direction = sampleEncoder1.getDirection();
		System.out.println("Direction " + direction);
		boolean stopped = sampleEncoder1.getStopped();
		System.out.println("Stopped " + stopped);
		//sampleEncoder1.reset();*/
		
	}
	/**if  (Joy.getRawButton(1) == true) {
	sampleEncoder1 = new Encoder ( 0, 1, false, EncodingType.k1X);
		System.out.println("step1");
		sampleEncoder1.setMaxPeriod(.1);
		System.out.println("step2");
		sampleEncoder1.setMinRate(10); 
		System.out.println("step3");
		sampleEncoder1.setDistancePerPulse(5);
		System.out.println("step4");
		sampleEncoder1.setReverseDirection(true);
		System.out.println("step5");
		sampleEncoder1.setSamplesToAverage(7);
		System.out.println("step6");
		int count = sampleEncoder1.get();
		System.out.println("step7");
		double distance = sampleEncoder1.getDistance();
		System.out.println("step8");
		double period = sampleEncoder1.getPeriod();
		double rate = sampleEncoder1.getRate();
		System.out.println("step9");
		boolean direction = sampleEncoder1.getDirection();
		System.out.println("step10");
		boolean stopped = sampleEncoder1.getStopped();
		System.out.println("step11");
		System.out.println("Count" + count);
		System.out.println("distance" + distance);
		//System.out.println("period" + period);
		System.out.println("rate" + rate);
		System.out.println("direction" + direction);
		System.out.println("stopped" + stopped);
		}
		else {  
			talon1.set(0);
			
		}
		//@Override
	//public void robotInit() {

	//@Override
	//public void autonomousPeriodic() {
		//switch (m_autoSelected) {
			//case kCustomAuto:
				// Put custom auto code here
				//break;
		//	case kDefaultAuto:
			//default:
				// Put default auto code here
				//break;
//		}
//	}

	/**
	 * This function is called periodically during operator control.
	 */
	//@Override
//	public void teleopPeriodic() {
	//	Pizza = Xbox.getRawAxis(1);
	//	Taco = Xbox.getRawAxis(0);
	//	PizzaTacoDrive.arcadeDrive(-Pizza, Taco);
	//}

	/**
	 * This function is called periodically during test mode.
	 */
	//@Override
	//public void testPeriodic() {
	//}
//}
//=======
		

//	}

	/**
	 * This function is called periodically during test mode.
	 */
	//@override
	public void testPeriodic() {
		}
	}

//>>>>>>> refs/remotes/origin/EncoderCode
