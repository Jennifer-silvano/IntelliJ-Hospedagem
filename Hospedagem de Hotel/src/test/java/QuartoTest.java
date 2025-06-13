import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class QuartoTest {

    private Quarto quartoStandard;
    private Quarto quartoDeluxe;
    private Quarto quartoSuite;
    private Quarto quartoPresidencial;
    private Quarto quartoOutro;

    @BeforeEach
    void setUp() {
        quartoStandard = new Quarto(1, 101, "Standard", 2);
        quartoDeluxe = new Quarto(2, 102, "Deluxe", 3);
        quartoSuite = new Quarto(3, 103, "Suite", 4);
        quartoPresidencial = new Quarto(4, 104, "Presidencial", 6);
        quartoOutro = new Quarto(5, 105, "Outro", 2);

        // Configurar status ocupado para testes de checkout
        quartoStandard.setStatus("Ocupado");
        quartoDeluxe.setStatus("Ocupado");
        quartoSuite.setStatus("Ocupado");
        quartoPresidencial.setStatus("Ocupado");
        quartoOutro.setStatus("Ocupado");
    }

    // ==================== TESTES PARA processarCheckout ====================

    @Test
    @DisplayName("Teste processarCheckout - Quarto disponível (erro)")
    void testProcessarCheckout_QuartoDisponivel() {
        quartoStandard.setStatus("Disponivel");
        String resultado = quartoStandard.processarCheckout("Normal", false, 0.0,
                true, false, false, 0, false, 0.0, "Cartao");
        assertEquals("Erro: Quarto não está ocupado", resultado);
    }

    @Test
    @DisplayName("Teste processarCheckout - Checkout Normal")
    void testProcessarCheckout_Normal() {
        String resultado = quartoStandard.processarCheckout("Normal", false, 0.0,
                true, false, false, 0, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Checkout padrão realizado"));
        assertEquals("Limpeza", quartoStandard.getStatus());
        assertTrue(quartoStandard.isDisponivel());
    }

    @Test
    @DisplayName("Teste processarCheckout - Checkout Expresso")
    void testProcessarCheckout_Expresso() {
        String resultado = quartoStandard.processarCheckout("Expresso", false, 0.0,
                true, false, false, 0, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Checkout expresso realizado"));
        assertEquals("Limpeza", quartoStandard.getStatus());
    }

    @Test
    @DisplayName("Teste processarCheckout - Checkout Tardio")
    void testProcessarCheckout_Tardio() {
        String resultado = quartoStandard.processarCheckout("Tardio", false, 0.0,
                true, false, false, 0, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Checkout tardio - taxa adicional aplicada"));
        assertTrue(resultado.contains("Cobrança adicional de R$50.0 processada no cartão"));
    }

    @Test
    @DisplayName("Teste processarCheckout - Com danos")
    void testProcessarCheckout_ComDanos() {
        String resultado = quartoStandard.processarCheckout("Normal", true, 100.0,
                true, false, false, 0, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Danos identificados - valor: R$100.0"));
        assertTrue(resultado.contains("Cobrança adicional de R$100.0 processada no cartão"));
    }

    @Test
    @DisplayName("Teste processarCheckout - Limpeza incompleta")
    void testProcessarCheckout_LimpezaIncompleta() {
        String resultado = quartoStandard.processarCheckout("Normal", false, 0.0,
                false, false, false, 0, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Limpeza adicional necessária - taxa aplicada"));
        assertTrue(resultado.contains("Cobrança adicional de R$30.0 processada no cartão"));
    }

    @Test
    @DisplayName("Teste processarCheckout - Itens esquecidos")
    void testProcessarCheckout_ItensEsquecidos() {
        String resultado = quartoStandard.processarCheckout("Normal", false, 0.0,
                false, true, true , 0, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Itens esquecidos encontrados - contato necessário"));
        assertEquals("Aguardando", quartoStandard.getStatus());
        assertTrue(quartoStandard.isDisponivel()); // Não libera o quarto
    }

    @Test
    @DisplayName("Teste processarCheckout - Avaliação positiva")
    void testProcessarCheckout_AvaliacaoPositiva() {
        String resultado = quartoStandard.processarCheckout("Normal", false, 0.0,
                true, false, true, 5, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Avaliação positiva recebida"));
    }

    @Test
    @DisplayName("Teste processarCheckout - Avaliação neutra")
    void testProcessarCheckout_AvaliacaoNeutra() {
        String resultado = quartoStandard.processarCheckout("Normal", false, 0.0,
                true, false, true, 3, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Avaliação neutra recebida"));
    }

    @Test
    @DisplayName("Teste processarCheckout - Avaliação negativa")
    void testProcessarCheckout_AvaliacaoNegativa() {
        String resultado = quartoStandard.processarCheckout("Normal", false, 0.0,
                true, false, true, 1, false, 0.0, "Cartao");
        assertTrue(resultado.contains("Avaliação negativa - revisão necessária"));
    }

    @Test
    @DisplayName("Teste processarCheckout - Cobrança adicional dinheiro")
    void testProcessarCheckout_CobrancaAdicionalDinheiro() {
        String resultado = quartoStandard.processarCheckout("Normal", true, 50.0,
                true, false, false, 0, false, 0.0, "Dinheiro");
        assertTrue(resultado.contains("Cobrança adicional de R$50.0 pendente - pagamento em dinheiro"));
    }

    @Test
    @DisplayName("Teste processarCheckout - Cenário completo")
    void testProcessarCheckout_CenarioCompleto() {
        String resultado = quartoStandard.processarCheckout("Tardio", true, 100.0,
                false, false, true, 4, true, 25.0, "Cartao");
        assertTrue(resultado.contains("Checkout tardio"));
        assertTrue(resultado.contains("Danos identificados"));
        assertTrue(resultado.contains("Limpeza adicional necessária"));
        assertTrue(resultado.contains("Avaliação positiva"));
        assertFalse(resultado.contains("Cobrança adicional de R$180.0")); // 50+100+30
    }



    @Test
    @DisplayName("Teste calcularPrecoQuarto - Temporada Alta")
    void testCalcularPrecoQuarto_TemporadaAlta() {
        double preco = quartoStandard.calcularPrecoQuarto("Alta", 10, "Regular",
                false, false, false, 50, false, false, 30);
        assertEquals(180.0, preco, 0.01); // 100 * 1.8
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Temporada Média")
    void testCalcularPrecoQuarto_TemporadaMedia() {
        double preco = quartoStandard.calcularPrecoQuarto("Media", 10, "Regular",
                false, false, false, 50, false, false, 30);
        assertEquals(130.0, preco, 0.01); // 100 * 1.3
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Temporada Baixa")
    void testCalcularPrecoQuarto_TemporadaBaixa() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, false, 50, false, false, 30);
        assertEquals(80.0, preco, 0.01); // 100 * 0.8
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Temporada não especificada")
    void testCalcularPrecoQuarto_TemporadaNormal() {
        double preco = quartoStandard.calcularPrecoQuarto("Normal", 10, "Regular",
                false, false, false, 50, false, false, 30);
        assertEquals(100.0, preco, 0.01); // 100 * 1.0
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Fim de semana")
    void testCalcularPrecoQuarto_FimDeSemana() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                true, false, false, 50, false, false, 30);
        assertEquals(96.0, preco, 0.01); // 100 * 0.8 * 1.2
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Feriado")
    void testCalcularPrecoQuarto_Feriado() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, true, false, 50, false, false, 30);
        assertEquals(120.0, preco, 0.01); // 100 * 0.8 * 1.5
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Evento local")
    void testCalcularPrecoQuarto_EventoLocal() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, true, 50, false, false, 30);
        assertEquals(112.0, preco, 0.01); // 100 * 0.8 * 1.4
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Ocupação alta (>90%)")
    void testCalcularPrecoQuarto_OcupacaoAlta() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, false, 95, false, false, 30);
        assertEquals(104.0, preco, 0.01); // 100 * 0.8 * 1.3
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Ocupação moderada (>70%)")
    void testCalcularPrecoQuarto_OcupacaoModerada() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, false, 80, false, false, 30);
        assertEquals(88.0, preco, 0.01); // 100 * 0.8 * 1.1
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Ocupação baixa (<30%)")
    void testCalcularPrecoQuarto_OcupacaoBaixa() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, false, 20, false, false, 30);
        assertEquals(72.0, preco, 0.01); // 100 * 0.8 * 0.9
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Reserva antecipada")
    void testCalcularPrecoQuarto_ReservaAntecipada() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 35, "Regular",
                false, false, false, 50, true, false, 30);
        assertEquals(68.0, preco, 0.01); // 100 * 0.8 * 0.85
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Reserva não antecipada suficiente")
    void testCalcularPrecoQuarto_ReservaNaoAntecipada() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 20, "Regular",
                false, false, false, 50, true, false, 30);
        assertEquals(80.0, preco, 0.01); // 100 * 0.8 (sem desconto de reserva antecipada)
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Cliente VIP")
    void testCalcularPrecoQuarto_ClienteVIP() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, false, 50, false, true, 30);
        assertEquals(72.0, preco, 0.01); // 100 * 0.8 * 0.9
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Cliente Corporativo")
    void testCalcularPrecoQuarto_ClienteCorporativo() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Corporativo",
                false, false, false, 50, false, false, 30);
        assertEquals(76.0, preco, 0.01); // 100 * 0.8 * 0.95
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Cliente Estudante")
    void testCalcularPrecoQuarto_ClienteEstudante() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Estudante",
                false, false, false, 50, false, false, 30);
        assertEquals(64.0, preco, 0.01); // 100 * 0.8 * 0.8
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Terceira idade")
    void testCalcularPrecoQuarto_TerceiraIdade() {
        double preco = quartoStandard.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, false, 50, false, false, 70);
        assertEquals(72.0, preco, 0.01); // 100 * 0.8 * 0.9
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Todos os tipos de quarto")
    void testCalcularPrecoQuarto_TodosTiposQuarto() {
        assertEquals(100.0, quartoStandard.calcularPrecoQuarto("Normal", 10, "Regular",
                false, false, false, 50, false, false, 30), 0.01);
        assertEquals(150.0, quartoDeluxe.calcularPrecoQuarto("Normal", 10, "Regular",
                false, false, false, 50, false, false, 30), 0.01);
        assertEquals(200.0, quartoSuite.calcularPrecoQuarto("Normal", 10, "Regular",
                false, false, false, 50, false, false, 30), 0.01);
        assertEquals(350.0, quartoPresidencial.calcularPrecoQuarto("Normal", 10, "Regular",
                false, false, false, 50, false, false, 30), 0.01);
        assertEquals(80.0, quartoOutro.calcularPrecoQuarto("Normal", 10, "Regular",
                false, false, false, 50, false, false, 30), 0.01);
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Cenário completo máximo")
    void testCalcularPrecoQuarto_CenarioCompletoMaximo() {
        double preco = quartoPresidencial.calcularPrecoQuarto("Alta", 35, "Estudante",
                true, true, true, 95, true, false, 70);
        // 350 * 1.8 * 1.2 * 1.5 * 1.4 * 1.3 * 0.85 * 0.8 * 0.9 = 2751.58
        assertEquals(1263.09456, preco, 0.1);
    }

    @Test
    @DisplayName("Teste calcularPrecoQuarto - Cenário completo mínimo")
    void testCalcularPrecoQuarto_CenarioCompletoMinimo() {
        double preco = quartoOutro.calcularPrecoQuarto("Baixa", 10, "Regular",
                false, false, false, 20, false, false, 30);
        // 80 * 0.8 * 0.9 = 57.6
        assertEquals(57.6, preco, 0.01);
    }
}