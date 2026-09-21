package pt.isec.pa.deepsea.ui;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.SoundManager;

public class JogoUI extends BorderPane {
    private DeepSeaManager manager;
    private StackPane stackCentral;
    private VBox lateralInfoBox;
    private Label lblInfoGeral;
    private MenuBar menuBar;
    private Menu menuGame,menuLog, menuOpenRecent;
    private MenuItem menuNew,menuOpen,menuSaveAs,menuExit,menuShowHide,menuSaveLogs,menuClearLogs;
    private Button btnOficina;
    private LogStage logStage;
    private FossoUI fosso;
    private SuperficieUI superficie;
    private FundoUI fundo;
    private MiniJogoUI miniJogo;
    private FimJogoUI fimJogo;
    private List<String> recentFiles = new ArrayList<>();

    public JogoUI(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        menuBar = new MenuBar();
        menuGame = new Menu("Game");
        menuNew = new MenuItem("New");
        menuOpen = new MenuItem("Open...");
        menuOpenRecent = new Menu("Open recent");
        menuSaveAs = new MenuItem("Save as...");
        menuExit = new MenuItem("Exit");
        menuGame.getItems().addAll(menuNew, menuOpen, menuOpenRecent, menuSaveAs, new SeparatorMenuItem(), menuExit);
        menuLog = new Menu("Log");
        menuShowHide = new MenuItem("Show/Hide");
        menuLog.getItems().addAll(menuShowHide);

        menuBar.getMenus().addAll(menuGame, menuLog);
        this.setTop(menuBar);

        lateralInfoBox = new VBox(15);
        lateralInfoBox.setPrefWidth(260);
        lateralInfoBox.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #00004d, #adbef6);" +
                        "-fx-padding: 15px;" +
                        "-fx-border-color: transparent transparent transparent white; -fx-border-width: 1px;"
        );

        lblInfoGeral = new Label("Info Jogo");
        lblInfoGeral.setStyle(
                "-fx-font-family: 'Segoe UI', Arial, sans-serif; " +
                        "-fx-font-size: 14px; " +
                        "-fx-text-fill: linear-gradient(white, #ecf0f1); " +
                        "-fx-line-spacing: 0.6em; " +
                        "-fx-font-weight: bold;"
        );
        lblInfoGeral.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(lblInfoGeral);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        btnOficina = new Button("OFICINA");
        btnOficina.setPrefWidth(220);
        btnOficina.setPrefHeight(45);
        btnOficina.setStyle(
                "-fx-background-color: linear-gradient(#2ecc71, #27ae60); " +
                        "-fx-background-radius: 10; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Segoe UI', Arial; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 8, 0, 0, 8);"
        );


        lateralInfoBox.getChildren().addAll(scrollPane, btnOficina);
        this.setRight(lateralInfoBox);

