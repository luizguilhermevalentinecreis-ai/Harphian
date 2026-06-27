package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.robot.RobotConfig.zonaMortaA;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.zonaMortaG;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.passoAceleracao;
import static org.firstinspires.ftc.teamcode.robot.RobotConfig.passoDesaleração;


public class Math {


    // Valor absoluto
    public static double absoluto(double numero) {

        return numero < 0 ? -numero : numero;
    }



    // Retorna direção
    public static double sinal(double numero) {

        if(numero > 0) return 1.0;
        if(numero < 0) return -1.0;

        return 0.0;
    }




    // Limita valor
    public static double limitar(
            double numero,
            double minimo,
            double maximo
    ){

        if(numero < minimo)
            return minimo;

        if(numero > maximo)
            return maximo;

        return numero;
    }





    // Converter escala
    public static double mapear(
            double valor,
            double entradaMin,
            double entradaMax,
            double saidaMin,
            double saidaMax
    ){

        return (valor - entradaMin)
                *
                (saidaMax - saidaMin)
                /
                (entradaMax - entradaMin)
                +
                saidaMin;
    }





    // Zona morta inteligente
    public static double deadzone(
            double valor,
            double zona
    ){

        if(absoluto(valor) < zona)
            return 0;


        return sinal(valor)
                *
                mapear(
                        absoluto(valor),
                        zona,
                        1,
                        0,
                        1
                );
    }





    // Curva cúbica
    public static double aplicarCurvaValor(
            double entrada
    ){

        if(
            entrada < zonaMortaA &&
            entrada > -zonaMortaA
        )

            entrada = 0;


        return
                (0.6 *
                entrada *
                entrada *
                entrada)
                +
                (0.4 * entrada);
    }





    // Curva para gatilho
    public static double aplicarCurvaGatilho(
            double entrada
    ){

        if(entrada < zonaMortaG)
            entrada = 0;


        return
                (0.5 *
                entrada *
                entrada *
                entrada)
                +
                (0.5 * entrada);
    }





    // Curva personalizada
    public static double curva(
            double entrada,
            double peso
    ){

        return
                (peso *
                entrada *
                entrada *
                entrada)
                +
                ((1-peso)*entrada);
    }





    // Expo
    public static double exponencial(
            double entrada,
            double nivel
    ){

        return
                sinal(entrada)
                *
                java.lang.Math.pow(
                        absoluto(entrada),
                        nivel
                );
    }





    // Interpolação
    public static double lerp(
            double atual,
            double alvo,
            double peso
    ){

        return atual +
                (alvo-atual)
                *
                peso;
    }





    // Rampa de aceleração
    public static double rampa(
            double atual,
            double alvo
    ){

        double diferenca =
                alvo-atual;



        if(diferenca > passoAceleracao)
            return atual+passoAceleracao;



        if(diferenca < -passoAceleracao)
            return atual-passoAceleracao;



        if(alvo == 0){

            if(atual > passoDesaleração)
                return atual-passoDesaleração;


            if(atual < -passoDesaleração)
                return atual+passoDesaleração;


            return 0;
        }



        return alvo;
    }





    // Normaliza ângulo -180 até 180
    public static double normalizarAngulo(
            double angulo
    ){

        while(angulo > 180)
            angulo -= 360;


        while(angulo < -180)
            angulo += 360;


        return angulo;
    }





    // Distância entre pontos
    public static double distancia(
            double x1,
            double y1,
            double x2,
            double y2
    ){

        double dx = x2-x1;
        double dy = y2-y1;


        return java.lang.Math.sqrt(
                dx*dx + dy*dy
        );
    }





    // Encoder para graus
    public static double encoderParaGraus(
            double ticks,
            double ticksPorVolta
    ){

        return
                ticks /
                ticksPorVolta *
                360;
    }





    // Graus para encoder
    public static double grausParaEncoder(
            double graus,
            double ticksPorVolta
    ){

        return
                graus /
                360 *
                ticksPorVolta;
    }





    // Encoder para distância
    public static double ticksParaDistancia(
            double ticks,
            double ticksPorVolta,
            double diametro
    ){

        double voltas =
                ticks / ticksPorVolta;


        return
                voltas *
                (java.lang.Math.PI * diametro);
    }





    // Aproxima suavemente
    public static double aproximar(
            double atual,
            double alvo,
            double passo
    ){

        if(atual < alvo)
            return limitar(
                    atual+passo,
                    atual,
                    alvo
            );


        if(atual > alvo)
            return limitar(
                    atual-passo,
                    alvo,
                    atual
            );


        return atual;
    }





    // Média simples
    public static double media(
            double a,
            double b,
            double c
    ){

        return (a+b+c)/3;
    }





    // Rotação de vetor (field centric)
    public static double[] rotacionarVetor(
            double x,
            double y,
            double angulo
    ){

        double rad =
                java.lang.Math.toRadians(angulo);


        double novoX =
                x *
                java.lang.Math.cos(rad)
                -
                y *
                java.lang.Math.sin(rad);



        double novoY =
                x *
                java.lang.Math.sin(rad)
                +
                y *
                java.lang.Math.cos(rad);



        return new double[]{
                novoX,
                novoY
        };
    }





    // PID
    public static class PID {


        private double kp;
        private double ki;
        private double kd;


        private double erroAnterior = 0;
        private double integral = 0;




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


            double derivada =
                    erro-erroAnterior;


            erroAnterior = erro;



            return
                    kp*erro
                    +
                    ki*integral
                    +
                    kd*derivada;
        }
    }

}
