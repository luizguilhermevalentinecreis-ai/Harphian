//Imports e packages
//Caminho do diretorio dos recursos q o código vai utilizar(exemplo: C.User.Usuario = C:/Users/Usuario)





package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//Comentario Teleop:
//Indica qual Grupo o codigo pertence.
//Nesse caso, Indica que o código é do grupo Linear OpMode, ou seja, a função principal sempre terá o mesmo formato
@TeleOp(name="Controle", group="Linear OpMode")


//Código principal
public class Controle extends LinearOpMode {

    //Definições das variaveis globais(podem ser acessadas por qualquer função do código)
    //Variaveis tem tipos diferentes.
    //Variaveis do tipo DcMotor indicam um motor;
    //Do tipo double indicam um numero inteiro/decimal;
    //Do tipo int indicam um numero inteiro;
    //Do tipo Boolean indicam uma condição(Verdadeiro(true) ou falso(false))
    //Do tipo string indicam um texto/algo que não se encaixa nas acima

    DcMotor rodaTiro;
    DcMotor motorTrasEsquerda;
    DcMotor motorTrasDireita;
    DcMotor motorFrenteEsquerda;
    DcMotor motorFrenteDireita;

    Servo travaObjeto;

    double frente;
    double lateral;
    double giro;

    double alvoFrente;
    double alvoLateral;
    double alvoGiro;

    double valor;
    double potenciaFE;
    double potenciaFD;
    double potenciaTE;
    double potenciaTD;
    double maiorPotencia;

    double absFE;
    double absFD;
    double absTE;
    double absTD;

    double ZONA_MORTA;
    double ZONA_MORTA_GATILHO;
    double PASSO_ACELERACAO;
    double PASSO_DESACELERACAO;
    double ATRITO_ESTATICO;



    //Função que configura os motores corretamente
    //(configura encoders, direção de rotação, quando freiar e quando resetar os encoders)

