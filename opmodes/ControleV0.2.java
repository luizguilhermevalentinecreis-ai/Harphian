package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Atirador;
import org.firstinspires.ftc.teamcode.robot.GamepadInput;
import org.firstinspires.ftc.teamcode.robot.MecanumChassis;

@TeleOp(name = "Controle", group = "Linear OpMode")
public class Controle extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumChassis chassis = new MecanumChassis(hardwareMap);
        Atirador atirador      = new Atirador(hardwareMap);
        GamepadInput input     = new GamepadInput(gamepad1);

        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            input.ler();
            chassis.mover(input.frente, input.lateral, input.giro);
            atirador.controlar(gamepad1.b);
            mostrarTelemetry(input, chassis);
        }
    }

    private void mostrarTelemetry(GamepadInput input, MecanumChassis chassis) {
        telemetry.addData("RT/R2 frente", gamepad1.right_trigger);
        telemetry.addData("LT/L2 tras",   gamepad1.left_trigger);
        telemetry.addData("frente",        input.frente);
        telemetry.addData("lateral RB/LB", input.lateral);
        telemetry.addData("giro",          input.giro);
        telemetry.addData("FE", chassis.potenciaFE / chassis.maiorPotencia);
        telemetry.addData("FD", chassis.potenciaFD / chassis.maiorPotencia);
        telemetry.addData("TE", chassis.potenciaTE / chassis.maiorPotencia);
        telemetry.addData("TD", chassis.potenciaTD / chassis.maiorPotencia);
        telemetry.update();
    }
}
