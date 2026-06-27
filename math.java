package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.robot.RobotConfig.zonaMortaA;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.zonaMortaG;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.passoAceleracao;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.passoDesaceleracao;


public class RobotMath {

    public static double absoluto(double numero){
        return numero < 0 ? -numero : numero;
    }

    public static double sinal(double numero){

        if(numero > 0)
            return 1;

        if(numero < 0)
            return -1;

        return 0;
    }

    public static double limitar(
            double numero,
            double minimo,
            double maximo
    ){

        return java.lang.Math.max(
                minimo,
                java.lang.Math.min(
                        maximo,
                        numero
                )
        );
    }

    public static double mapear(
            double valor,
            double entradaMin,
            double entradaMax,
            double saidaMin,
            double saidaMax
    ){

        if(entradaMax == entradaMin)
            return 0;

        return
                (valor - entradaMin)
                *
                (saidaMax - saidaMin)
                /
                (entradaMax - entradaMin)
                +
                saidaMin;
    }

    public static double deadzone(
            double valor,
            double zona
    ){

        if(zona >= 1)
            return 0;

        if(absoluto(valor) < zona)
            return 0;

        return limitar(
                sinal(valor)
                *
                mapear(
                        absoluto(valor),
                        zona,
                        1,
                        0,
                        1
                ),
                -1,
                1
        );
    }

    public static double aplicarCurvaValor(
            double entrada
    ){

        if(absoluto(entrada) < zonaMortaA)
            entrada = 0;

        double resultado =
                (0.6 *
                entrada *
                entrada *
                entrada)
                +
                (0.4 * entrada);

        return limitar(
                resultado,
                -1,
                1
        );
    }

    public static double aplicarCurvaGatilho(
            double entrada
    ){

        if(entrada < zonaMortaG)
            entrada = 0;

        double resultado =
                (0.5 *
                entrada *
                entrada *
                entrada)
                +
                (0.5 * entrada);

        return limitar(
                resultado,
                0,
                1
        );
    }

    public static double curva(
            double entrada,
            double peso
    ){

        peso = limitar(
                peso,
                0,
                1
        );

        return limitar(
                (peso *
                entrada *
                entrada *
                entrada)
                +
                ((1-peso)*entrada),
                -1,
                1
        );
    }

    public static double exponencial(
            double entrada,
            double nivel
    ){

        return limitar(
                sinal(entrada)
                *
                java.lang.Math.pow(
                        absoluto(entrada),
                        nivel
                ),
                -1,
                1
        );
    }

    public static double lerp(
            double atual,
            double alvo,
            double peso
    ){

        peso = limitar(
                peso,
                0,
                1
        );

        return atual +
                (alvo-atual)
                *
                peso;
    }

    public static double rampa(
            double atual,
            double alvo
    ){

        double diferenca =
                alvo - atual;

        if(alvo == 0){

            if(absoluto(atual)
                    <= passoDesaceleracao)

                return 0;

            return atual -
                    sinal(atual)
                    *
                    passoDesaceleracao;
        }

        if(diferenca > passoAceleracao)
            return atual + passoAceleracao;

        if(diferenca < -passoAceleracao)
            return atual - passoAceleracao;

        return alvo;
    }

    public static double normalizarAngulo(
            double angulo
    ){

        while(angulo > 180)
            angulo -= 360;

        while(angulo < -180)
            angulo += 360;

        return angulo;
    }

    public static double distancia(
            double x1,
            double y1,
            double x2,
            double y2
    ){

        double dx = x2-x1;
        double dy = y2-y1;

        return java.lang.Math.sqrt(
                dx*dx +
                dy*dy
        );
    }

    public static double encoderParaGraus(
            double ticks,
            double ticksPorVolta
    ){

        if(ticksPorVolta == 0)
            return 0;

        return ticks /
                ticksPorVolta *
                360;
    }

    public static double grausParaEncoder(
            double graus,
            double ticksPorVolta
    ){

        if(ticksPorVolta == 0)
            return 0;

        return graus /
                360 *
                ticksPorVolta;
    }

    public static double ticksParaDistancia(
            double ticks,
            double ticksPorVolta,
            double diametro
    ){

        if(ticksPorVolta == 0)
            return 0;

        return
                (ticks / ticksPorVolta)
                *
                (java.lang.Math.PI * diametro);
    }

    public static double aproximar(
            double atual,
            double alvo,
            double passo
    ){

        if(absoluto(alvo-atual) <= passo)
            return alvo;

        return atual +
                sinal(alvo-atual)
                *
                passo;
    }

    public static double media(
            double... valores
    ){

        if(valores.length == 0)
            return 0;

        double soma = 0;

        for(double valor : valores)
            soma += valor;

        return soma /
                valores.length;
    }

    public static double[] rotacionarVetor(
            double x,
            double y,
            double angulo
    ){

        double rad =
                java.lang.Math.toRadians(
                        angulo
                );

        return new double[]{

                x *
                java.lang.Math.cos(rad)
                -
                y *
                java.lang.Math.sin(rad),

                x *
                java.lang.Math.sin(rad)
                +
                y *
                java.lang.Math.cos(rad)

        };
    }


    public static class PID {

        private double kp;
        private double ki;
        private double kd;

        private double erroAnterior;
        private double integral;

        private double limiteIntegral = 100;
        private double limiteSaida = 1;

        public PID(
                double kp,
                double ki,
                double kd
        ){

            this.kp = kp;
            this.ki = ki;
            this.kd = kd;

        }

        public double calcular(
                double alvo,
                double atual
        ){

            double erro =
                    alvo-atual;

            integral += erro;

            integral =
                    limitar(
                            integral,
                            -limiteIntegral,
                            limiteIntegral
                    );

            double derivada =
                    erro -
                    erroAnterior;

            erroAnterior = erro;

            return limitar(
                    kp*erro
                    +
                    ki*integral
                    +
                    kd*derivada,
                    -limiteSaida,
                    limiteSaida
            );
        }

        public void setLimiteIntegral(
                double limite
        ){

            limiteIntegral = limite;

        }

        public void setLimiteSaida(
                double limite
        ){

            limiteSaida = limite;

        }

        public void reset(){

            erroAnterior = 0;
            integral = 0;

        }
    }
}
