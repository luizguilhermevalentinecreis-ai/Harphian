![Uploading image.png…]()


# Harphian FTC — Controle V0.1

## Visão Geral
Código de TeleOp para robô FTC com chassi **mecanum** (4 rodas) e sistema de **arremesso de artifacts**.  
Desenvolvido para o jogo **FTC DECODE 2025-2026**.

---

## Hardware Mapeado

| Nome no código | Tipo | Função |
|---|---|---|
| `frontLeftDrive` | DcMotor | Roda dianteira esquerda |
| `frontRightDrive` | DcMotor | Roda dianteira direita |
| `backLeftDrive` | DcMotor | Roda traseira esquerda |
| `backRightDrive` | DcMotor | Roda traseira direita |
| `shootwheel` | DcMotor | Roda de arremesso |
| `artifactstopper` | Servo | Trava do artifact |

---

## Controles (Layout Xbox)

| Botão | Ação |
|---|---|
| **RT (R2)** | Mover para trás |
| **LT (L2)** | Mover para frente |
| **RB** | Mover para a direita |
| **LB** | Mover para a esquerda |
| **Analógico direito (X)** | Girar (yaw) |
| **B** | Arremessar artifact |

---

## Sistemas

### Chassi Mecanum
Movimentação com aceleração/desaceleração suavizadas por rampa.  
Parâmetros ajustáveis em `runOpMode()`:

| Parâmetro | Padrão | Descrição |
|---|---|---|
| `ZONA_MORTA` | `0.05` | Limiar mínimo dos analógicos |
| `ZONA_MORTA_GATILHO` | `0.03` | Limiar mínimo dos gatilhos |
| `PASSO_ACELERACAO` | `0.06` | Incremento máximo de potência por ciclo |
| `PASSO_DESACELERACAO` | `0.09` | Decremento máximo de potência por ciclo |
| `ATRITO_ESTATICO` | `0.04` | Compensação de atrito inicial |

### Arremesso
Pressionar **B** abre o servo (`artifactstopper`) e liga a roda de arremesso (`shootwheel`) simultaneamente, lançando o artifact carregado.  
Soltar **B** fecha o servo e desliga a roda.

---

## Telemetria

| Campo | Descrição |
|---|---|
| `RT/R2 frente` | Valor bruto do gatilho direito |
| `LT/L2 tras` | Valor bruto do gatilho esquerdo |
| `frente / lateral / giro` | Potências calculadas por eixo |
| `FE / FD / TE / TD` | Potência normalizada de cada roda |
| `tiro (B)` | `ATIRANDO` ou `off` |

---

## Versão

`V0.1` — TeleOp básico com mecanum e arremesso de artifact.
