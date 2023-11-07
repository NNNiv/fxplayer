package player.music;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.TextField;
        import javafx.stage.Stage;

public class EditUsernameController {

    @FXML
    private TextField newUsernameField;

    private String newUsername;

    private Stage stage;

    public void setStage(Stage dialogStage) {
        stage = dialogStage;
    }
    @FXML
    protected void applyUsername(ActionEvent event) {
        newUsername = newUsernameField.getText();
        closeDialog();
    }

    @FXML
    protected void cancel(ActionEvent event) {
        closeDialog();
    }

    public String getNewUsername() {
        return newUsername;
    }

    private void closeDialog() {
        if (stage != null)
            stage.close();

    }
}