    public void configurarMotores() {
        //Definição de propriedades dos motores
        // Inverte o lado esquerdo do chassi
        motorFrenteEsquerda.setDirection(DcMotor.Direction.REVERSE);
        motorTrasEsquerda.setDirection(DcMotor.Direction.REVERSE);
        motorFrenteDireita.setDirection(DcMotor.Direction.FORWARD);
        motorTrasDireita.setDirection(DcMotor.Direction.FORWARD);

        // Faz o robô frear quando a potência for 0
        motorFrenteEsquerda.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrenteDireita.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorTrasEsquerda.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorTrasDireita.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //Faz o robo redefinir os encoders quando parar o código
        motorFrenteEsquerda.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrenteDireita.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorTrasEsquerda.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorTrasDireita.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrenteEsquerda.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrenteDireita.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorTrasEsquerda.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorTrasDireita.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //Funções de calculo



    //Faz o calculo do valor absoluto de x(no caso, x é um numero indefinido q vai ser definido mais tarde)
    //ou seja; se x for negativo, transforma em positivo(esse é o calculo absoluto)
    public double absoluto(double x) {
        if (x < 0.0) {
            return 0.0 - x;
        }
        return x;
    }


    //Verifica qual o valor de x atual e transforma x em um valor minimo/positivo ou negativos
    //de acordo com seu valor original
    public double sinal(double x) {
        if (x > 0.0) return 1.0;
        if (x < 0.0) return -1.0;
        return 0.0;
    }
    //Limita o valor de x dentro de um valor minimo/maximo pra nao ultrapassar os limites
    public double limitar(double x, double minimo, double maximo) {
        if (x < minimo) return minimo;
        if (x > maximo) return maximo;
        return x;
    }


    //Começa a fazer os calculos do controle remoto(gamepad)

    //Suaviza a força aplicada no controle e exige uma força minima atraves da zona morta pra mover o robo
    public double aplicarCurvaValor(double entrada) {
        valor = entrada;

        // Ignora movimentos muito pequenos do analógico
        if (valor < ZONA_MORTA && valor > -ZONA_MORTA) {
            valor = 0.0;
        }

        // Suaviza a força do controle
        valor = (0.6 * valor * valor * valor) + (0.4 * valor);

        return valor;
    }
    //Faz a mesma coisa da função acima, so que nos gatilhos do controle
    public double aplicarCurvaGatilho(double entrada) {
        valor = entrada;

        if (valor < ZONA_MORTA_GATILHO) {
            valor = 0.0;
        }

        return (0.5 * valor * valor * valor) + (0.5 * valor);
    }


    //Faz a aceleração/desaceleração gradual(aos poucos) com um limite de aceleração por ciclo
    //Exemplo: so pode adicionar/diminuir a potencia com um valor de  no maximo0.3 por ciclo
    // (cada ciclo dura x segundos)
    public double rampa(double atual, double alvo) {
        double diferenca = alvo - atual;

        // Aceleração gradual
        if (diferenca > PASSO_ACELERACAO) {
            return atual + PASSO_ACELERACAO;
        }

        if (diferenca < 0.0 - PASSO_ACELERACAO) {
            return atual - PASSO_ACELERACAO;
        }

        // Desaceleração gradual
        if (alvo == 0.0) {
            if (atual > PASSO_DESACELERACAO) return atual - PASSO_DESACELERACAO;
            if (atual < 0.0 - PASSO_DESACELERACAO) return atual + PASSO_DESACELERACAO;
            return 0.0;
        }

        return alvo;
    }


    //Compensa atrito inicial(Que pode fazer o robo deslizar/agarrar nos primeiros movimentos depois de
    // muito tempo parado)
    public double compensarAtrito(double potencia) {
        if (potencia == 0.0) return 0.0;

        if (absoluto(potencia) < 0.08) {
            return potencia;
        }

        // Ajuda o motor a vencer o atrito inicial
        return potencia + (sinal(potencia) * ATRITO_ESTATICO);
    }
    //Le os comandos do gamepad e interpreta
    //Exemplo: transforma a força aplicada no gatilho em potencia
    public void lerGamepad() {
        double gatilhoFrente;
        double gatilhoTras;

        gatilhoFrente = aplicarCurvaGatilho(gamepad1.right_trigger);
        gatilhoTras = aplicarCurvaGatilho(gamepad1.left_trigger);

        alvoFrente = gatilhoFrente - gatilhoTras;

        alvoLateral = 0.0;

        if (gamepad1.right_bumper) {
            alvoLateral = 0.7;
        }

        if (gamepad1.left_bumper) {
            alvoLateral = -0.7;
        }

        alvoGiro = aplicarCurvaValor(gamepad1.right_stick_x);

        frente = rampa(frente, alvoFrente);
        lateral = rampa(lateral, alvoLateral);
        giro = rampa(giro, alvoGiro);
    }

    //Move os motores com a força aplicada no gamepad convertida na função "LerGamepad()"
    //(Aplica potencias com base na formula padrão de motores mecanum)
    public void moverMotores() {
        // Cálculo de potencias mecanum com base na leitura do gamepad
        potenciaFE = frente + giro + lateral;
        potenciaFD = frente - giro - lateral;
        potenciaTE = frente + giro - lateral;
        potenciaTD = frente - giro + lateral;

        potenciaFE = compensarAtrito(potenciaFE);
        potenciaFD = compensarAtrito(potenciaFD);
        potenciaTE = compensarAtrito(potenciaTE);
        potenciaTD = compensarAtrito(potenciaTD);

        absFE = absoluto(potenciaFE);
        absFD = absoluto(potenciaFD);
        absTE = absoluto(potenciaTE);
        absTD = absoluto(potenciaTD);

        maiorPotencia = 1.0; //potencia maxima

        if (absFE > maiorPotencia) maiorPotencia = absFE;
        if (absFD > maiorPotencia) maiorPotencia = absFD;
        if (absTE > maiorPotencia) maiorPotencia = absTE;
        if (absTD > maiorPotencia) maiorPotencia = absTD;

        motorFrenteEsquerda.setPower(potenciaFE / maiorPotencia);
        motorFrenteDireita.setPower(potenciaFD / maiorPotencia);
        motorTrasEsquerda.setPower(potenciaTE / maiorPotencia);
        motorTrasDireita.setPower(potenciaTD / maiorPotencia);
    }
    // Metodo q controla quando atirar(No caso, quando apertar B/Bolinha no controle) e quando parar de atirar
    //(No caso, quanto soltar B/Bolinha ele para de atirar)
    public void controlarTiro() {
        if (gamepad1.b) {
            travaObjeto.setPosition(0.0);
            rodaTiro.setPower(0.8);
        } else {
            travaObjeto.setPosition(0.2);
            rodaTiro.setPower(0.0);
        }
    }
    //Metodo auxiliar pra exibir dados importantes na telemetria do robo
    //(Ajuda a detectar se tudo está funcionando conforme planejado ou se há algum erro no código)
    public void mostrarTelemetry() {
        telemetry.addData("RT/R2 frente", gamepad1.right_trigger);
        telemetry.addData("LT/L2 tras", gamepad1.left_trigger);
        telemetry.addData("frente", frente);
        telemetry.addData("lateral RB/LB", lateral);
        telemetry.addData("giro", giro);
        telemetry.addData("FE", potenciaFE / maiorPotencia);
        telemetry.addData("FD", potenciaFD / maiorPotencia);
        telemetry.addData("TE", potenciaTE / maiorPotencia);
        telemetry.addData("TD", potenciaTD / maiorPotencia);
        telemetry.update();
    }
    //funde todas as funções de controle em uma unificada pra economizar espaço na função principal
    public void controleGamepadSimples() {
        while (opModeIsActive()) {
            lerGamepad();
            moverMotores();
            controlarTiro();
            mostrarTelemetry();
        }
    }


    //Função principal: Precisa seguir esse padrao no Linear OpMode, ou seja, sempre terá:
    //@Override e runOpMode
    @Override
    public void runOpMode() {
        //Mapeia onde estão todos os componentes no robo fisico com base no nome que foi definido
        rodaTiro = hardwareMap.get(DcMotor.class, "shootwheel");

        motorTrasEsquerda = hardwareMap.get(DcMotor.class, "backLeftDrive");
        motorTrasDireita = hardwareMap.get(DcMotor.class, "backRightDrive");
        motorFrenteEsquerda = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        motorFrenteDireita = hardwareMap.get(DcMotor.class, "frontRightDrive");
        travaObjeto = hardwareMap.get(Servo.class, "artifactstopper");
        //Mapeia os valores usados nas formulas de precisao
        //Valor minimo de força nos analogicos pro robo poder mover (abaixo disso anula)
        ZONA_MORTA = 0.05;
        //Zona morta dos gatilhos(sim, o robo usa gatilhos)
        ZONA_MORTA_GATILHO = 0.03;
        //Limite de quanto a potencia pode aumentar por ciclo
        PASSO_ACELERACAO = 0.06;
        //Limite de quanto a potencia pode diminuir por ciclo
        PASSO_DESACELERACAO = 0.09;
        //Força extra pro robo vencer o atrito inicial
        ATRITO_ESTATICO = 0.04;

        frente = 0.0;
        lateral = 0.0;
        giro = 0.0;
        //Configura os atributos dos motores
        configurarMotores();
        //Servo fechado
        travaObjeto.setPosition(0.2);
        //para o atirador
        rodaTiro.setPower(0.0);
        //Atualiza telemetria antes de começar
        telemetry.update();
        //Espera o inicio da execuçao do programa
        waitForStart();
        //Inicia o controle por gamepad
        controleGamepadSimples();
    }
}



