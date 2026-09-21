package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.SoundManager;

import java.util.Objects;

/**
 * Interface de utilizador responsável pela representação visual e controlo do drone durante a fase do Fosso.
 * <p>
 * Gere a transição entre a superfície e o fundo, tratando de inputs de movimento, colisões com rochas,
 * interações com fauna marinha e correntes, bem como a reprodução de efeitos sonoros contextuais.
 * </p>
 *
 * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class FossoUI extends BorderPane {
    private DeepSeaManager manager;
    private FossoCanvas canvas;
    private Pane areaPane;
    private ImageView imagemFundo;

    /**
     * Construtor da classe FossoUI.
     * @param manager Gestor principal que fornece os dados e controlo da lógica de jogo.
     */
    public FossoUI(DeepSeaManager manager){
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    /**
     * Inicializa os componentes visuais e configura a hierarquia de camadas (StackPane).
     */
    private void createViews() {
        canvas = new FossoCanvas();
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
     * Configura os ouvintes de eventos para redimensionamento, notificações do modelo
     * e processamento de teclas de movimento e interação.
     */
    private void registerHandlers(){
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

        manager.addPropertyChangeListener(DeepSeaManager.PROP, event -> {
            update();
        });

        this.setOnKeyPressed(event -> {
            if (!this.isVisible()) return;
            int direcaoX=0;
            int direcaoY=0;
            switch (event.getCode()) {
                case UP, W -> direcaoX=-1;
                case DOWN, S -> direcaoX=1;
                case LEFT, A -> direcaoY=-1;
                case RIGHT, D -> direcaoY=1;
                default -> { return; }
            }
            if(direcaoX!=0 || direcaoY!=0){
                int posAtualX=manager.getDroneAtivoPosX();
                int posAtualY=manager.getDroneAtivoPosY();
                String[][] fossoBefore=manager.getGrelhaFosso();
                if(manager.moverDrone(direcaoX,direcaoY)){
                    if(BateRocha(direcaoX,direcaoY) && posAtualX==manager.getDroneAtivoPosX()&&posAtualY==manager.getDroneAtivoPosY()){
                        SoundManager.getInstance().playEspecial("movimentoDrone.wav","rock.wav");
                    }else if(Animal(fossoBefore)){
                        SoundManager.getInstance().playEspecial("movimentoDrone.wav","animalMarinho.wav");
                    }else if(Corrente(fossoBefore)) {
                        SoundManager.getInstance().playEspecial("movimentoDrone.wav","Corrente.wav");
                    }else{
                        SoundManager.getInstance().playMovimento("movimentoDrone.wav");
                    }
                }
            }
            event.consume();
        });
    }

    /**
     * Sincroniza a visibilidade do componente com o estado atual da FSM e solicita a atualização do desenho.
     */
    private void update() {
        this.setVisible(manager.getState() == DeepSeaState.DESCE_FOSSO || manager.getState() == DeepSeaState.SOBE_FOSSO);

        if (this.isVisible()) {
            String[][] grid = manager.getGrelhaFosso();
            canvas.update(grid);
            Platform.runLater(this::requestFocus);
        }
    }

    /**
     * Verifica se o movimento pretendido resulta em colisão com uma rocha.
     * @param direcaoX Deslocamento horizontal pretendido.
     * @param direcaoY Deslocamento vertical pretendido.
     * @return true se a posição destino for uma rocha.
     */
    private boolean BateRocha(int direcaoX,int direcaoY){
        int posX = manager.getDroneAtivoPosX()+direcaoX;
        int posY = manager.getDroneAtivoPosY()+direcaoY;
        String[][] grelha = manager.getGrelhaFosso();

        if (grelha != null && posX >= 0 && posX < grelha.length && posY >= 0 && posY < grelha[0].length) {
            if ("R".equals(grelha[posX][posY])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se existe um animal marinho na posição atual do drone.
     * @param fosso A matriz representativa do estado do fosso.
     * @return true se o drone estiver sobre um animal marinho.
     */
    private boolean Animal(String[][] fosso){
        int posX = manager.getDroneAtivoPosX();
        int posY = manager.getDroneAtivoPosY();
        String[][] grelha = manager.getGrelhaFosso();

        if (grelha != null && posX >= 0 && posX < grelha.length && posY >= 0 && posY < grelha[0].length) {
            if ("A".equals(fosso[posX][posY])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se existe uma corrente marítima na posição atual do drone.
     * @param fosso A matriz representativa do estado do fosso.
     * @return true se o drone estiver sob influência de uma corrente.
     */
    private boolean Corrente(String[][] fosso){
        int posX = manager.getDroneAtivoPosX();
        int posY = manager.getDroneAtivoPosY();
        String[][] grelha = manager.getGrelhaFosso();

        if (grelha != null && posX >= 0 && posX < grelha.length && posY >= 0 && posY < grelha[0].length) {
            if ("C".equals(fosso[posX][posY])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Classe interna especializada no renderização gráfica do fosso.
     */
    class FossoCanvas extends Canvas {
        private String[][] grid;
        private Image drone, rocha, animalMarinho, correnteMaritima;

        private FossoCanvas() {
            super(500, 500);
            createViews();
            registerHandlers();
        }

        /**
         * Carrega os recursos de imagem para os elementos do fosso.
         */
        private void createViews() {
            try {
                drone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/FotoDrone.png")));
                rocha = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/rocha.png")));
                animalMarinho = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/animalMarinho.png")));
                correnteMaritima = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/CorrenteMaritima.png")));
            } catch(Exception e) {
                System.out.println("Erro: Não foi possível carregar as imagens do FossoCanvas.");
                drone = null;
                rocha = null;
                animalMarinho = null;
                correnteMaritima = null;
            }
        }

        private void registerHandlers() {
        }

        /**
         * Atualiza os dados internos da grelha e despoleta o redesenho do componente.
         * @param grid Matriz de strings com a representação dos elementos do fosso.
         */
        private void update(String[][] grid) {
            this.grid = grid;
            desenhaFosso();
        }

        /**
         * Executa o ciclo de desenho gráfico utilizando GraphicsContext.
         * Calcula dinamicamente o posicionamento centralizado e as dimensões das células.
         */
        private void desenhaFosso(){
            GraphicsContext gc = this.getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            if(grid == null) return;

            int rows = grid.length;
            int cols = grid[0].length;

            double tamGrid = Math.min(getWidth(), getHeight()) - 40;

            double cellW = tamGrid / cols;
            double cellH = tamGrid / rows;

            double offsetX = (getWidth() - tamGrid) / 2;
            double offsetY = (getHeight() - tamGrid) / 2;

            gc.setFill(Color.web("#0a2f5c"));
            gc.fillRect(offsetX, offsetY, tamGrid, tamGrid);

            for(int i = 0; i < grid.length; i++) {
                for(int j = 0; j < grid[i].length; j++) {
                    double posX = offsetX + (j * cellW);
                    double posY = offsetY + (i * cellH);

                    gc.setStroke(Color.web("#1d538f"));
                    gc.setLineWidth(1);
                    gc.strokeRect(posX, posY, cellW, cellH);

                    desenhaObjeto(gc, grid[i][j], posX, posY, cellW, cellH);
                }
            }

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeRect(offsetX, offsetY, tamGrid, tamGrid);
        }

        /**
         * Desenha um objeto individual (imagem ou forma geométrica) numa célula específica.
         * @param gc Contexto gráfico do canvas.
         * @param type Tipo de objeto identificado pelo símbolo na grelha.
         * @param x Coordenada X do desenho.
         * @param y Coordenada Y do desenho.
         * @param w Largura da célula.
         * @param h Altura da célula.
         */
        private void desenhaObjeto(GraphicsContext gc, String type, double x, double y, double w, double h) {
            double padding = 3;

            if(rocha == null || animalMarinho == null || correnteMaritima == null || drone == null) {
                switch (type) {
                    case "R" -> {
                        gc.setFill(Color.GRAY);
                        gc.fillRect(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                    }
                    case "A" -> {
                        gc.setFill(Color.MAGENTA);
                        gc.fillOval(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                    }
                    case "C" -> {
                        gc.setFill(Color.CYAN);
                        gc.fillOval(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                    }
                    case "D" -> {
                        gc.setFill(Color.RED);
                        gc.fillRect(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                        gc.setStroke(Color.WHITE);
                        gc.strokeRect(x + padding, y + padding, w - 2 * padding, h - 2 * padding);
                    }
                }
            } else {
                switch (type) {
                    case "R" -> gc.drawImage(rocha, x, y, w, h);
                    case "A" -> gc.drawImage(animalMarinho, x, y, w, h);
                    case "C" -> gc.drawImage(correnteMaritima, x, y, w, h);
                    case "D" -> gc.drawImage(drone, x, y, w, h);
                }
            }
        }
    }
}