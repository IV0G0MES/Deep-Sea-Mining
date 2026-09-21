package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

public class FimJogoUI extends VBox {
    private DeepSeaManager manager;

    private Label lblResultado;
    private Label lblArtefactos;
    private Button btnMenuInicial;
    private Button btnSair;

    public FimJogoUI(DeepSeaManager manager) {
        this.manager = manager;

        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: rgba(2, 11, 20, 0.95); -fx-padding: 30px;");

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lblResultado = new Label();
        lblResultado.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));

        lblArtefactos = new Label();
        lblArtefactos.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 20));
        lblArtefactos.setTextFill(Color.WHITE);

        String estiloBotao = "-fx-background-radius: 8; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand; -fx-pref-width: 180px; -fx-pref-height: 40px;";

        btnMenuInicial = new Button("NOVO JOGO");
        btnMenuInicial.setStyle("-fx-background-color: linear-gradient(#3498db, #2980b9); " + estiloBotao);

        btnSair = new Button("SAIR DO JOGO");
        btnSair.setStyle("-fx-background-color: linear-gradient(#e74c3c, #c0392b); " + estiloBotao);

        this.getChildren().addAll(lblResultado, lblArtefactos, btnMenuInicial, btnSair);
    }

    private void registerHandlers() {

        btnMenuInicial.setOnAction(e -> {
            manager.novoJogo();
        });

        btnSair.setOnAction(e -> {
            Platform.exit();
        });
    }

    public void update() {
        DeepSeaState estado = manager.getState();

        if (estado == DeepSeaState.VITORIA || estado == DeepSeaState.GAME_OVER) {
            this.setVisible(true);

            int recolhidos = manager.getArtefacto();
            int totalJogo = manager.getQntArtefactosJogo();
            lblArtefactos.setText(String.format("Artefactos Resgatados: %d / %d", recolhidos, totalJogo));

            if (estado == DeepSeaState.VITORIA) {
                lblResultado.setText("🎉 VITÓRIA CONCLUÍDA! 🎉");
                lblResultado.setTextFill(Color.web("#2ecc71"));
            } else {
                lblResultado.setText("💀 GAME OVER 💀");
                lblResultado.setTextFill(Color.web("#e74c3c"));
            }
        } else {
            this.setVisible(false);
        }
    }
}