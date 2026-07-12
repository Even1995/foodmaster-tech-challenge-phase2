package br.com.app.foodmaster.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "cardapios")
public class Cardapio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String nome;

    @Column
    private String descricao;

    @Column
    private Double preco;

    @Column
    private Boolean apenasNoLocal;

    @Column
    private String caminhoFoto;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restauranteId;

    public Restaurante getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Restaurante restauranteId) {
        this.restauranteId = restauranteId;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Boolean getApenasNoLocal() {
        return apenasNoLocal;
    }

    public void setApenasNoLocal(Boolean apenasNoLocal) {
        this.apenasNoLocal = apenasNoLocal;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
