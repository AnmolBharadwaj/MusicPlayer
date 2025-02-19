import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends Application {
    private MediaPlayer mediaPlayer;
    private List<String> playlist = new ArrayList<>();
    private int currentSongIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        // Load songs from the "songs" folder
        File folder = new File("songs");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    playlist.add(file.toURI().toString());
                }
            }
        }

        if (playlist.isEmpty()) {
            System.out.println("No songs found in the 'songs' folder.");
            return;
        }

        // Create media and media player
        Media media = new Media(playlist.get(currentSongIndex));
        mediaPlayer = new MediaPlayer(media);

        // GUI Components
        Label nowPlayingLabel = new Label("Now Playing: " + new File(playlist.get(currentSongIndex)).getName());
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button nextButton = new Button("Next");
        Button previousButton = new Button("Previous");
        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);

        // Event Handlers
        playButton.setOnAction(e -> mediaPlayer.play());
        pauseButton.setOnAction(e -> mediaPlayer.pause());
        stopButton.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
        });
        nextButton.setOnAction(e -> playNextSong());
        previousButton.setOnAction(e -> playPreviousSong());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                mediaPlayer.setVolume(newValue.doubleValue() / 100));

        // Layout
        HBox controls = new HBox(10, playButton, pauseButton, stopButton, previousButton, nextButton);
        controls.setPadding(new Insets(10));
        VBox root = new VBox(10, nowPlayingLabel, controls, new Label("Volume:"), volumeSlider);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("Java Music Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void playNextSong() {
        if (currentSongIndex < playlist.size() - 1) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0;
        }
        updateMediaPlayer();
    }

    private void playPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
        } else {
            currentSongIndex = playlist.size() - 1;
        }
        updateMediaPlayer();
    }

    private void updateMediaPlayer() {
        mediaPlayer.stop();
        Media media = new Media(playlist.get(currentSongIndex));
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}