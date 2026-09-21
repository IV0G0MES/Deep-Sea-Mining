package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import java.util.Objects;

/**
 * Interface de utilizador para o estado de exploração do Fundo do Mar.
 * <p>
 * Esta classe gere a visualização da camada mais profunda do oceano, onde o drone
 * procura por minérios e artefactos. Implementa a lógica de movimento do drone,
 * recolha de itens, subida para o fosso e efeitos sonoros contextuais.
 * Segue o modelo MVC@PA, observando as alterações no {@link DeepSeaManager}.
 * </p>
 *
 * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class FundoUI extends BorderPane {

    /** Fachada do modelo de dados. */
    private DeepSeaManager manager;

    /** Componente de desenho especializado para a grelha do fundo. */
    private FundoCanvas canvas;

    /** Painel de contenção para o canvas, permitindo redimensionamento dinâmico. */
    private Pane areaPane;

    /** Imagem de fundo decorativa. */
    private ImageView imagemFundo;

    /**
     * Construtor da FundoUI.
     * @param manager Instância do Manager que fornece o estado e dados do fundo.
     */
    public FundoUI(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    /**
     * Inicializa os componentes visuais, configura o StackPane central e a opacidade do fundo.
     */
    private void createViews() {
        canvas = new FundoCanvas();
        areaPane = new Pane(canvas);
        areaPane.setStyle("-fx-background-color: transparent;");
        areaPane.setMinSize(0, 0);

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ImageUIMenuInicial.png")));
        imagemFundo = new ImageView(img);
        imagemFundo.setOpacity(0.3);
        imagemFundo.setPreserveRatio(false);

        StackPane stackCentro = new StackPane();
        stackCentro.getChildren().addAll(imagemFundo, areaPane);
        stackCentro.setMinSize(0, 0);
        stackCentro.setStyle("-fx-background-color: #051930;");

        this.setMinSize(0, 0);
        this.setCenter(stackCentro);
    }

    /**
     * Configura os ouvintes de eventos, incluindo redimensionamento, notificações do modelo
     * e o processamento de teclas (movimento, recolha de objetos e subida).
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

        manager.addPropertyChangeListener(DeepSeaManager.PROP, evt -> {
            boolean estavaVisivel = this.isVisible();
            update();
            if(!estavaVisivel && this.isVisible()){
                SoundManager.getInstance().playUmaVez("descerFosso-Fundo.mp3");
            }
        });

        this.setOnKeyPressed(event -> {
            if (!this.isVisible()) return;

            int dx = 0, dy = 0;
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W)    dx = -1;
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S)  dx = 1;
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A)  dy = -1;
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) dy = 1;

            if (dx != 0 || dy != 0) {
                if(manager.moverDrone(dx, dy)) {
                    String[][] fundo = manager.getGrelhaFundo();
                    if (Monstro(fundo)) {
                        SoundManager.getInstance().playEspecial("movimentoDrone.wav", "Monstro.wav");
                    } else {
                        SoundManager.getInstance().playMovimento("movimentoDrone.wav");
                    }
                }
            }
            if (event.getCode() == KeyCode.SPACE) {
                if(manager.apanhaObjeto()) {
                    SoundManager.getInstance().playEspecial("movimentoDrone.wav", "minerio.wav");
                }
            }
            if (event.getCode() == KeyCode.ENTER) {
                manager.sobeFosso();
                SoundManager.getInstance().playUmaVez("descerFosso-Fundo.mp3");
            }

            event.consume();
        });
    }

    /**
     * Atualiza a visibilidade do ecrã e solicita ao canvas que redesenhe a grelha
     * com a posição atualizada do drone.
     */
    private void update(){
        this.setVisible(manager.getState() == DeepSeaState.FUNDO_MAR);

        if (this.isVisible()) {
            String[][] grelha = manager.getGrelhaFundo();
            int linhaDrone = manager.getDroneAtivo() != -1 ? manager.getDroneAtivoPosX() : -1;
            int colunaDrone = manager.getDroneAtivo() != -1 ? manager.getDroneAtivoPosY() : -1;

            canvas.update(grelha, linhaDrone, colunaDrone);
            Platform.runLater(this::requestFocus);
        }
    }

    /**
     * Verifica se a célula atual ocupada pelo drone contém um monstro.
     * @param fundo Matriz representativa da grelha de fundo.
     * @return {@code true} se existir um monstro na posição do drone; {@code false} caso contrário.
     */
    private boolean Monstro(String[][] fundo){
        int posX = manager.getDroneAtivoPosX();
        int posY = manager.getDroneAtivoPosY();

        if (fundo != null && posX >= 0 && posX < fundo.length && posY >= 0 && posY < fundo[0].length) {
            if ("M".equals(fundo[posX][posY])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Classe interna responsável pelo renderização gráfica do fundo marinho.
     * <p>
     * Implementa a lógica de visibilidade (névoa de guerra), desenhando apenas as
     * células descobertas pelo drone. Gere o desenho de minérios, artefactos,
     * monstros e do próprio drone.
     * </p>
     */
    class FundoCanvas extends Canvas {
        private String[][] grelha;
        private int linhaDrone;
        private int colunaDrone;

        private Image imgDrone, monstroMarinho, artefacto, minerio;

        /** Construtor do Canvas do Fundo. */
        private FundoCanvas() {
            super(500, 500);
            createViews();
            registerHandlers();
        }

        /** Carrega os assets de imagem necessários para os elementos do fundo. */
        private void createViews() {
            try {
                imgDrone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/FotoDrone.png")));
                monstroMarinho = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/monstroMarinho.png")));
                artefacto = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/artefacto.png")));
                minerio = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/minerio.png")));
            } catch (Exception e) {
                System.out.println("Erro: Não foi possível carregar as imagens do FundoCanvas.");
            }
        }

        private void registerHandlers() {}

        /**
         * Atualiza os dados locais de desenho e despoleta o método de renderização.
         */
        private void update(String[][] grelha, int linhaDrone, int colunaDrone) {
            this.grelha = grelha;
            this.linhaDrone = linhaDrone;
            this.colunaDrone = colunaDrone;
            desenhaFundo();
        }

        /**
         * Executa o ciclo de desenho principal.
         * <p>
         * Calcula o posicionamento centralizado da grelha e percorre a matriz
         * aplicando cores de fundo diferentes para distinguir áreas exploradas
         * de áreas ocultas pela escuridão do abismo.
         * </p>
         */
        private void desenhaFundo() {
            GraphicsContext gc = this.getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            if (grelha == null) return;

            int rows = grelha.length;
            int cols = grelha[0].length;

            double tamanhoGrelhaVisual = Math.min(getWidth(), getHeight()) - 40;
            double cellW = tamanhoGrelhaVisual / cols;
            double cellH = tamanhoGrelhaVisual / rows;

            double offsetX = (getWidth() - tamanhoGrelhaVisual) / 2;
            double offsetY = (getHeight() - tamanhoGrelhaVisual) / 2;

            for (int i = 0; i < grelha.length; i++) {
                for (int j = 0; j < grelha[i].length; j++) {
                    double posX = offsetX + (j * cellW);
                    double posY = offsetY + (i * cellH);
                    String conteudo = grelha[i][j];

                    boolean isVisivel = conteudo.equals("M") ||
                            conteudo.equals("m") ||
                            conteudo.equals("A") ||
                            conteudo.equals(" ") ||
                            (i == linhaDrone && j == colunaDrone);

                    if (isVisivel) {
                        gc.setFill(Color.web("#0a2f5c"));
                        gc.fillRect(posX, posY, cellW, cellH);
                        desenhaObstaculo(gc, conteudo, posX, posY, cellW, cellH);
                    } else {
                        gc.setFill(Color.web("#031222"));
                        gc.fillRect(posX, posY, cellW, cellH);
                    }

                    // Desenho da malha da grelha
                    gc.setStroke(Color.web("#1d538f"));
                    gc.setLineWidth(1);
                    gc.strokeRect(posX, posY, cellW, cellH);
                }
            }

            // Moldura exterior
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeRect(offsetX, offsetY, tamanhoGrelhaVisual, tamanhoGrelhaVisual);

            // Renderização do Drone ativo
            if (linhaDrone != -1 && colunaDrone != -1) {
                double droneX = offsetX + (colunaDrone * cellW) + (cellW * 0.1);
                double droneY = offsetY + (linhaDrone * cellH) + (cellH * 0.1);
                double droneW = cellW * 0.8;
                double droneH = cellH * 0.8;

                if (imgDrone != null) {
                    gc.drawImage(imgDrone, droneX, droneY, droneW, droneH);
                } else {
                    gc.setFill(Color.RED);
                    gc.fillRect(droneX, droneY, droneW, droneH);
                }
            }
        }

        /**
         * Desenha os objetos específicos (minérios, artefactos ou monstros) numa célula.
         * @param gc O contexto gráfico do canvas.
         * @param type Símbolo do objeto.
         * @param x Posição X em pixels.
         * @param y Posição Y em pixels.
         * @param w Largura da célula.
         * @param h Altura da célula.
         */
        private void desenhaObstaculo(GraphicsContext gc, String type, double x, double y, double w, double h){
            double padding = 3;

            if (imgDrone == null || monstroMarinho == null || artefacto == null || minerio == null) {
                switch (type) {
                    case "A" -> {
                        gc.setFill(Color.GOLD);
                        gc.fillOval(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                    }
                    case "M" -> {
                        gc.setFill(Color.RED);
                        gc.fillRect(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                    }
                    case "m" -> {
                        gc.setFill(Color.SILVER);
                        gc.fillOval(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                    }
                }
            } else {
                switch (type) {
                    case "A" -> gc.drawImage(artefacto, x, y, w, h);
                    case "M" -> gc.drawImage(monstroMarinho, x, y, w, h);
                    case "m" -> gc.drawImage(minerio, x, y, w, h);
                }
            }
        }
    }
}