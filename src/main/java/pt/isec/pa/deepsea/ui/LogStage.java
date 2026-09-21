package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class LogStage extends Stage implements PropertyChangeListener {
    private DeepSeaManager manager;
    private ListView<String> logs;
    private Button btnSave, btnClear;

    public LogStage(DeepSeaManager manager) {
        this.manager = manager;
        setTitle("DeepSea Logs");
        setMinHeight(400);
        setMinWidth(400);

        createViews();
        registerHandlers();
        update();
        BorderPane pane = new BorderPane();
        pane.setCenter(logs);
        pane.setBottom(createBottomBar());
        Scene scene = new Scene(pane);
        this.setScene(scene);

    }

    private void createViews(){
        logs = new ListView<>();
        logs.setStyle("-fx-background-color: #FFFFFF; -fx-font-family: 'Courier New'");

        btnSave = new Button("Gravar Logs");
        btnClear = new Button("Limpar Logs");
    }

    private HBox createBottomBar(){
        HBox hb = new HBox(10, btnSave, btnClear);
        hb.setPadding(new Insets(10));
        hb.setStyle("-fx-background-color: #111111; -fx-font-family: 'Courier New'");
        return hb;
    }

    private void registerHandlers(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP, this);

        btnClear.setOnAction(event -> {
            manager.resetLog();
        });
        btnSave.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar Log");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto", "*.txt"));
            fc.setInitialFileName("deapsea_logs.txt");
            File file = fc.showSaveDialog(this);
            if (file != null) {
                manager.guardarLog(file.getAbsolutePath());
            }
        });

        this.setOnCloseRequest(event -> {
            manager.removePropertyChangeListener(this);
            System.out.println("LogStage desligado com sucesso!");
        });
    }

    private void update(){
        Platform.runLater(() -> {
            logs.getItems().clear();
            logs.getItems().addAll(manager.getLog());
            if (!logs.getItems().isEmpty()) {
                logs.scrollTo(logs.getItems().size()-1);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        update();
    }
}
