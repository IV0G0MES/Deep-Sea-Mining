package pt.isec.pa.deepsea.ui;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

public class MainJFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ObjectProperty<DeepSeaManager> managerProperty = new SimpleObjectProperty<>(null);

        RootPane rootA = new RootPane(stage, managerProperty);
        Scene sceneA = new Scene(rootA, 800, 600);
        stage.setScene(sceneA);
        stage.setTitle("Janela Principal - Projeção A");
        stage.show();
        Stage stageB = new Stage();
        RootPane rootB = new RootPane(stageB, managerProperty);
        Scene sceneB = new Scene(rootB, 800, 600);
        stageB.setScene(sceneB);
        stageB.setTitle("Janela Secundária - Projeção B");
        stageB.show();

        managerProperty.addListener((obs, oldManager, newManager) -> {
            if (newManager != null) {
                JogoUI jogoA = new JogoUI(newManager);
                JogoUI jogoB = new JogoUI(newManager);

                stage.getScene().setRoot(jogoA);
                stageB.getScene().setRoot(jogoB);
            }
        });

        stageB.setOnCloseRequest(windowEvent -> stage.close());

        /*createOneStage(stage);
        Stage stage1 = new Stage();
        createOneStage(stage1);
        stage1.setOnCloseRequest(windowEvent -> stage.close());*/
    }

    /*private void createOneStage(Stage stage) {
        RootPane root = new RootPane(stage);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Deep Sea Mining");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }*/

//       DeepSeaManager manager=new DeepSeaManager();
//        JogoUI menuInicial = new JogoUI();
//        RootPane rootJogo=new RootPane(manager);
//        Scene scene = new Scene(menuInicial, 800, 600);
//
//        menuInicial.setAcaoNovoJogo(e -> {
//            manager.novoJogo();
//            scene.setRoot(rootJogo);
//        });
//
//        menuInicial.setAcaoCarregarJogo(e -> {
//            FileChooser fileChooser = new javafx.stage.FileChooser();
//            fileChooser.setTitle("Escolher o ficheiro de gravação");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiros de Jogo (*.dat)", "*.dat")
//            );
//
//            File ficheiroEscolhido = fileChooser.showOpenDialog(stage);
//
//            if (ficheiroEscolhido != null) {
//                String caminho = ficheiroEscolhido.getAbsolutePath();
//                boolean carregou = manager.carregarJogo(caminho);
//                if (carregou) {
//                    scene.setRoot(rootJogo);
//                } else {
//                    Alert erro = new Alert(Alert.AlertType.ERROR);
//                    erro.setTitle("Erro");
//                    erro.setHeaderText("Falha ao Carregar");
//                    erro.setContentText("O ficheiro escolhido não é válido ou está corrompido.");
//                    erro.showAndWait();
//                }
//            }
//        });

}
