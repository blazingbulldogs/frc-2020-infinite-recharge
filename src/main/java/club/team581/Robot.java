/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package club.team581;

import club.team581.util.ControllerUtil;
import club.team581.util.ShuffleboardLogger;
import club.team581.util.limelight.Limelight;
import club.team581.util.limelight.Limelight.LimelightMotion;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command autonomousCommand;

  public RobotContainer robotContainer;

  public static final ShuffleboardLogger shuffleboard = new ShuffleboardLogger();

  private static final XboxController controller = RobotContainer.controller;
  private static final MecanumDrive drive = RobotContainer.driveSubsystem.mecanumDrive;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    robotContainer = new RobotContainer();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // We check if the trigger is past the threshold and that the left bumper is not
    // being pressed (the bumper is used to put the snarfer in reverse)
    /** The snarfer should be enabled. */
    final boolean currentlySnarfing = controller.getTriggerAxis(Hand.kRight) > RobotContainer.TRIGGER_DEADZONE
        && !controller.getBumper(Hand.kLeft);

    if (controller.getAButton()) {
      final LimelightMotion motion = Limelight.getDriveCommand(
          Constants.Autonomous.Measurements.LIMELIGHT_ANGLE_OF_ELEVATION, Constants.Autonomous.Targets.LoadingBay);

      shuffleboard.logPIDValues(motion.xAxisTranslation, motion.yAxisTranslation, motion.zAxisRotation);
      drive.driveCartesian(motion.xAxisTranslation, motion.yAxisTranslation, motion.zAxisRotation);
    } else {
      // If we are in snarfer mode override the regular controls
      drive.driveCartesian(-ControllerUtil.joystickScale(controller.getX(Hand.kLeft)),
          currentlySnarfing ? -0.2 : ControllerUtil.joystickScale(controller.getY(Hand.kLeft)),
          ControllerUtil.joystickScale(controller.getX(Hand.kRight)));
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
