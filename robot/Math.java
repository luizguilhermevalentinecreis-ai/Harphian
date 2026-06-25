package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.robot.RobotConfig.zonaMortaA;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.zonaMortaG;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.passoAceleracao;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.passoDesaleração;

public class Math {

    public static double absoluto(double numero) {
        if (numero < 0.0) return 0.0 - numero;
        return numero;
    }

    public static double sinal(double numero) {
        if (numero > 0.0) return  1.0;
        if (numero < 0.0) return -1.0;
        return 0.0;
    }

    public static double limitar(double numero, double minimo, double maximo) {
        if (numero < minimo) return minimo;
        if (numero > maximo) return maximo;
        return numero;
    }

    public static double aplicarCurvaValor(double entrada) {
        if (entrada < zonaMortaA && entrada > -zonaMortaA) entrada = 0.0;
        return (0.6 * entrada * entrada * entrada) + (0.4 * entrada);
    }

    public static double aplicarCurvaGatilho(double entrada) {
        if (entrada < zonaMortaG) entrada = 0.0;
        return (0.5 * entrada * entrada * entrada) + (0.5 * entrada);
    }

    public static double rampa(double atual, double alvo) {
        double diferenca = alvo - atual;

        if (diferenca >  passoAceleracao) return atual + passoAceleracao;
        if (diferenca < -passoAceleracao) return atual - passoAceleracao;

        if (alvo == 0.0) {
            if (atual >  passoDesaleração) return atual - passoDesaleração;
            if (atual < -passoDesaleração) return atual + passoDesaleração;
            return 0.0;
        }

        return alvo;
    }
}
