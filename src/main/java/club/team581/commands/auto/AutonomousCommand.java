/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package club.team581.commands.auto;

import club.team581.Constants;
import club.team581.RobotContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonomousCommand extends SequentialCommandGroup {
  /**
   * Creates a new AutonomousCommand.
   */
  public AutonomousCommand() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new DriveWithEncoders(24),
          new SeekVisionTarget(Constants.Autonomous.START_POSITION),
          new DriveWithLimelight(),
          new DriveWithEncoders(3),
          new InstantCommand(() -> RobotContainer.dumperSubsystem.dumper.set(Value.kForward), RobotContainer.dumperSubsystem),
          new WaitCommand(2),
          new InstantCommand(() -> RobotContainer.dumperSubsystem.dumper.set(Value.kOff), RobotContainer.dumperSubsystem));
  }
}
