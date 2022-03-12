// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final WPI_TalonSRX frontleft = new WPI_TalonSRX(RobotMap.frontleft);
  private final WPI_TalonSRX frontright = new WPI_TalonSRX(RobotMap.frontright);
  private final WPI_VictorSPX backleft = new WPI_VictorSPX(RobotMap.backleft);
  private final WPI_VictorSPX backright = new WPI_VictorSPX(RobotMap.backright);
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_sticktwo = new Joystick(1);
  private DifferentialDrive m_robotDrive;
  private final WPI_VictorSPX shooter = new WPI_VictorSPX(RobotMap.shooter);
  private final WPI_VictorSPX balllift = new WPI_VictorSPX(RobotMap.balllift);
  private final WPI_VictorSPX intake = new WPI_VictorSPX(RobotMap.intake);
  private final WPI_VictorSPX climberextend = new WPI_VictorSPX(RobotMap.climberextend);
  private final WPI_VictorSPX climberretract = new WPI_VictorSPX(RobotMap.climberretract);
  private final WPI_VictorSPX intakearm = new WPI_VictorSPX(RobotMap.intakearm);

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    backleft.follow(frontleft);
    backright.follow(frontright);
    m_robotDrive = new DifferentialDrive(frontleft, frontright);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    /* switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        
        break;
    } */
runautonomous();

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    //Drive Train
    if (m_stick.getRawButton(RobotMap.halfspeedbutton)) {
      m_robotDrive.arcadeDrive(m_stick.getRawAxis(2), -m_stick.getRawAxis(1));
    } else {
      m_robotDrive.arcadeDrive(m_stick.getRawAxis(2) / 2, -m_stick.getRawAxis(1) / 2);
    }

    //Intake Rollers
    if (m_sticktwo.getRawButton(RobotMap.reverseintakebutton)) {
      intake.set(-RobotMap.intakespeed);
    } else {
      if (m_sticktwo.getRawButton(RobotMap.intakebutton))
        intake.set(RobotMap.intakespeed);

      else {
        intake.set(0.0);
      }
    }

    //Intake Arm
    if (m_sticktwo.getRawButton(RobotMap.intakearmbutton)) {
      intakearm.set(RobotMap.armspeed);
    } else {
      if (m_sticktwo.getRawButton(RobotMap.reverseintakarmbutton))
        intakearm.set(-RobotMap.armspeed);

      else {
        intakearm.set(0.0);
      }
    }

    //Ball Lift
    if (m_sticktwo.getRawButton(RobotMap.ballliftbutton)) {
      balllift.set(RobotMap.ballliftspeed);
    } else {
      if (m_sticktwo.getRawButton(RobotMap.reverseballliftbutton)) {

        balllift.set(-RobotMap.ballliftspeed);
      }

      else {
        balllift.set(0.0);
      }

    }

    //Shooter
    if (m_sticktwo.getRawButton(RobotMap.shooterbutton)) {
      shooter.set(RobotMap.shooterspeed);
    } else {
      shooter.set(0.0);

    }

    //Climbing
    if (m_sticktwo.getRawButton(RobotMap.climberextendbutton)) {
      climberextend.set(RobotMap.climberextendspeed);
    } else {
      if (m_sticktwo.getRawButton(RobotMap.downclimberextendbutton)) {
        climberextend.set(-RobotMap.climberextendspeed);
        climberretract.set(RobotMap.climberretractspeed);
      }
      else {
        climberextend.set(0.0);
        climberretract.set(0.0);

      }
    }

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }

public void runautonomous(){
  shooter.set(RobotMap.shooterspeed);
  WaitCommand shooterwait = new WaitCommand(1);
  shooterwait.initialize();
  shooterwait.execute();
  balllift.set(RobotMap.ballliftspeed);
  shooterwait.initialize();
  shooterwait.execute();
  shooter.set(0.0);
  balllift.set(0.0);
  frontleft.set(-0.5);
  frontright.set(-0.5);
  WaitCommand drivebackwards = new WaitCommand(3);
  drivebackwards.initialize();
  drivebackwards.execute();
  frontleft.set(0.0);
  frontright.set(0.0);

}

}