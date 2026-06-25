package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.robot.RobotConfig.atritoEstatico;

public class MecanumChassis {

    private DcMotor motorFrenteEsquerda;
    private DcMotor motorFrenteDireita;
    private DcMotor motorTrasEsquerda;
    private DcMotor motorTrasDireita;

    public double potenciaFE;
    public double potenciaFD;
    public double potenciaTE;
    public double potenciaTD;
    public double maiorPotencia;

    public MecanumChassis(HardwareMap hardwareMap) {
        motorFrenteEsquerda = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        motorFrenteDireita  = hardwareMap.get(DcMotor.class, "frontRightDrive");
        motorTrasEsquerda   = hardwareMap.get(DcMotor.class, "backLeftDrive");
        motorTrasDireita    = hardwareMap.get(DcMotor.class, "backRightDrive");
        configurar();
    }

    private void configurar() {
        motorFrenteEsquerda.setDirection(DcMotor.Direction.REVERSE);
        motorTrasEsquerda.setDirection(DcMotor.Direction.REVERSE);
        motorFrenteDireita.setDirection(DcMotor.Direction.FORWARD);
        motorTrasDireita.setDirection(DcMotor.Direction.FORWARD);

        motorFrenteEsquerda.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrenteDireita.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorTrasEsquerda.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorTrasDireita.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorFrenteEsquerda.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrenteDireita.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorTrasEsquerda.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorTrasDireita.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrenteEsquerda.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrenteDireita.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorTrasEsquerda.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorTrasDireita.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void mover(double frente, double lateral, double giro) {
        potenciaFE = frente + giro + lateral;
        potenciaFD = frente - giro - lateral;
        potenciaTE = frente + giro - lateral;
        potenciaTD = frente - giro + lateral;

        potenciaFE = compensarAtrito(potenciaFE);
        potenciaFD = compensarAtrito(potenciaFD);
        potenciaTE = compensarAtrito(potenciaTE);
        potenciaTD = compensarAtrito(potenciaTD);

        maiorPotencia = 1.0;
        if (Math.absoluto(potenciaFE) > maiorPotencia) maiorPotencia = Math.absoluto(potenciaFE);
        if (Math.absoluto(potenciaFD) > maiorPotencia) maiorPotencia = Math.absoluto(potenciaFD);
        if (Math.absoluto(potenciaTE) > maiorPotencia) maiorPotencia = Math.absoluto(potenciaTE);
        if (Math.absoluto(potenciaTD) > maiorPotencia) maiorPotencia = Math.absoluto(potenciaTD);

        motorFrenteEsquerda.setPower(potenciaFE / maiorPotencia);
        motorFrenteDireita.setPower(potenciaFD / maiorPotencia);
        motorTrasEsquerda.setPower(potenciaTE / maiorPotencia);
        motorTrasDireita.setPower(potenciaTD / maiorPotencia);
    }

    private double compensarAtrito(double potencia) {
        if (potencia == 0.0) return 0.0;
        if (Math.absoluto(potencia) < 0.08) return potencia;
        return potencia + (Math.sinal(potencia) * atritoEstatico);
    }
}
