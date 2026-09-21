package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface de utilizador para o estado de Superfície.
 * <p>
 * Esta classe gere a visualização do navio no oceano, o sistema de radar para deteção
 * de artefactos e a interação do utilizador via teclado para movimento e lançamento de drones.
 * Segue o modelo MVC@PA, atuando como uma vista que observa o {@link DeepSeaManager}.
 * </p>
 *
 * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class SuperficieUI extends BorderPane {

    /** Fachada do modelo de dados. */
    private DeepSeaManager model;

    /** Componente de desenho para a grelha de superfície. */
    private SuperficieCanvas canvas;

    /** Contentor para o canvas que facilita o controlo de redimensionamento. */
    private Pane areaPane;

    /** Imagem de fundo temática para a superfície. */
    private ImageView imagemFundo;

    /** Ângulo de rotação visual do navio baseado na última direção de movimento. */
    private double anguloNavio = 0;

    /**
     * Construtor da SuperficieUI.
     * @param model Instância do Manager que gere a lógica do jogo.
     */
    public SuperficieUI(DeepSeaManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    /**
     * Inicializa os componentes visuais, configura o fundo e o stack central.
     */
    private void createViews() {
        canvas = new SuperficieCanvas();
        areaPane = new Pane(canvas);
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ImageUIMenuInicial.png")));
        imagemFundo = new ImageView(img);
        StackPane stackCentro = new StackPane();

        areaPane.setStyle("-fx-background-color: transparent;");
        areaPane.setMinSize(0, 0);

        imagemFundo.setOpacity(0.3);
        imagemFundo.setPreserveRatio(false);

        stackCentro.getChildren().addAll(imagemFundo, areaPane);
        stackCentro.setMinSize(0, 0);
        stackCentro.setStyle("-fx-background-color: #051930;");

        this.setMinSize(0, 0);
        this.setCenter(stackCentro);
    }

    /**
     * Configura os listeners de redimensionamento, notificações do modelo e inputs de teclado.
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

        model.addPropertyChangeListener(DeepSeaManager.PROP, evt -> update());

        this.setOnKeyPressed(event -> {
            if (!this.isVisible()) return;
            int direcaoX = 0;
            int direcaoY = 0;

            switch (event.getCode()) {
                case UP: case W:
                    direcaoX = -1; anguloNavio = 0;
                    break;
                case DOWN: case S:
                    direcaoX = 1; anguloNavio = 180;
                    break;
                case LEFT: case A:
                    direcaoY = -1; anguloNavio = 270;
                    break;
                case RIGHT: case D:
                    direcaoY = 1; anguloNavio = 90;
                    break;
                case SPACE:
                    event.consume();
                    abrirJanelaSelecaoDrone();
                    return;
                default:
                    return;
            }

            if(model.moveNavio(direcaoX, direcaoY)){
                SoundManager.getInstance().playMovimento("movimentoNavio.wav");
            }
            event.consume();
        });
    }

    /**
     * Apresenta um diálogo de confirmação para o utilizador escolher qual drone lançar.
     * <p>
     * Se o lançamento for bem-sucedido, despoleta o som de imersão e altera o estado do jogo.
     * </p>
     */
    private void abrirJanelaSelecaoDrone(){
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Lançamento de Drone");
        alerta.setHeaderText("Escolha o Drone para Iniciar a descida ao Fosso");
        alerta.setContentText("Selecione um dos 3 drones disponíveis na frota:");

        ButtonType btnDrone1 = new ButtonType("Drone 1");
        ButtonType btnDrone2 = new ButtonType("Drone 2");
        ButtonType btnDrone3 = new ButtonType("Drone 3");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(btnDrone1, btnDrone2, btnDrone3, btnCancelar);

        DialogPane dialogPane = alerta.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #17178a; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #788cf1;");
        if(dialogPane.lookup(".content") != null) {
            dialogPane.lookup(".content").setStyle("-fx-text-fill: white;");
        }

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent()) {
            int idDroneEscolhido = -1;
            if (resultado.get() == btnDrone1) idDroneEscolhido = 1;
            else if (resultado.get() == btnDrone2) idDroneEscolhido = 2;
            else if (resultado.get() == btnDrone3) idDroneEscolhido = 3;

            if (idDroneEscolhido != -1) {
                boolean sucesso = model.iniciarDescida(idDroneEscolhido);
                if (!sucesso) {
                    new Alert(Alert.AlertType.WARNING, "Não foi possível lançar o Drone " + idDroneEscolhido + ". Verifica o combustível ou integridade!", ButtonType.OK).showAndWait();
                } else {
                    SoundManager.getInstance().playUmaVez("descerFosso-Fundo.mp3");
                }
            }
        }
    }

    /**
     * Atualiza a visibilidade do componente e os dados do canvas com base no estado do modelo.
     */
    private void update() {
        this.setVisible(model.getState() == DeepSeaState.SUPERFICIE);

        if (this.isVisible()) {
            String[][] grelha = model.getGrelhaSuperficie();
            int posX = model.getNavioPosX();
            int posY = model.getNavioPosY();

            canvas.update(grelha, posX, posY, anguloNavio);
            Platform.runLater(this::requestFocus);
        }
    }

    /**
     * Classe interna responsável pelo renderização gráfica da superfície.
     * Utiliza um {@link Canvas} para desenhar a grelha, o radar e o navio rotacionado.
     */
    class SuperficieCanvas extends Canvas {

        private String[][] grelhaSimbolos;
        private int linhaNavio;
        private int colunaNavio;
        private double anguloNavio;

        private Image imgNavio;
        private Image imgArtefacto;

        /** Construtor do Canvas de Superfície. */
        public SuperficieCanvas() {
            super(500, 500);
            createViews();
            registerHandlers();
        }

        /** Carrega os assets de imagem necessários para o desenho. */
        private void createViews() {
            try {
                imgNavio = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/FotoNavio.png")));
                imgArtefacto = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/artefacto.png")));
            } catch (Exception e) {
                System.out.println("Erro: Não foi possível carregar as imagens do Canvas.");
                imgNavio = null;
                imgArtefacto = null;
            }
        }

        private void registerHandlers() {}

        /**
         * Atualiza os dados internos de desenho e solicita um novo frame.
         */
        private void update(String[][] grelhaSimbolos, int linhaNavio, int colunaNavio, double anguloNavio) {
            this.grelhaSimbolos = grelhaSimbolos;
            this.linhaNavio = linhaNavio;
            this.colunaNavio = colunaNavio;
            this.anguloNavio = anguloNavio;
            desenhar();
        }

        /**
         * Executa o ciclo de desenho gráfico.
         * <p>
         * Desenha o oceano, a malha da grelha, os indicadores de radar (usando gradientes radiais)
         * e o navio com a rotação apropriada.
         * </p>
         */
        private void desenhar() {
            GraphicsContext gc = this.getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            if (grelhaSimbolos == null) return;

            int tamanhoMatriz = grelhaSimbolos.length;
            double tamanhoGrelhaVisual = Math.min(getWidth(), getHeight()) - 40;
            double tamanhoCelula = tamanhoGrelhaVisual / tamanhoMatriz;

            double offsetX = (getWidth() - tamanhoGrelhaVisual) / 2;
            double offsetY = (getHeight() - tamanhoGrelhaVisual) / 2;

            // Fundo do oceano
            gc.setFill(Color.web("#0a2f5c"));
            gc.fillRect(offsetX, offsetY, tamanhoGrelhaVisual, tamanhoGrelhaVisual);

            // Grelha e Radar
            for (int i = 0; i < tamanhoMatriz; i++) {
                for (int j = 0; j < tamanhoMatriz; j++) {
                    double x = offsetX + (j * tamanhoCelula);
                    double y = offsetY + (i * tamanhoCelula);

                    gc.setStroke(Color.web("#1d538f"));
                    gc.setLineWidth(1);
                    gc.strokeRect(x, y, tamanhoCelula, tamanhoCelula);

                    String simbolo = grelhaSimbolos[i][j];

                    if (simbolo.equals("?") || simbolo.equals("A")) {
                        double centroX = x + (tamanhoCelula / 2);
                        double centroY = y + (tamanhoCelula / 2);
                        double raio = tamanhoCelula * 0.6;

                        // Efeito visual do Radar
                        RadialGradient gradienteRadar = new RadialGradient(
                                0, 0, centroX, centroY, raio, false, CycleMethod.NO_CYCLE,
                                new Stop(0.0, Color.web("#ffff00", 0.8)),
                                new Stop(0.5, Color.web("#ff3333", 0.5)),
                                new Stop(1.0, Color.web("#0a2f5c", 0.0))
                        );

                        gc.setFill(gradienteRadar);
                        gc.fillOval(centroX - raio, centroY - raio, raio * 2, raio * 2);

                        gc.setFill(Color.web("#ffffff", 0.7));
                        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, tamanhoCelula * 0.4));
                        gc.fillText("?", x + (tamanhoCelula * 0.38), y + (tamanhoCelula * 0.65));
                    }
                }
            }

            // Moldura da grelha
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeRect(offsetX, offsetY, tamanhoGrelhaVisual, tamanhoGrelhaVisual);

            // Desenho do Navio
            double navioX = offsetX + (colunaNavio * tamanhoCelula) + (tamanhoCelula * 0.1);
            double navioY = offsetY + (linhaNavio * tamanhoCelula) + (tamanhoCelula * 0.1);
            double tamanhoNavio = tamanhoCelula * 0.8;

            if (imgNavio != null) {
                gc.save();
                double centroNavioX = navioX + (tamanhoNavio / 2);
                double centroNavioY = navioY + (tamanhoNavio / 2);

                gc.translate(centroNavioX, centroNavioY);
                gc.rotate(anguloNavio);
                gc.drawImage(imgNavio, -tamanhoNavio / 2, -tamanhoNavio / 2, tamanhoNavio, tamanhoNavio);
                gc.restore();
            } else {
                gc.setFill(Color.RED);
                gc.fillRect(navioX, navioY, tamanhoNavio, tamanhoNavio);
            }
        }
    }
}