package mahoraga.maps.entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CSVUtils {

    private static final String caminho = "C:\\Users\\ANGEL\\OneDrive\\Área de Trabalho\\in\\arquivoT.csv";

    public static List<Municipio> lerCSV(String caminhoArquivo) {
    List<Municipio> municipios = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo, StandardCharsets.UTF_8))) {
        String linha;
        String[] headers = br.readLine().split(";");
        NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
        while ((linha = br.readLine()) != null) {
            String[] values = linha.split(";");
            Municipio municipio = new Municipio();
            municipio.setCodigoIBGE(values[0]);
            municipio.setNome(values[1]);
            municipio.setMicroRegiao(values[2]);
            municipio.setEstado(values[3]);
            municipio.setRegiaoGeografica(values[4]);
            municipio.setAreaKm(values[5]);
            municipio.setPopulacao(values[6]);
            municipio.setDomicilios(values[7]);
            municipio.setPibTotal(values[8]);
            municipio.setIdh(values[9]);
            municipio.setRendaMedia(values[10]);
            municipio.setRendaNominal(values[11]);
            municipio.setPeaDia(values[12]);
            municipio.setIdhEducacao(values[13]);
            municipio.setIdhLongevidade(values[14]);
            municipios.add(municipio);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return municipios;
}


    public static void escreverCSV(List<Municipio> municipios, String caminho) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho, StandardCharsets.ISO_8859_1))) {
            bw.write("Código IBGE;Municípios;Microrregião;Estado;Região Geográfica;Área km2;População;Domicílios;PIB Total (R$ mil);IDH;Renda Média;Renda Nominal;PEA Dia;IDH Dimensão Educação;IDH Dimensão Longevidade\n");
            for (Municipio m : municipios) {
                bw.write(String.join(";",
                        m.getCodigoIBGE(),
                        m.getNome(),
                        m.getMicroRegiao(),
                        m.getEstado(),
                        m.getRegiaoGeografica(),
                        String.valueOf(m.getAreaKm()),
                        String.valueOf(m.getPopulacao()),
                        String.valueOf(m.getDomicilios()),
                        String.valueOf(m.getPibTotal()),
                        String.valueOf(m.getIdh()),
                        String.valueOf(m.getRendaMedia()),
                        String.valueOf(m.getRendaNominal()),
                        String.valueOf(m.getPeaDia()),
                        String.valueOf(m.getIdhEducacao()),
                        String.valueOf(m.getIdhLongevidade())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}