        stackCentral = new StackPane();
        superficie = new SuperficieUI(manager);
        fosso = new FossoUI(manager);
        fundo = new FundoUI(manager);
        fimJogo = new FimJogoUI(manager);
        miniJogo = new MiniJogoUI(manager);
        stackCentral.getChildren().addAll(superficie, fosso, fundo, miniJogo,fimJogo);
        this.setCenter(stackCentral);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP,evt -> {update();});

        DropShadow glowOficina = new DropShadow();
        glowOficina.setColor(Color.web("#2ecc71"));
        glowOficina.setRadius(15);
        glowOficina.setSpread(0.2);

        btnOficina.setOnMouseEntered(e -> {
            btnOficina.setStyle(
                    "-fx-background-color: linear-gradient(#4ce38e, #2ecc71); " +
                            "-fx-background-radius: 10; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-cursor: hand;"
            );
            btnOficina.setEffect(glowOficina);
        });

        btnOficina.setOnMouseExited(e -> {
            btnOficina.setStyle(
                    "-fx-background-color: linear-gradient(#2ecc71, #27ae60); " +
                            "-fx-background-radius: 10; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-cursor: hand;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 8, 0, 0, 8);"
            );
            btnOficina.setEffect(null);
        });

        btnOficina.setOnAction(e -> {
            abrirJanelaOficina();
        });

        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            double alturaJanela = newVal.doubleValue();

            int tamanhoLetra = (int) Math.max(10, Math.min(14, alturaJanela / 45));

            double espacamentoLinha = Math.max(0.1, Math.min(0.5, (alturaJanela / 1500)));

            lblInfoGeral.setStyle(
                    "-fx-font-family: 'Segoe UI', Arial, sans-serif; " +
                            "-fx-font-size: " + tamanhoLetra + "px; " +
                            "-fx-text-fill: linear-gradient(white, #ecf0f1); " +
                            "-fx-line-spacing: " + espacamentoLinha + "em; " +
                            "-fx-font-weight: bold;"
            );
        });

        menuShowHide.setOnAction(e -> {
            if(logStage == null || !logStage.isShowing()) {
                logStage = new LogStage(manager);
                logStage.show();
            }else {
                logStage.close();
            }
        });

        menuOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Escolher o ficheiro de gravação");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiros de Jogo (*.dat)", "*.dat"));

            File ficheiroEscolhido = fileChooser.showOpenDialog(this.getScene().getWindow());


            if (ficheiroEscolhido != null) {

                String caminho = ficheiroEscolhido.getAbsolutePath();

                boolean carregou = manager.carregarJogo(caminho);

                if(carregou) {
                    updateRecentFiles(caminho);
                }
                else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText("Falha ao Carregar");
                    erro.setContentText("O ficheiro escolhido não é válido ou está corrompido.");
                    erro.showAndWait();
                }
            }
        });

        menuSaveAs.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DeapSea (*.dat)", "*.dat"));
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());

            if (file != null) {

                String caminho = file.getAbsolutePath();
                boolean gravou = manager.guardarJogo(caminho);
                if (gravou) {
                    updateRecentFiles(caminho);
                    Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "O teu progresso foi guardado com sucesso!", ButtonType.OK);
                    sucesso.setTitle("Jogo Guardado");
                    sucesso.setHeaderText(null);
                    sucesso.showAndWait();
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível gravar o jogo. Tenta novamente.", ButtonType.OK);
                    erro.setTitle("Erro ao Gravar");
                    erro.setHeaderText(null);
                    erro.showAndWait();
                }
            }

            if (file != null) {

                String caminho = file.getAbsolutePath();
                boolean gravou = manager.guardarJogo(caminho);
                if (gravou) {
                    Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "O teu progresso foi guardado com sucesso!", ButtonType.OK);
                    sucesso.setTitle("Jogo Guardado");
                    sucesso.setHeaderText(null);
                    sucesso.showAndWait();
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível gravar o jogo. Tenta novamente.", ButtonType.OK);
                    erro.setTitle("Erro ao Gravar");
                    erro.setHeaderText(null);
                    erro.showAndWait();
                }
            }

        });

        menuExit.setOnAction(e -> {
            Platform.exit();
        });

        menuNew.setOnAction(e->{
            manager.novoJogo();
        });
    }

    private void update() {
        DeepSeaState estado = manager.getState();

        fimJogo.update();

        if (estado == DeepSeaState.VITORIA || estado == DeepSeaState.GAME_OVER) {
            this.setRight(null);
        } else {
            this.setRight(lateralInfoBox);
        }

        switch (estado) {
            case SUPERFICIE ->
                    SoundManager.getInstance().playMusicaDeFundo("superficie.mp3");
            case DESCE_FOSSO, SOBE_FOSSO, FUNDO_MAR ->
                    SoundManager.getInstance().playMusicaDeFundo("sonar.mp3");
            case MINI_JOGO ->
                    SoundManager.getInstance().playMusicaDeFundo("musicaMiniJogo.mp3");
            default ->
                    SoundManager.getInstance().stopMusicaDeFundo();
        }

        if(estado == DeepSeaState.SUPERFICIE){

            StringBuilder infoSuperficie = new StringBuilder();

            infoSuperficie.append("=== NAVIO ===\n");
            infoSuperficie.append("Estado: ").append(manager.getState()).append("\n");
            infoSuperficie.append("Combustível: ").append(String.format("%.2f", manager.getCombustivelNavio())).append("\n");
            infoSuperficie.append("Minérios: ").append(manager.getQtdMinerios()).append("\n");
            infoSuperficie.append("Artefactos: ").append(manager.getArtefacto()).append("/").append(manager.getQntArtefactosJogo()).append("\n\n");

            infoSuperficie.append("=== FROTA DE DRONES ===\n");

            List<String> infoDrones = manager.getDronesPorCasco();

            for (String droneStr : infoDrones) {
                String droneFormatado = droneStr.replace(" | ", "\n  ");
                infoSuperficie.append(droneFormatado).append("\n\n");
            }

            lblInfoGeral.setText(infoSuperficie.toString());
            btnOficina.setVisible(true);
        }
        else {
            lblInfoGeral.setText("=== DADOS JOGO ===\n" +
                    "Estado: " + manager.getState() + "\n" +
                    "Combustivel Drone: " + String.format("%.2f", manager.getDroneAtivoCombustivel()) + "\n" +
                    "Integridade Drone: " + String.format("%.2f", manager.getIntegridadeDrone()));

            btnOficina.setVisible(false);
        }
    }

    private void abrirJanelaOficina() {

        Stage janelaOficina = new Stage();
        janelaOficina.setTitle("Oficina Submarina");

        janelaOficina.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        janelaOficina.setResizable(false); // Bloqueia o tamanho para não estragar o layout


        Image imgFundo;
        try {
            imgFundo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/oficina.jpg")));
        } catch (Exception ex) {
            System.out.println("Erro ao carregar oficina.jpg");
            return;
        }
        ImageView bgView = new ImageView(imgFundo);
        bgView.setFitWidth(800);
        bgView.setFitHeight(450);


        AnchorPane layout = new AnchorPane();


        ComboBox<Integer> comboDrones = new ComboBox<>();
        comboDrones.getItems().addAll(1, 2, 3);
        comboDrones.setValue(1);
        comboDrones.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-background-color: #00e6e6;");
        Label lblSelecione = new Label("A operar Drone ID:");
        lblSelecione.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        HBox boxTopo = new HBox(10, lblSelecione, comboDrones);
        boxTopo.setAlignment(Pos.CENTER);
        boxTopo.setStyle("-fx-background-color: rgba(0, 0, 50, 0.7); -fx-padding: 10px; -fx-background-radius: 10px;");


        String estiloBotao = "-fx-background-color: rgba(0, 200, 255, 0.2); -fx-border-color: #00ffff; " +
                "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;";


        Button btnMelhorarCasco = new Button("🛡️ MELHORAR CASCO");
        btnMelhorarCasco.setStyle(estiloBotao);

        Button btnMelhorarComb = new Button("⚡ MELHORAR TANQUE");
        btnMelhorarComb.setStyle(estiloBotao);

        Button btnReparar = new Button("🔧 REPARAR");
        btnReparar.setStyle(estiloBotao);
        Button btnReabastecer = new Button("⛽ REABASTECER");
        btnReabastecer.setStyle(estiloBotao);
        HBox boxMeio = new HBox(20, btnReparar, btnReabastecer);

        AnchorPane.setTopAnchor(boxTopo, 20.0);
        AnchorPane.setLeftAnchor(boxTopo, 300.0);
        AnchorPane.setLeftAnchor(btnMelhorarCasco, 40.0);
        AnchorPane.setTopAnchor(btnMelhorarCasco, 200.0);

        AnchorPane.setRightAnchor(btnMelhorarComb, 40.0);
        AnchorPane.setTopAnchor(btnMelhorarComb, 200.0);

        AnchorPane.setBottomAnchor(boxMeio, 40.0);
        AnchorPane.setLeftAnchor(boxMeio, 280.0);


        btnReabastecer.setOnAction(ev -> {
            int id = comboDrones.getValue();
            if(manager.abastecerDrone(id, 50.0)){
                SoundManager.getInstance().playMovimento("arranjo.wav");
                System.out.println("Drone " + id + " reabastecido!");
            }
            else{
                System.out.println("Falha ao reabastecer o Drone");
            }
        });

        btnReparar.setOnAction(ev -> {
            int id = comboDrones.getValue();
            if(manager.reparaDrone(id, 50.0)){
                SoundManager.getInstance().playMovimento("arranjo.wav");
                System.out.println("Drone " + id + " reparado!");
            }
            else{
                System.out.println("Falha ao reparar o Drone " + id );
            }

        });

        btnMelhorarCasco.setOnAction(ev -> {
            int id = comboDrones.getValue();
            if(manager.melhoraDrone(id, 2)){
                SoundManager.getInstance().playMovimento("arranjo.wav");
                System.out.println("Casco do Drone " + id + " melhorado!");
            }
            else{
                System.out.println("Falha ao melhorar o casco do Drone " + id);
            }
        });

        btnMelhorarComb.setOnAction(ev -> {
            int id = comboDrones.getValue();
            if(manager.melhoraDrone(id, 1)){
                SoundManager.getInstance().playMovimento("arranjo.wav");
                System.out.println("Tanque do Drone " + id + " melhorado!");
            }else {
                System.out.println("Falha ao melhorar o Tanque do Drone " + id);
            }
        });


        StackPane rootOficina = new StackPane(bgView, layout);
        layout.getChildren().addAll(boxTopo, btnMelhorarCasco, btnMelhorarComb, boxMeio);

        Scene cenaOficina = new Scene(rootOficina, 800, 450);
        janelaOficina.setScene(cenaOficina);
        janelaOficina.showAndWait();

        Platform.runLater(() -> {
            superficie.requestFocus();
        });
    }
    private void updateRecentFiles(String path){
        recentFiles.remove(path);
        recentFiles.add(0, path);
        if(recentFiles.size() > 5 )
            recentFiles.remove(5);

        menuOpenRecent.getItems().clear();
        for (String caminho : recentFiles){
            MenuItem menuItem = new MenuItem(caminho);
            menuItem.setOnAction(ev -> {
                if(manager.carregarJogo(caminho)){
                    updateRecentFiles(caminho);
                }
            });
            menuOpenRecent.getItems().add(menuItem);
        }
    }
}