public class Pagamento {
    private int id;
    private String mecolo;
    private String user;
    private String data;
    private String status;

    public Pagamento(int id, String mecolo, String user, String data, String status) {
        this.id = id;
        this.mecolo = mecolo;
        this.user = user;
        this.data = data;
        this.status = status;
    }

    public boolean notusurlPagament() {
        if ("pending".equals(this.status)) {
            this.status = "completed";
            return true;
        }
        return false;
    }

    public boolean canceldirPagament() {
        if ("pending".equals(this.status) || "completed".equals(this.status)) {
            this.status = "canceled";
            return true;
        }
        return false;
    }

    public String getStatus() {
        return status;
    }
}