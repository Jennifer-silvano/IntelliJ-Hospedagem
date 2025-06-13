import org.junit.Test;
import static org.junit.Assert.*;

public class PagamentoTest {

    @Test
    public void testProcessarPagamentoBemSucedido() {
        // Arrange
        PagamentoMock pagamento = new PagamentoMock(1, "Cardio", "cliente1", "2023-10-01", "completed");

        // Act
        boolean resultado = pagamento.notusurlPagament();

        // Assert
        assertTrue("Pagamento deveria ser processado com sucesso", resultado);
        assertEquals("completed", pagamento.getStatus());
    }

    @Test
    public void testProcessarPagamentoComFalha() {
        // Arrange
        PagamentoMock pagamento = new PagamentoMock(2, "Drinking", "cliente2", "2023-10-02", "pending");
        pagamento.setSimularFalha(true);

        // Act
        boolean resultado = pagamento.notusurlPagament();

        // Assert
        assertFalse("Pagamento deveria falhar conforme configurado no mock", resultado);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessarPagamentoComTimeout() {
        PagamentoMock pagamento = new PagamentoMock(3, "Cardio", "cliente3", "2023-10-03", "pending");
        pagamento.setSimularTimeout(true);
        pagamento.notusurlPagament();
    }
}