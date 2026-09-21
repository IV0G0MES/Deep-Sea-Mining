package pt.isec.pa.deepsea.ui.res;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager soundManager;
    private final BooleanProperty somLigado = new SimpleBooleanProperty(true);
    private final Map<String, Media> sonsCarregados = new HashMap<>();
    private MediaPlayer movimento;
    private MediaPlayer especial;
    private MediaPlayer BackGround;
    private String BackgroundAtual;


    private SoundManager() {
        BackgroundAtual="";
        preloadSounds();
    }

    public static SoundManager getInstance() {
        if (soundManager == null) {
            soundManager = new SoundManager();
        }
        return soundManager;
    }

    public BooleanProperty soundEnabledProperty() {
        return somLigado;
    }

    public boolean isSoundEnabled() {
        return somLigado.get();
    }

    private void preloadSounds() {
        String[] soundFiles = {"error.wav","sonar.mp3","superficie.mp3","pecaMiniJogo.wav","movimentoNavio.wav","droneDestruido.mp3","minerio.wav","descerFosso-Fundo.mp3","movimentoDrone.wav","animalMarinho.wav","Corrente.wav","Monstro.wav","arranjo.wav","rock.wav","musicaMiniJogo.mp3"};
        for (String file : soundFiles) {
            try {
                URL resource = getClass().getResource("/sounds/" + file);
                if (resource != null) {
                    sonsCarregados.put(file, new Media(resource.toExternalForm()));
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar o som " + file + ": " + e.getMessage());
            }
        }
    }

    public void playMovimento(String nomeSom) {
        if (!isSoundEnabled()) return;

        Media media = sonsCarregados.get(nomeSom);
        if (media == null) return;

        if (movimento != null) {
            movimento.stop();
        }

        movimento = new MediaPlayer(media);
        movimento.play();
    }

    public void playEspecial(String moveSound, String specialSound) {
        if (!isSoundEnabled()) return;

        Media moveMedia = sonsCarregados.get(moveSound);
        Media specialMedia = sonsCarregados.get(specialSound);
        if (moveMedia == null || specialMedia == null) return;

        if (movimento != null) {
            movimento.stop();
        }

        movimento = new MediaPlayer(moveMedia);

        movimento.setOnEndOfMedia(() -> {
            if (especial != null) {
                especial.stop();
            }
            especial = new MediaPlayer(specialMedia);
            especial.play();
        });

        movimento.play();
    }

    public void playUmaVez(String soundName) {
        if (!isSoundEnabled()) return;

        Media media = sonsCarregados.get(soundName);
        if (media != null) {
            MediaPlayer p = new MediaPlayer(media);
            p.play();
        }
    }

    public void playMusicaDeFundo(String nomeSom) {
        if (!isSoundEnabled()) return;

        Media media = sonsCarregados.get(nomeSom);
        if (media == null) return;

        if (nomeSom.equals(BackgroundAtual) && BackGround != null) {
            return;
        }
        if (BackGround != null) {
            BackGround.stop();
        }
        BackGround = new MediaPlayer(media);
        BackGround.setCycleCount(MediaPlayer.INDEFINITE);
        BackGround.play();
        BackgroundAtual = nomeSom;
    }

    public void stopMusicaDeFundo() {
        if (BackGround != null) {
            BackGround.stop();
            BackGround = null;
            BackgroundAtual="";
        }
    }
}
