package player.music;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicController {
    public ImageView waveform;
    public ImageView profile;
    public Button add;
    int isRandom = 0;
    int isPlaying = 0;
    int isLooping = 0;
    long clipTime = 0;
    int currentSongIndex = 0;
    Clip clip;
    File media = new File("media");
    File[] songs = media.listFiles();

    @FXML
    Button random;

    @FXML
    Button prev;

    @FXML
    Button play;

    @FXML
    Button next;

    @FXML
    Button loop;

    @FXML
    VBox radioButtonContainer;

    @FXML
    Label currentPlay;

    @FXML
    Label username;

    @FXML
    public void initialize() {
        Image profilePic = new Image(new File("assets/profile.png").toURI().toString());
        profile.setImage(profilePic);
        Image waveformGIF = new Image(new File("assets/waveform.gif").toURI().toString());
        waveform.setImage(waveformGIF);
        play.setOnAction(event -> togglePlayback(currentSongIndex));
        if (songs != null && songs.length > 0) {
            playSong(currentSongIndex);
        }
        displayCurrentSong();
        createRadioButtons();
    }
    @FXML
    public void addSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a song to add");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.flac", "*.m4a")); // Add more extensions if needed

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                File destination = new File("media", selectedFile.getName());
                copyFile(selectedFile, destination);
                songs = media.listFiles(); // Refresh the list of songs
                createRadioButtons(); // Refresh the radio buttons
            } catch (IOException e) {
                System.err.println("Error copying file: " + e.getMessage());
            }
        }
    }


    private void copyFile(File sourceFile, File destFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(sourceFile);
        FileOutputStream outputStream = new FileOutputStream(destFile);
        FileChannel sourceChannel = inputStream.getChannel();
        FileChannel destChannel = outputStream.getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
        sourceChannel.close();
        destChannel.close();
        inputStream.close();
        outputStream.close();
    }

    public void displayCurrentSong() {
        currentPlay.setText(songs[currentSongIndex].toString().substring(6));
    }

    private void createRadioButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonContainer.getChildren().clear();

        for (int i = 0; i < songs.length; i++) {
            RadioButton radioButton = new RadioButton(songs[i].getName());
            radioButton.getStyleClass().add("radio-button");
            radioButton.setToggleGroup(toggleGroup);

            final int index = i;
            radioButton.setOnAction(event -> {
                currentSongIndex = index;
                clipTime = 0;
                playSong(currentSongIndex);
            });

            radioButtonContainer.getChildren().add(radioButton);
        }
    }
    //            radioButton.setStyle(
//                    "-fx-font-size: 14px; fx-text-fill: #FFFFFF;"
//                    );
    void playSong(int songIndex) {
        try {
            if (songs == null || songIndex < 0 || songIndex >= songs.length) {
                return;
            }

            if (clip != null) {
                clip.close();
            }

            File musicPath = songs[songIndex];
            System.out.println("Current Index: " + songIndex);
            System.out.println("Music playback: " + musicPath);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                play.setText("󰏤");
                clip.setMicrosecondPosition(clipTime);
                clip.start();
                isPlaying = 1;
                System.out.println("State: " + isPlaying);
                displayCurrentSong();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    protected void togglePlayback(int songIndex) {
        try {
            if (clip != null) {
                if (isPlaying == 1) {
                    waveform.setImage(null);
                    play.setText("");
                    isPlaying = 0;
                    clipTime = clip.getMicrosecondPosition();
                    System.out.println("Pause Clip Time: " + clipTime);
                    clip.stop();
                    System.out.println("State: " + isPlaying);
                } else {
                    Image waveformGIF = new Image(new File("assets/waveform.gif").toURI().toString());
                    waveform.setImage(waveformGIF);
                    play.setText("󰏤");
                    System.out.println("Play Clip Time: " + clipTime);
                    clip.setMicrosecondPosition(clipTime);
                    clip.start();
                    isPlaying = 1;
                    System.out.println("State: " + isPlaying);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    protected void toggleLoop() {
        if (isLooping == 1) {
            isLooping = 0;
        } else {
            isLooping = 1;
        }
    }

    @FXML
    protected void goToPrev() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
        } else {
            currentSongIndex = songs.length - 1;
        }
        clipTime = 0;
        playSong(currentSongIndex);
        System.out.println("Prev Song");
    }

    @FXML
    protected void goToNext() {
        if (currentSongIndex < songs.length - 1) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0;
        }
        clipTime = 0;
        playSong(currentSongIndex);
        System.out.println("Next Song");
    }

    @FXML
    protected void randomizeSongs() {
        if (isRandom == 0) {
//            random.setText("Random");
            isRandom = 1;
        } else {
//            random.setText("Randomize");
            isRandom = 0;
        }
    }
}
