package mahoraga.maps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mahoraga.maps.entities.CSVUtils;
import mahoraga.maps.entities.Municipio;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AtualizacaoController implements Initializable {

    private MainController mainController;
    private Municipio municipioSelecionado;
    private ObservableList<Municipio> municipios;

    private final String inCSVPath = "C:\\Users\\ANGEL\\OneDrive\\Área de Trabalho\\inCSV";
    private final String outCSVPath = "C:\\Users\\ANGEL\\OneDrive\\Área de Trabalho\\outCSV";

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private Button buttonListMunicipio;

    @FXML
    private Button buttonAtualizar;

    @FXML
    private TextField txtArea;

    @FXML
    private TextField txtCodigoIBGE;

    @FXML
    private TextField txtDomicilios;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtIdh;

    @FXML
    private TextField txtIdhEducacao;

    @FXML
    private TextField txtIdhLongevidade;

    @FXML
    private TextField txtMicrorregiao;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPeaDia;

    @FXML
    private TextField txtPibTotal;

    @FXML
    private TextField txtPopulacao;

    @FXML
    private TextField txtRendaMedia;

    @FXML
    private TextField txtRendaNominal;

    @FXML
    private ComboBox<String> CBMunicipios;

    @FXML
    private void switchToMain(ActionEvent event) throws IOException {
        Parent telaMain = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(telaMain);
        Stage janelaAtual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        janelaAtual.setScene(scene);
        janelaAtual.show();

    }

    @FXML
    private void selecionarMunicipio(ActionEvent event) {
        String selectedMunicipioName = CBMunicipios.getSelectionModel().getSelectedItem();
        municipioSelecionado = municipios.stream().filter(m -> m.getNome().equals(selectedMunicipioName)).findFirst().orElse(null);
        exibirDetalhesMunicipio();
    }

    @FXML
    void salvarAlteracao(ActionEvent event) {
        String populacao = txtPopulacao.getText();
        String domicilios = txtDomicilios.getText();
        String pibTotal = txtPibTotal.getText();
        String idh = txtIdh.getText();
        String rendaMedia = txtRendaMedia.getText();
        String rendaNominal = txtRendaNominal.getText();
        String peaDia = txtPeaDia.getText();
        String idhEducacao = txtIdhEducacao.getText();
        String idhLongevidade = txtIdhLongevidade.getText();

        municipioSelecionado.setPopulacao(populacao);
        municipioSelecionado.setDomicilios(domicilios);
        municipioSelecionado.setPibTotal(pibTotal);
        municipioSelecionado.setIdh(idh);
        municipioSelecionado.setRendaMedia(rendaMedia);
        municipioSelecionado.setRendaNominal(rendaNominal);
        municipioSelecionado.setPeaDia(peaDia);
        municipioSelecionado.setIdhEducacao(idhEducacao);
        municipioSelecionado.setIdhLongevidade(idhLongevidade);

        CSVUtils.escreverCSV(new ArrayList<>(municipios), "C:\\Users\\ANGEL\\OneDrive\\Área de Trabalho\\outCSV\\arquivo_saida.csv");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
        CBMunicipios.getItems().addAll(municipios.stream().map(Municipio::getNome).collect(Collectors.toList()));
    }

    private void loadData() {
        File inDir = new File(inCSVPath);
        File outDir = new File(outCSVPath);

        File[] inFiles = inDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        File[] outFiles = outDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (inFiles == null || inFiles.length == 0) {
            // Se inCSV estiver vazio, carrega os dados de outCSV
            loadFromDirectory(outFiles);
            return;
        }

        if (outFiles != null && outFiles.length > 0) {
            // Verifica o arquivo mais recente em outCSV
            File mostRecentOutFile = Arrays.stream(outFiles)
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()))
                    .orElse(null);

            if (mostRecentOutFile != null) {
                // Verifica se o arquivo mais recente em outCSV é mais novo que todos em inCSV
                long mostRecentOutModified = mostRecentOutFile.lastModified();
                boolean inCsvOlderThanOutCsv = Arrays.stream(inFiles)
                        .allMatch(inFile -> inFile.lastModified() < mostRecentOutModified);

                if (inCsvOlderThanOutCsv) {
                    // Carrega os dados do arquivo mais recente em outCSV
                    loadFromFile(mostRecentOutFile);
                    return;
                }
            }
        }

        // Carrega os dados do primeiro arquivo em inCSV por padrão
        if (inFiles.length > 0) {
            loadFromFile(inFiles[0]);
        }
    }

    private void loadFromDirectory(File[] files) {
        if (files != null && files.length > 0) {
            File mostRecentFile = Arrays.stream(files)
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()))
                    .orElse(null);
            if (mostRecentFile != null) {
                loadFromFile(mostRecentFile);
            }
        }
    }

    private void loadFromFile(File file) {
        List<Municipio> lista = CSVUtils.lerCSV(file.getAbsolutePath());
        municipios = FXCollections.observableArrayList(lista);
    }

    private void exibirDetalhesMunicipio() {
        if (municipioSelecionado != null) {
            txtCodigoIBGE.setText(municipioSelecionado.getCodigoIBGE());
            txtCodigoIBGE.setDisable(true);
            txtNome.setText(municipioSelecionado.getNome());
            txtNome.setDisable(true);
            txtEstado.setText(municipioSelecionado.getEstado());
            txtEstado.setDisable(true);
            txtMicrorregiao.setText(municipioSelecionado.getMicroRegiao());
            txtMicrorregiao.setDisable(true);
            txtArea.setText(municipioSelecionado.getAreaKm());
            txtArea.setDisable(true);
            txtPopulacao.setText(municipioSelecionado.getPopulacao());
            txtDomicilios.setText(municipioSelecionado.getDomicilios());
            txtPibTotal.setText(municipioSelecionado.getPibTotal());
            txtIdh.setText(municipioSelecionado.getIdh());
            txtRendaMedia.setText(municipioSelecionado.getRendaMedia());
            txtRendaNominal.setText(municipioSelecionado.getRendaNominal());
            txtPeaDia.setText(municipioSelecionado.getPeaDia());
            txtIdhEducacao.setText(municipioSelecionado.getIdhEducacao());
            txtIdhLongevidade.setText(municipioSelecionado.getIdhLongevidade());
        }
    }
}
