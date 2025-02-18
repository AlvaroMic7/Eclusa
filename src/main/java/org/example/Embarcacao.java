package org.example;

public abstract class Embarcacao {
    protected String codigoIdentificacao;
    protected String capitao;
    protected String portoOrigem;
    protected String portoDestino;
    protected String pais;
    protected String sentido;
    protected boolean pago;


    public Embarcacao(String codigoIdentificacao, String capitao, String portoOrigem, String portoDestino, String pais, String sentido) {
        this.codigoIdentificacao = codigoIdentificacao;
        this.capitao = capitao;
        this.portoOrigem = portoOrigem;
        this.portoDestino = portoDestino;
        this.pais = pais;
        this.sentido = sentido.toLowerCase();
        this.pago = false;
    }

    public abstract double calcularTaxa();

    public String getCodigoIdentificacao() {
        return codigoIdentificacao;
    }

    public void marcarComoPago() {
        this.pago = true;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public String getCapitao() {
        return capitao;
    }

    public String getPortoOrigem() {
        return portoOrigem;
    }

    public String getPortoDestino() {
        return portoDestino;
    }

    public String getPais() {
        return pais;
    }

    public String getSentido() {
        return sentido;
    }

    public abstract double getComprimento();

    public abstract double getLargura();
}
