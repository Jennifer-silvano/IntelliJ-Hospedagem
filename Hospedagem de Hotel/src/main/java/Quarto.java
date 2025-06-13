import java.util.*;

public class Quarto {
    private int id;
    private int numero;
    private boolean disponibilidade;
    private boolean acessibilidade;
    private Date dataCheckIn;
    private Date dataCheckOut;
    private String tipo;
    private int capacidade;
    private String status;
    private boolean vistaMar;
    private boolean permiteAnimais;
    private boolean temWifi;

    public Quarto(int id, int numero, String tipo, int capacidade) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.disponibilidade = true;
        this.status = "Disponivel";
        this.acessibilidade = false;
        this.vistaMar = false;
        this.permiteAnimais = false;
        this.temWifi = true; // WiFi por padrão
    }

    /**
     * Método com complexidade ciclomática alta (> 10)
     * Processa checkout com validações múltiplas
     * Complexidade Ciclomática: 12
     */
    public String processarCheckout(String tipoCheckout, boolean temDanos, double valorDanos,
                                    boolean limpezaCompleta, boolean itensEsquecidos,
                                    boolean avaliacaoCliente, int notaAvaliacao,
                                    boolean cobrancaAdicional, double valorAdicional,
                                    String metodoPagamento) {

        StringBuilder resultado = new StringBuilder("Checkout processado: ");

        // 1ª decisão: Verificar se o quarto está ocupado
        if (this.status.equals("Disponivel")) {
            return "Erro: Quarto não está ocupado";
        }

        // 2ª decisão: Verificar tipo de checkout
        if (tipoCheckout.equals("Normal")) {
            resultado.append("Checkout padrão realizado. ");
        } else if (tipoCheckout.equals("Expresso")) { // 3ª decisão
            resultado.append("Checkout expresso realizado. ");
            // Checkout expresso tem menos validações
        } else if (tipoCheckout.equals("Tardio")) { // 4ª decisão
            resultado.append("Checkout tardio - taxa adicional aplicada. ");
            cobrancaAdicional = true;
            valorAdicional += 50.0;
        }

        // 5ª decisão: Verificar danos no quarto
        if (temDanos) {
            resultado.append("Danos identificados - valor: R$").append(valorDanos).append(". ");
            cobrancaAdicional = true;
            valorAdicional += valorDanos;
        }

        // 6ª decisão: Verificar limpeza
        if (!limpezaCompleta) {
            resultado.append("Limpeza adicional necessária - taxa aplicada. ");
            cobrancaAdicional = true;
            valorAdicional += 30.0;
        }

        // 7ª decisão: Verificar itens esquecidos
        if (itensEsquecidos) {
            resultado.append("Itens esquecidos encontrados - contato necessário. ");
            this.status = "Aguardando";
        } else {
            this.status = "Limpeza";
        }

        // 8ª decisão: Processar avaliação do cliente
        if (avaliacaoCliente) {
            if (notaAvaliacao >= 4) { // 9ª decisão
                resultado.append("Avaliação positiva recebida. ");
            } else if (notaAvaliacao >= 2) { // 10ª decisão
                resultado.append("Avaliação neutra recebida. ");
            } else {
                resultado.append("Avaliação negativa - revisão necessária. ");
            }
        }

        // 11ª decisão: Processar cobrança adicional
        if (cobrancaAdicional && valorAdicional > 0) {
            if (metodoPagamento.equals("Cartao")) { // 12ª decisão
                resultado.append("Cobrança adicional de R$").append(valorAdicional)
                        .append(" processada no cartão. ");
            } else {
                resultado.append("Cobrança adicional de R$").append(valorAdicional)
                        .append(" pendente - pagamento em dinheiro. ");
            }
        }

        // Liberar quarto se tudo estiver ok
        if (!itensEsquecidos) {
            this.disponibilidade = true;
            this.dataCheckOut = new Date();
        }

        return resultado.toString();
    }

    /**
     * Método com complexidade ciclomática alta (> 10)
     * Calcula taxa de ocupação e define preço dinâmico
     * Complexidade Ciclomática: 14
     */
    public double calcularPrecoQuarto(String temporada, int diasReserva, String tipoCliente,
                                      boolean fimDeSemana, boolean feriado, boolean eventoLocal,
                                      int ocupacaoHotel, boolean reservaAntecipada,
                                      boolean clienteVIP, int idadeCliente) {

        double precoBase = obterPrecoBasePorTipo();
        double multiplicador = 1.0;

        // 1ª decisão: Verificar temporada
        if (temporada.equals("Alta")) {
            multiplicador *= 1.8;
        } else if (temporada.equals("Media")) { // 2ª decisão
            multiplicador *= 1.3;
        } else if (temporada.equals("Baixa")) { // 3ª decisão
            multiplicador *= 0.8;
        }

        // 4ª decisão: Verificar fim de semana
        if (fimDeSemana) {
            multiplicador *= 1.2;
        }

        // 5ª decisão: Verificar feriado
        if (feriado) {
            multiplicador *= 1.5;
        }

        // 6ª decisão: Verificar evento local
        if (eventoLocal) {
            multiplicador *= 1.4;
        }

        // 7ª decisão: Verificar ocupação do hotel
        if (ocupacaoHotel > 90) {
            multiplicador *= 1.3; // Alta demanda
        } else if (ocupacaoHotel > 70) { // 8ª decisão
            multiplicador *= 1.1; // Demanda moderada
        } else if (ocupacaoHotel < 30) { // 9ª decisão
            multiplicador *= 0.9; // Baixa demanda - desconto
        }

        // 10ª decisão: Verificar reserva antecipada
        if (reservaAntecipada && diasReserva > 30) {
            multiplicador *= 0.85; // Desconto reserva antecipada
        }

        // 11ª decisão: Verificar tipo de cliente
        if (clienteVIP) {
            multiplicador *= 0.9; // Desconto VIP
        } else if (tipoCliente.equals("Corporativo")) { // 12ª decisão
            multiplicador *= 0.95; // Desconto corporativo
        } else if (tipoCliente.equals("Estudante")) { // 13ª decisão
            multiplicador *= 0.8; // Desconto estudante
        }

        // 14ª decisão: Verificar desconto por idade (terceira idade)
        if (idadeCliente >= 65) {
            multiplicador *= 0.9; // Desconto terceira idade
        }

        return precoBase * multiplicador;
    }

    // Métodos auxiliares
    private double obterPrecoBasePorTipo() {
        switch (this.tipo) {
            case "Standard": return 100.0;
            case "Deluxe": return 150.0;
            case "Suite": return 200.0;
            case "Presidencial": return 350.0;
            default: return 80.0;
        }
    }

    // Getters e Setters
    public int getId() { return id; }
    public int getNumero() { return numero; }
    public boolean isDisponivel() { return disponibilidade; }
    public boolean temAcessibilidade() { return acessibilidade; }
    public Date getDataCheckIn() { return dataCheckIn; }
    public Date getDataCheckOut() { return dataCheckOut; }
    public String getTipo() { return tipo; }
    public int getCapacidade() { return capacidade; }
    public String getStatus() { return status; }
    public boolean temVistaMar() { return vistaMar; }
    public boolean permiteAnimais() { return permiteAnimais; }
    public boolean temWifi() { return temWifi; }

    public void setDisponibilidade(boolean disponibilidade) { this.disponibilidade = disponibilidade; }
    public void setStatus(String status) { this.status = status; }
    public void setDataCheckIn(Date dataCheckIn) { this.dataCheckIn = dataCheckIn; }
    public void setDataCheckOut(Date dataCheckOut) { this.dataCheckOut = dataCheckOut; }
    public void setAcessibilidade(boolean acessibilidade) { this.acessibilidade = acessibilidade; }
    public void setVistaMar(boolean vistaMar) { this.vistaMar = vistaMar; }
    public void setPermiteAnimais(boolean permiteAnimais) { this.permiteAnimais = permiteAnimais; }
    public void setTemWifi(boolean temWifi) { this.temWifi = temWifi; }
}