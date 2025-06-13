import java.util.*;

// Classe Hospedagem
public class Hospedagem {
    private int id;
    private String nome;
    private String telefone;
    private String email;
    private List<Quarto> quartos;

    public Hospedagem(int id, String nome, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.quartos = new ArrayList<>();
    }

    /**
     * Método com complexidade ciclomática alta (> 10)
     * Calcula preço total baseado em critérios do quarto e estadia
     * Complexidade Ciclomática: 13
     */
    public double calcularPrecoTotalEstadia(Quarto quarto, Date dataInicio, Date dataFim,
                                            boolean temDesconto, String tipoCliente,
                                            int numeroHospedes, boolean temCafeDaManha,
                                            boolean temEstacionamento, String temporada) {

        double precoBase = 0.0;
        int diasEstadia = calcularDiasEstadia(dataInicio, dataFim);

        // 1ª decisão: Verificar se o quarto existe
        if (quarto == null) {
            return 0.0;
        }

        // 2ª decisão: Verificar tipo do quarto
        if (quarto.getTipo().equals("Standard")) {
            precoBase = 100.0;
        } else if (quarto.getTipo().equals("Deluxe")) { // 3ª decisão
            precoBase = 150.0;
        } else if (quarto.getTipo().equals("Suite")) { // 4ª decisão
            precoBase = 200.0;
        } else { // 5ª decisão
            precoBase = 80.0; // Econômico
        }

        // 6ª decisão: Verificar temporada
        if (temporada.equals("Alta")) {
            precoBase *= 1.5;
        } else if (temporada.equals("Media")) { // 7ª decisão
            precoBase *= 1.2;
        }

        // 8ª decisão: Aplicar desconto por tipo de cliente
        if (temDesconto && tipoCliente.equals("VIP")) {
            precoBase *= 0.8; // 20% desconto
        } else if (temDesconto && tipoCliente.equals("Fidelidade")) { // 9ª decisão
            precoBase *= 0.9; // 10% desconto
        }

        // 10ª decisão: Taxa adicional por hóspedes extras
        if (numeroHospedes > quarto.getCapacidade()) {
            return -1.0; // Capacidade excedida
        } else if (numeroHospedes > 2) { // 11ª decisão
            precoBase += (numeroHospedes - 2) * 30.0;
        }

        // 12ª decisão: Café da manhã
        if (temCafeDaManha) {
            precoBase += 25.0 * diasEstadia;
        }

        // 13ª decisão: Estacionamento
        if (temEstacionamento) {
            precoBase += 15.0 * diasEstadia;
        }

        return precoBase * diasEstadia;
    }

    /**
     * Método com complexidade ciclomática alta (> 10)
     * Valida disponibilidade de quartos com múltiplas condições
     * Complexidade Ciclomática: 12
     */
    public boolean validarDisponibilidadeQuarto(int quartoId, Date dataInicio, Date dataFim,
                                                int numeroHospedes, boolean precisaAcessibilidade,
                                                boolean precisaVistaMar, String tipoQuarto,
                                                boolean permitePets, boolean precisaWifi) {

        Quarto quarto = buscarQuartoPorId(quartoId);

        // 1ª decisão: Verificar se o quarto existe
        if (quarto == null) {
            return false;
        }

        // 2ª decisão: Verificar se o quarto está disponível
        if (!quarto.isDisponivel()) {
            return false;
        }

        // 3ª decisão: Verificar capacidade do quarto
        if (quarto.getCapacidade() < numeroHospedes) {
            return false;
        }

        // 4ª decisão: Verificar tipo do quarto
        if (!quarto.getTipo().equals(tipoQuarto)) {
            return false;
        }

        // 5ª decisão: Verificar acessibilidade
        if (precisaAcessibilidade && !quarto.temAcessibilidade()) {
            return false;
        }

        // 6ª decisão: Verificar vista para o mar
        if (precisaVistaMar && !quarto.temVistaMar()) {
            return false;
        }

        // 7ª decisão: Verificar se permite pets
        if (permitePets && !quarto.permiteAnimais()) {
            return false;
        }

        // 8ª decisão: Verificar WiFi
        if (precisaWifi && !quarto.temWifi()) {
            return false;
        }

        // 9ª decisão: Verificar se o quarto não está em manutenção
        if (quarto.getStatus().equals("Manutencao")) {
            return false;
        }

        // 10ª decisão: Verificar se já tem checkout agendado no período
        if (quarto.getDataCheckOut() != null) {
            if (datasSeInterceptam(dataInicio, dataFim, quarto.getDataCheckIn(), quarto.getDataCheckOut())) {
                return false;
            }
        }

        // 11ª decisão: Verificar período mínimo de estadia
        int diasEstadia = calcularDiasEstadia(dataInicio, dataFim);
        if (diasEstadia < 1) {
            return false;
        }

        // 12ª decisão: Verificar se data de início não é no passado
        if (dataInicio.before(new Date())) {
            return false;
        }

        return true;
    }

    // Métodos auxiliares
    private int calcularDiasEstadia(Date inicio, Date fim) {
        return (int) ((fim.getTime() - inicio.getTime()) / (1000 * 60 * 60 * 24));
    }

    private Quarto buscarQuartoPorId(int id) {
        return quartos.stream().filter(q -> q.getId() == id).findFirst().orElse(null);
    }

    private boolean datasSeInterceptam(Date inicio1, Date fim1, Date inicio2, Date fim2) {
        return inicio1.before(fim2) && fim1.after(inicio2);
    }

    // Getters e Setters básicos
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public List<Quarto> getQuartos() { return quartos; }

    // Método para adicionar quartos (necessário para os testes)
    public void adicionarQuarto(Quarto quarto) {
        this.quartos.add(quarto);
    }
}