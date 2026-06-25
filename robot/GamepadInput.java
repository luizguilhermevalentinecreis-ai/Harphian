package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadInput {

    private Gamepad gamepad;

    public double frente  = 0.0;
    public double lateral = 0.0;
    public double giro    = 0.0;

    public GamepadInput(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void ler() {
        double gatilhoFrente = Math.aplicarCurvaGatilho(gamepad.right_trigger);
        double gatilhoTras   = Math.aplicarCurvaGatilho(gamepad.left_trigger);

        double alvoFrente = gatilhoFrente - gatilhoTras;

        double alvoLateral = 0.0;
        if (gamepad.right_bumper) alvoLateral =  0.7;
        if (gamepad.left_bumper)  alvoLateral = -0.7;

        double alvoGiro = Math.aplicarCurvaValor(gamepad.right_stick_x);

        frente  = Math.rampa(frente,  alvoFrente);
        lateral = Math.rampa(lateral, alvoLateral);
        giro    = Math.rampa(giro,    alvoGiro);
    }
}
