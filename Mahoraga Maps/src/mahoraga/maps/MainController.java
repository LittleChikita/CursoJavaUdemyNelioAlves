    package mahoraga.maps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mahoraga.maps.entities.CSVUtils;
import mahoraga.maps.entities.Municipio;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    @FXML
    private Button buttonAtualizar;

    @FXML
    private TableView<Municipio> municipiosTableView;

    @FXML
    private TableColumn<Municipio, String> municipioCodigoTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioDomiciliosTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioEstadoTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioClassificacaoIDHETableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioClassificacaoIDHLTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioClassificacaoIDHTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioIDHEducacaoTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioIDHLongevidadeTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioIDHTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioMicrorregiaoTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioNameTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioPEADiaTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioPibTotalTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioPopulacaoTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioRegiaoTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioRendaMediaTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioDensidadeDemograficaTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioPibPerCapitaTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioRendaNominalTableColumn;

    @FXML
    private TableColumn<Municipio, String> municipioAreaTableColumn;

    @FXML
    private TextField municipioBusca;

    private ObservableList<Municipio> municipioObservableList = FXCollections.observableArrayList();

    private final String inCSVPath = "C:\\Users\\ANGEL\\OneDrive\\Área de Trabalho\\inCSV";
    private final String outCSVPath = "C:\\Users\\ANGEL\\OneDrive\\Área de Trabalho\\outCSV";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTableColumns();
        loadData();
        setupSearchFilter();
    }

    private void initTableColumns() {
        municipioCodigoTableColumn.setCellValueFactory(new PropertyValueFactory<>("codigoIBGE"));
        municipioNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        municipioMicrorregiaoTableColumn.setCellValueFactory(new PropertyValueFactory<>("microRegiao"));
        municipioEstadoTableColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        municipioRegiaoTableColumn.setCellValueFactory(new PropertyValueFactory<>("regiaoGeografica"));
        municipioAreaTableColumn.setCellValueFactory(new PropertyValueFactory<>("areaKm"));
        municipioPopulacaoTableColumn.setCellValueFactory(new PropertyValueFactory<>("populacao"));
        municipioDensidadeDemograficaTableColumn.setCellValueFactory(new PropertyValueFactory<>("densidadeDemografica"));
        municipioDomiciliosTableColumn.setCellValueFactory(new PropertyValueFactory<>("domicilios"));
        municipioPibTotalTableColumn.setCellValueFactory(new PropertyValueFactory<>("pibTotal"));
        municipioPibPerCapitaTableColumn.setCellValueFactory(new PropertyValueFactory<>("PibPerCapita"));
        municipioRendaMediaTableColumn.setCellValueFactory(new PropertyValueFactory<>("rendaMedia"));
        municipioRendaNominalTableColumn.setCellValueFactory(new PropertyValueFactory<>("rendaNominal"));
        municipioPEADiaTableColumn.setCellValueFactory(new PropertyValueFactory<>("peaDia"));
        municipioIDHTableColumn.setCellValueFactory(new PropertyValueFactory<>("idh"));
        municipioClassificacaoIDHTableColumn.setCellValueFactory(new PropertyValueFactory<>("classificacaoIDH"));
        municipioIDHEducacaoTableColumn.setCellValueFactory(new PropertyValueFactory<>("idhEducacao"));
        municipioClassificacaoIDHETableColumn.setCellValueFactory(new PropertyValueFactory<>("classificacaoIDHE"));
        municipioIDHLongevidadeTableColumn.setCellValueFactory(new PropertyValueFactory<>("idhLongevidade"));
        municipioClassificacaoIDHLTableColumn.setCellValueFactory(new PropertyValueFactory<>("classificacaoIDHL"));
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
    municipioObservableList.clear();
    municipioObservableList.addAll(lista);
    municipiosTableView.setItems(municipioObservableList);
}

    private void setupSearchFilter() {
        FilteredList<Municipio> filteredData = new FilteredList<>(municipioObservableList, b -> true);

        municipioBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(municipio -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return municipioMatchesFilter(municipio, lowerCaseFilter);
            });
        });

        SortedList<Municipio> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(municipiosTableView.comparatorProperty());
        municipiosTableView.setItems(sortedData);
    }

    private boolean municipioMatchesFilter(Municipio municipio, String filter) {
        return municipio.getCodigoIBGE().toLowerCase().contains(filter)
                || municipio.getNome().toLowerCase().contains(filter)
                || municipio.getMicroRegiao().toLowerCase().contains(filter)
                || municipio.getEstado().toLowerCase().contains(filter)
                || municipio.getRegiaoGeografica().toLowerCase().contains(filter);
        // Adicione as demais condições de filtro conforme necessário
    }

    @FXML
    private void switchToUpdate(ActionEvent event) {
        try {
            Parent telaUpdate = FXMLLoader.load(getClass().getResource("Atualizacao.fxml"));
            Scene scene2 = new Scene(telaUpdate);
            Stage janelaAtual = (Stage) buttonAtualizar.getScene().getWindow();
            janelaAtual.setScene(scene2);
            janelaAtual.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Aqui você pode implementar uma mensagem de erro ao usuário se a troca de cena falhar
        }
    }

}
