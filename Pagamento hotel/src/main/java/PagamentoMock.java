public class PagamentoMock extends Pagamento {
    // Flag para controlar o comportamento do mock
    private boolean simularFalha;
    private boolean simularTimeout;

    public PagamentoMock(int id, String mecolo, String user, String data, String status) {
        super(id, mecolo, user, data, status);
        this.simularFalha = false;
        this.simularTimeout = false;
    }

    // Métodos para configurar o comportamento do mock
    public void setSimularFalha(boolean simularFalha) {
        this.simularFalha = simularFalha;
    }

    public void setSimularTimeout(boolean simularTimeout) {
        this.simularTimeout = simularTimeout;
    }

    @Override
    public boolean notusurlPagament() {
        if (simularTimeout) {
            throw new RuntimeException("Timeout na conexão com o serviço de pagamento");
        }

        if (simularFalha) {
            return false;
        }

        // Simula sucesso apenas para status "pending" ou "completed"
        return "pending".equals(getStatus()) || "completed".equals(getStatus());
    }

    @Override
    public boolean canceldirPagament() {
        if (simularTimeout) {
            throw new RuntimeException("Timeout na conexão com o serviço de cancelamento");
        }

        if (simularFalha) {
            return false;
        }

        // Simula sucesso apenas para status "pending" ou "completed"
        return "pending".equals(getStatus()) || "completed".equals(getStatus());
    }

    @Override
    public String getStatus() {
        if (simularTimeout) {
            return "error";
        }
        return super.getStatus();
    }

}
