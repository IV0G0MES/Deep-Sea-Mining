package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import java.util.Objects;

/**
 * Interface de utilizador para o mini-jogo de recuperação de artefactos.
 * <p>
 * Esta classe implementa a visualização e interação do sliding puzzle (4x4).
 * Estende {@link BorderPane} e utiliza um {@link Canvas} interno para desenhar as peças
 * numeradas e o estado do jogo em tempo real.
 * </p>
 *
 * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class MiniJogoUI extends BorderPane {

    /** Fachada do modelo para consulta de dados e execução de movimentos do puzzle. */
    private DeepSeaManager manager;

    /** Componente de desenho customizado para o tabuleiro do mini-jogo. */
    private MiniJogoCanvas canvas;

    /** Contentor que envolve o canvas para permitir redimensionamento flexível. */
    private Pane areaPane;

    /** Imagem de fundo temática para manter a consistência visual com a aplicação. */
    private ImageView imagemFundo;

    /**
     * Construtor do MiniJogoUI.
     * @param manager Instância do Manager que fornece o estado do puzzle.
     */
    public MiniJogoUI(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    /**
     * Cria e configura os elementos visuais iniciais do componente.
     */
    private void createViews() {
        canvas = new MiniJogoCanvas();
        areaPane = new Pane(canvas);
        areaPane.setStyle("-fx-background-color: transparent;");
        areaPane.setMinSize(0, 0);

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ImageUIMenuInicial.png")));
        imagemFundo = new ImageView(img);
        imagemFundo.setOpacity(0.3);
        imagemFundo.setPreserveRatio(false);

        StackPane stackCentro = new StackPane();
        stackCentro.getChildren().addAll(imagemFundo, areaPane);

        stackCentro.setStyle("-fx-background-color: #051930;");
        stackCentro.setMinSize(0, 0);

        this.setMinSize(0, 0);
        this.setCenter(stackCentro);
    }

    /**
     * Configura os ouvintes de eventos, incluindo o redimensionamento da janela,
     * as notificações de alteração do modelo e a deteção de cliques nas peças do puzzle.
     */
    private void registerHandlers() {
        areaPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            update();
        });
        areaPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            update();
        });

        if (imagemFundo != null) {
            imagemFundo.fitWidthProperty().bind(this.widthProperty());
            imagemFundo.fitHeightProperty().bind(this.heightProperty());
        }

        manager.addPropertyChangeListener(DeepSeaManager.PROP, evt -> update());

        canvas.setOnMouseClicked(event -> {
            if (!this.isVisible()) return;

            int valorPecaClicada = canvas.obterValorPecaPelaCoordenada(event.getX(), event.getY());

            if (valorPecaClicada > 0) {
                if(manager.jogarPuzzle(valorPecaClicada)){
                    SoundManager.getInstance().playMovimento("pecaMiniJogo.wav");
                }
            }
        });
    }

    /**
     * Atualiza o estado visual do componente com base no estado atual da FSM.
     * Sincroniza o tabuleiro e os movimentos restantes com o Manager.
     */
    private void update() {
        this.setVisible(manager.getState() == DeepSeaState.MINI_JOGO);

        if (this.isVisible()) {
            int[][] tabuleiro = manager.getPuzzle();
            int movimentosRestantes = manager.getMovesLeft();
            canvas.update(tabuleiro, movimentosRestantes);
            Platform.runLater(this::requestFocus);
        }
    }

    /**
     * Classe interna especializada no renderização gráfica do puzzle.
     * <p>
     * Gere o desenho das molduras, textos de informação e as células individuais
     * que compõem o desafio de recuperação de artefactos.
     * </p>
     */
    class MiniJogoCanvas extends Canvas {
        private int[][] tabuleiro;
        private int movimentosRestantes;

        private double gridStartX, gridStartY;
        private double cellW, cellH;
        private double paddingTotalCell;

        /** Construtor do Canvas do Mini-jogo. */
        public MiniJogoCanvas() {
            super(500, 500);
            createViews();
            registerHandlers();
        }

        private void createViews() {}
        private void registerHandlers() {}

        /**
         * Atualiza os dados internos e redesenha o tabuleiro completo.
         * @param tabuleiro Matriz 4x4 representativa do estado do puzzle.
         * @param movimentosRestantes Contador de tentativas disponíveis.
         */
        private void update(int[][] tabuleiro, int movimentosRestantes) {
            this.tabuleiro = tabuleiro;
            this.movimentosRestantes = movimentosRestantes;
            desenhaPuzzle();
        }

        /**
         * Executa o ciclo de desenho gráfico utilizando {@link GraphicsContext}.
         * <p>
         * Renderiza o fundo do puzzle, o título, o contador de movimentos e as
         * peças numeradas, calculando dinamicamente o posicionamento centralizado.
         * </p>
         */
        private void desenhaPuzzle() {
            GraphicsContext gc = this.getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            if (tabuleiro == null) return;

            // Definição da área útil do puzzle
            double tamanhoCaixa = Math.min(Math.min(getWidth(), getHeight()) - 40, 500);
            double startX = (getWidth() - tamanhoCaixa) / 2;
            double startY = (getHeight() - tamanhoCaixa) / 2;

            // Desenho do contentor principal
            gc.setFill(Color.web("#051930"));
            gc.fillRoundRect(startX, startY, tamanhoCaixa, tamanhoCaixa, 15, 15);

            // Cabeçalho e Texto de Status
            gc.setFill(Color.LIGHTBLUE);
            gc.setTextAlign(TextAlignment.CENTER);

            double tituloFontSize = Math.min(tamanhoCaixa * 0.08, 40);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, tituloFontSize));
            gc.setTextBaseline(VPos.TOP);
            gc.fillText("Mini Jogo", getWidth() / 2, startY + 20);

            double movesFontSize = Math.min(tamanhoCaixa * 0.045, 24);
            gc.setFont(Font.font("Arial", movesFontSize));
            gc.fillText("Movimentos Restantes: " + movimentosRestantes, getWidth() / 2, startY + 25 + tituloFontSize);

            // Configuração da Grelha
            double padding = 8.0;
            double cabecalhoAltura = 80.0 + tituloFontSize;
            double margemGrelha = 30.0;
            double areaGrelha = tamanhoCaixa - cabecalhoAltura - margemGrelha;

            int rows = 4;
            int cols = 4;

            this.cellW = (areaGrelha - (padding * (cols - 1))) / cols;
            this.cellH = (areaGrelha - (padding * (rows - 1))) / rows;
            this.paddingTotalCell = cellW + padding;

            double gridLarguraTotal = (cellW * cols) + (padding * (cols - 1));
            this.gridStartX = startX + (tamanhoCaixa - gridLarguraTotal) / 2;
            this.gridStartY = startY + cabecalhoAltura;

            // Desenho das Peças
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int valor = tabuleiro[i][j];

                    if (valor != 0) { // O valor 0 representa o espaço vazio
                        double px = gridStartX + (j * paddingTotalCell);
                        double py = gridStartY + (i * paddingTotalCell);

                        gc.setFill(Color.web("#0a2f5c"));
                        gc.fillRoundRect(px, py, cellW, cellH, 10, 10);

                        gc.setFill(Color.WHITE);
                        gc.setFont(Font.font("System", FontWeight.BOLD, cellH * 0.45));
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.setTextBaseline(VPos.CENTER);
                        gc.fillText(String.valueOf(valor), px + (cellW / 2), py + (cellH / 2));
                    }
                }
            }
        }

        /**
         * Traduz uma coordenada de clique (pixels) no valor da peça correspondente no tabuleiro.
         * @param mouseX Coordenada X do clique.
         * @param mouseY Coordenada Y do clique.
         * @return O número da peça clicada (1-15) ou -1 se o clique for fora de uma peça válida.
         */
        private int obterValorPecaPelaCoordenada(double mouseX, double mouseY) {
            if (tabuleiro == null || cellW <= 0 || cellH <= 0) return -1;

            int col = (int) ((mouseX - gridStartX) / paddingTotalCell);
            int row = (int) ((mouseY - gridStartY) / paddingTotalCell);

            if (row >= 0 && row < 4 && col >= 0 && col < 4) {
                double px = gridStartX + (col * paddingTotalCell);
                double py = gridStartY + (row * paddingTotalCell);

                if (mouseX <= px + cellW && mouseY <= py + cellH) {
                    return tabuleiro[row][col];
                }
            }
            return -1;
        }
    }
}