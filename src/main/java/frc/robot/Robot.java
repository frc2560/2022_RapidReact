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
    private final SendableChooser<String> m_chooser = new SendableChooser<>();
    private final WPI_TalonSRX frontleft = new WPI_TalonSRX(RobotMap.frontleft);
    private final WPI_TalonSRX frontright = new WPI_TalonSRX(RobotMap.frontright);
    private final WPI_VictorSPX backleft = new WPI_VictorSPX(RobotMap.backleft);
    private final WPI_VictorSPX backright = new WPI_VictorSPX(RobotMap.backright);
    private final Joystick m_stick = new Joystick(0);
    private final Joystick m_sticktwo = new Joystick(1);
    private final WPI_VictorSPX shooter = new WPI_VictorSPX(RobotMap.shooter);
    private final WPI_VictorSPX balllift = new WPI_VictorSPX(RobotMap.balllift);
    private final WPI_VictorSPX intake = new WPI_VictorSPX(RobotMap.intake);
    private final WPI_VictorSPX climberextend = new WPI_VictorSPX(RobotMap.climberextend);
    private final WPI_VictorSPX climberretract = new WPI_VictorSPX(RobotMap.climberretract);
    private final WPI_VictorSPX intakearm = new WPI_VictorSPX(RobotMap.intakearm);
    private String m_autoSelected;
    private DifferentialDrive m_robotDrive;
    private boolean firstWait = false;
    private boolean secondwait = false;
    private boolean thirdwait = false;
    private final WaitCommand shooterwait = new WaitCommand(1);
    private final WaitCommand ballliftwait = new WaitCommand(1);
    private final WaitCommand drivebackwards = new WaitCommand(2);




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

        //Setup Drive
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

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        runautonomous();
        // switch (m_autoSelected) {
        //     case kCustomAuto:
        //         // Put custom auto code here
        //         break;
        //     case kDefaultAuto:
        //     default:
        //         // Put default auto code here
        //         break;
        // }
    }

    /**
     * This function is called once when teleop is enabled.
     */
    @Override
    public void teleopInit() {
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        DriveTrain();
        IntakeRollers();
        IntakeArm();
        BallLift();
        Shooter();
        Climber();

    }

    private void Climber() {
        if (m_stick.getRawButton(RobotMap.climberextendbutton)) {
            climberextend.set(-RobotMap.climberextendspeed);
            // climberretract.set(-RobotMap.climberretractspeed);
        } else {
            if (m_stick.getRawButton(RobotMap.downclimberextendbutton)) {
                climberextend.set(RobotMap.climberextendspeed/2);
                climberretract.set(RobotMap.climberretractspeed);
            } else {
                climberextend.set(0.0);
                climberretract.set(0.0);
            }
        }
    }

    private void Shooter() {
        if (m_sticktwo.getRawButton(RobotMap.shooterbutton)) {
            shooter.set(RobotMap.shooterspeed);
            balllift.set(RobotMap.ballliftspeed);
        } else {
            shooter.set(0.0);
            if(!m_sticktwo.getRawButton(RobotMap.intakebutton) && !m_sticktwo.getRawButton(RobotMap.reverseintakebutton)){
                balllift.set(0.0);
            }
        }
    }

    private void BallLift() {
        // if (m_sticktwo.getRawButton(RobotMap.ballliftbutton)) {
        //     balllift.set(RobotMap.ballliftspeed);
        // } else {
        //     if (m_sticktwo.getRawButton(RobotMap.reverseballliftbutton)) {
        //         balllift.set(-RobotMap.ballliftspeed);
        //     } else {
        //         // balllift.set(0.0);
        //     }

        // }
    }

    private void IntakeArm() {
        if (m_sticktwo.getRawButton(RobotMap.intakearmbutton)) {
            intakearm.set(RobotMap.armspeed);
        } else {
            if (m_sticktwo.getRawButton(RobotMap.reverseintakarmbutton))
            {
                intakearm.set(-RobotMap.armspeed);
            }
            else {
                intakearm.set(0.0);
            }
        }
    }

    private void IntakeRollers() {
        if (m_sticktwo.getRawButton(RobotMap.reverseintakebutton)) {
            intake.set(-RobotMap.intakespeed);
            balllift.set(-RobotMap.ballliftspeed);
        } else {
            if (m_sticktwo.getRawButton(RobotMap.intakebutton))
            {
                intake.set(RobotMap.intakespeed);
                balllift.set(RobotMap.ballliftspeed);
            }
            else {
                intake.set(0.0);
                if(!m_sticktwo.getRawButton(RobotMap.shooterbutton)){
                    balllift.set(0.0);
                }
                

            }
        }
    }

    private void DriveTrain() {
        if (m_stick.getRawButton(RobotMap.halfspeedbutton)) {
            m_robotDrive.arcadeDrive(m_stick.getRawAxis(2) / 2, -m_stick.getRawAxis(1) / 2);
        } else {
            m_robotDrive.arcadeDrive(m_stick.getRawAxis(2), -m_stick.getRawAxis(1));
            
        }
    }

    public void runautonomous(){
        shooter.set(RobotMap.shooterspeed);
        if(!firstWait)
        {
          shooterwait.initialize();
          firstWait = true;
        }
        if(shooterwait.isFinished())
        {
          balllift.set(RobotMap.ballliftspeed);
          if(!secondwait){
            ballliftwait.initialize();
            secondwait = true;
      
          }
          if(ballliftwait.isFinished()){
            shooter.set(0.0);
            balllift.set(0.0);
            m_robotDrive.arcadeDrive(0, -1);
            if(!thirdwait){
              drivebackwards.initialize();
              thirdwait = true;
            }
            if(drivebackwards.isFinished()){
              m_robotDrive.arcadeDrive(0, 0);
            }
      
          }
        }
      }

    /**
     * This function is called once when the robot is disabled.
     */
    @Override
    public void disabledInit() {
    }

    /**
     * This function is called periodically when disabled.
     */
    @Override
    public void disabledPeriodic() {
    }

    /**
     * This function is called once when test mode is enabled.
     */
    @Override
    public void testInit() {
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    /**
     * This function is called once when the robot is first started up.
     */
    @Override
    public void simulationInit() {
    }

    /**
     * This function is called periodically whilst in simulation.
     */
    @Override
    public void simulationPeriodic() {
    }
}
