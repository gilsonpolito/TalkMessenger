package sdm.ifspsaocarlos.edu.br.talkmessenger.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contato implements Serializable{
    private String id;
    @SerializedName("nome_completo")
    private String nomeCompleto;
    private String apelido;
    transient private String principal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
