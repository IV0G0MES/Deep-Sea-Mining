package pt.isec.pa.deepsea.ui;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

import java.io.File;
import java.util.Objects;


public class RootPane extends StackPane {
    private ObjectProperty<DeepSeaManager> managerProperty;
    private Stage mainStage;
    private DeepSeaManager manager;
    private Scene scene;
    private Button btnNovoJogo;
    private Button btnCarregarJogo;
    private Button btnSair;
    private Label titulo;

    public RootPane(Stage mainStage,ObjectProperty<DeepSeaManager> managerProperty) {
        this.mainStage=mainStage;
        this.managerProperty=managerProperty;
        createViews();
        registerHandlers();
    }

    private void createViews(){
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ImageUIMenuInicial.png")));
        ImageView fundo = new ImageView(img);

        fundo.setOpacity(0.4);
        fundo.fitWidthProperty().bind(this.widthProperty());
        fundo.fitHeightProperty().bind(this.heightProperty());//Isto faz com que a imagem no caso a largura e a altura estejem "presas" a largura e altura da propria janela

        this.setStyle("-fx-background-color: black;");

        titulo = new Label("DEEP SEA MINING");
        titulo.setAlignment(Pos.CENTER);

        VBox caixaBotoes = new VBox(20);//Organizar botoes verticalmente
        caixaBotoes.setAlignment(Pos.CENTER);

        btnNovoJogo = new Button("Novo Jogo");
        btnCarregarJogo = new Button("Carregar Jogo");
        btnSair = new Button("Sair");

        btnNovoJogo.setPrefWidth(150);
        btnCarregarJogo.setPrefWidth(150);
        btnSair.setPrefWidth(150);

        caixaBotoes.getChildren().addAll(titulo,btnNovoJogo,btnCarregarJogo,btnSair);
        this.getChildren().addAll(fundo, caixaBotoes);
    }

    private void registerHandlers() {
        this.widthProperty().addListener((_, _, newVal) -> { //Isto serve para tornar o MenuInicial Responsivo a mudancas de tamanho do ecra

            double larguraEcra = newVal.doubleValue();

            double larguraBotao = larguraEcra * 0.30; // O botão ocupa 30% do ecrã
            double tamanhoFonteBotoes = larguraEcra * 0.025; // A letra do botão é 2.5% do ecrã
            double tamanhoFonteTitulo = larguraEcra * 0.06; // O título é 6% do ecrã

            btnNovoJogo.setPrefWidth(larguraBotao);
            btnCarregarJogo.setPrefWidth(larguraBotao);
            btnSair.setPrefWidth(larguraBotao);

            Font fonteBotoes = new Font("Arial", tamanhoFonteBotoes);
            btnNovoJogo.setFont(fonteBotoes);
            btnCarregarJogo.setFont(fonteBotoes);
            btnSair.setFont(fonteBotoes);
            titulo.setStyle("-fx-font-size: " + tamanhoFonteTitulo + "px; " + "-fx-font-weight: bold; " + "-fx-font-family: 'Arial'; " + "-fx-text-fill: linear-gradient(to bottom, #87CEFA, #00008B);");
        });

        btnNovoJogo.setOnAction((_event) -> {
            this.manager = new DeepSeaManager();
            manager.novoJogo();
            managerProperty.set(manager);
        });

        btnCarregarJogo.setOnAction((_event) -> {
            FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Escolher o ficheiro de gravação");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiros de Jogo (*.dat)", "*.dat")
            );

            File ficheiroEscolhido = fileChooser.showOpenDialog(mainStage);

            if (ficheiroEscolhido != null) {
                this.manager = new DeepSeaManager();
                String caminho = ficheiroEscolhido.getAbsolutePath();
                boolean carregou = manager.carregarJogo(caminho);
                if (carregou) {
                    managerProperty.set(manager);
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText("Falha ao Carregar");
                    erro.setContentText("O ficheiro escolhido não é válido ou está corrompido.");
                    erro.showAndWait();
                }
            }
        });

        btnSair.setOnAction(e -> System.exit(0));
    }
}
