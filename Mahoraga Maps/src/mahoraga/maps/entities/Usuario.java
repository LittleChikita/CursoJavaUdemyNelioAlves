package mahoraga.maps.entities;

public class Usuario {

    private static final String USUARIO_CORRETO = "IBGE550234";
    private static final String SENHA_CORRETA = "IBGE1234";
    private int tentativasRestantes;
    private static final int MAX_TENTATIVAS = 3;

    public Usuario() {
        this.tentativasRestantes = MAX_TENTATIVAS;
    }

    public boolean validarCredenciais(String usuario, String senha) {
        return USUARIO_CORRETO.equals(usuario) && SENHA_CORRETA.equals(senha);
    }

    public int getTentativasRestantes() {
        return tentativasRestantes;
    }

    public void diminuirTentativa() {
        this.tentativasRestantes--;
    }
}