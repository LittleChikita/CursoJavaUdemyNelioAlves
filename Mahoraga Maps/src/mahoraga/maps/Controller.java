package mahoraga.maps;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mahoraga.maps.entities.Usuario;

/**
 * FXML Controller class
 *
 * @author Josias Junior Santos  <josiajrsantos@gmail.com>
 */
public class Controller {

    private final Usuario usuario;

    public Controller() {
        usuario = new Usuario();
    }

    @FXML
    private TextField usuarioTextField;
    @FXML
    private PasswordField senhaPasswordField;
    @FXML
    private Button loginButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button cancelButton;

    public void loginButtonOnAction(ActionEvent e) {
        if (usuarioTextField.getText().isBlank() == false && senhaPasswordField.getText().isBlank() == false) {
            loginMessageLabel.setText("Usuário ou Senha invalidos.");

            validateLogin();

        } else {
            loginMessageLabel.setText("Por favor insira nome de usuário e senha.");
        }
    }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        String usuarioInput = usuarioTextField.getText();
        String senhaInput = senhaPasswordField.getText();

        if (usuario.validarCredenciais(usuarioInput, senhaInput)) {
            loginMessageLabel.setText("Login bem-sucedido!");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            iniciarTelaPrincipal();
        } else {
            usuario.diminuirTentativa();
            int tentativasRestantes = usuario.getTentativasRestantes();
            if (tentativasRestantes > 0) {
                loginMessageLabel.setText("Usuário ou senha incorretos. Tentativas restantes: " + tentativasRestantes);
            } else {
                loginMessageLabel.setText("Número de tentativas excedido. O programa será fechado.");
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
                System.exit(0);
            }
        }
    }

    private void iniciarTelaPrincipal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = fxmlLoader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Tela Principal");
            stage.setScene(new Scene(root,1366,763));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}