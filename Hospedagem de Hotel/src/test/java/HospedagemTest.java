import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HospedagemTest {

    private Hospedagem hospedagem;
    private Quarto quartoStandard;
    private Quarto quartoDeluxe;
    private Quarto quartoSuite;
    private Quarto quartoEconomico;
    private Date dataInicio;
    private Date dataFim;
    private Date dataPassado;

    @BeforeEach
    void setUp() {
        hospedagem = new Hospedagem(1, "Hotel Test", "123456789", "test@hotel.com");

        // Criar quartos de diferentes tipos
        quartoStandard = new Quarto(1, 101, "Standard", 2);
        quartoDeluxe = new Quarto(2, 102, "Deluxe", 3);
        quartoSuite = new Quarto(3, 103, "Suite", 4);
        quartoEconomico = new Quarto(4, 104, "Economico", 2);

        // Configurar propriedades dos quartos
        quartoStandard.setAcessibilidade(true);
        quartoStandard.setVistaMar(true);
        quartoStandard.setPermiteAnimais(true);
        quartoStandard.setTemWifi(true);

        quartoDeluxe.setAcessibilidade(false);
        quartoDeluxe.setVistaMar(true);
        quartoDeluxe.setPermiteAnimais(false);
        quartoDeluxe.setTemWifi(true);

        quartoSuite.setAcessibilidade(true);
        quartoSuite.setVistaMar(false);
        quartoSuite.setPermiteAnimais(true);
        quartoSuite.setTemWifi(false);

        // Adicionar quartos à hospedagem
        hospedagem.adicionarQuarto(quartoStandard);
        hospedagem.adicionarQuarto(quartoDeluxe);
        hospedagem.adicionarQuarto(quartoSuite);
        hospedagem.adicionarQuarto(quartoEconomico);

        // Configurar datas
        Calendar cal = Calendar.getInstance();
        dataInicio = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 3);
        dataFim = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, -10);
        dataPassado = cal.getTime();
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Quarto null")
    void testCalcularPrecoTotalEstadia_QuartoNull() {
        double preco = hospedagem.calcularPrecoTotalEstadia(null, dataInicio, dataFim,
                false, "Regular", 2, false, false, "Baixa");
        assertEquals(0.0, preco);
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Quarto Standard, temporada baixa")
    void testCalcularPrecoTotalEstadia_StandardBaixa() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoStandard, dataInicio, dataFim,
                false, "Regular", 2, false, false, "Baixa");
        assertEquals(300.0, preco, 0.01); // 100 * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Quarto Deluxe, temporada alta")
    void testCalcularPrecoTotalEstadia_DeluxeAlta() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoDeluxe, dataInicio, dataFim,
                false, "Regular", 2, false, false, "Alta");
        assertEquals(675.0, preco, 0.01); // 150 * 1.5 * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Suite, temporada média")
    void testCalcularPrecoTotalEstadia_SuiteMedia() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoSuite, dataInicio, dataFim,
                false, "Regular", 2, false, false, "Media");
        assertEquals(720.0, preco, 0.01); // 200 * 1.2 * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Quarto Econômico")
    void testCalcularPrecoTotalEstadia_Economico() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoEconomico, dataInicio, dataFim,
                false, "Regular", 2, false, false, "Baixa");
        assertEquals(240.0, preco, 0.01); // 80 * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Com desconto VIP")
    void testCalcularPrecoTotalEstadia_DescontoVIP() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoStandard, dataInicio, dataFim,
                true, "VIP", 2, false, false, "Baixa");
        assertEquals(240.0, preco, 0.01); // 100 * 0.8 * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Com desconto Fidelidade")
    void testCalcularPrecoTotalEstadia_DescontoFidelidade() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoStandard, dataInicio, dataFim,
                true, "Fidelidade", 2, false, false, "Baixa");
        assertEquals(270.0, preco, 0.01); // 100 * 0.9 * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Capacidade excedida")
    void testCalcularPrecoTotalEstadia_CapacidadeExcedida() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoStandard, dataInicio, dataFim,
                false, "Regular", 5, false, false, "Baixa");
        assertEquals(-1.0, preco);
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Hóspedes extras")
    void testCalcularPrecoTotalEstadia_HospedesExtras() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoDeluxe, dataInicio, dataFim,
                false, "Regular", 3, false, false, "Baixa");
        assertEquals(540.0, preco, 0.01); // (150 + 30) * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Com café da manhã")
    void testCalcularPrecoTotalEstadia_ComCafeDaManha() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoStandard, dataInicio, dataFim,
                false, "Regular", 2, true, false, "Baixa");
        assertEquals(525.0, preco, 0.01); // (100 + 25) * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Com estacionamento")
    void testCalcularPrecoTotalEstadia_ComEstacionamento() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoStandard, dataInicio, dataFim,
                false, "Regular", 2, false, true, "Baixa");
        assertEquals(435.0, preco, 0.01); // (100 + 15) * 3 dias
    }

    @Test
    @DisplayName("Teste calcularPrecoTotalEstadia - Cenário completo")
    void testCalcularPrecoTotalEstadia_CenarioCompleto() {
        double preco = hospedagem.calcularPrecoTotalEstadia(quartoSuite, dataInicio, dataFim,
                true, "VIP", 4, true, true, "Alta");
        assertEquals(1260.0, preco, 0.01); // (200 * 1.5 * 0.8 + 25 + 15) * 3 dias
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Quarto não existe")
    void testValidarDisponibilidadeQuarto_QuartoNaoExiste() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(999, dataInicio, dataFim,
                2, false, false, "Standard", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Quarto indisponível")
    void testValidarDisponibilidadeQuarto_QuartoIndisponivel() {
        quartoStandard.setDisponibilidade(false);
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(1, dataInicio, dataFim,
                2, false, false, "Standard", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Capacidade insuficiente")
    void testValidarDisponibilidadeQuarto_CapacidadeInsuficiente() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(1, dataInicio, dataFim,
                5, false, false, "Standard", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Tipo incorreto")
    void testValidarDisponibilidadeQuarto_TipoIncorreto() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(1, dataInicio, dataFim,
                2, false, false, "Deluxe", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Sem acessibilidade")
    void testValidarDisponibilidadeQuarto_SemAcessibilidade() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(2, dataInicio, dataFim,
                2, true, false, "Deluxe", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Sem vista mar")
    void testValidarDisponibilidadeQuarto_SemVistaMar() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(3, dataInicio, dataFim,
                2, false, true, "Suite", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Não permite pets")
    void testValidarDisponibilidadeQuarto_NaoPermitePets() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(2, dataInicio, dataFim,
                2, false, false, "Deluxe", true, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Sem WiFi")
    void testValidarDisponibilidadeQuarto_SemWifi() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(3, dataInicio, dataFim,
                2, false, false, "Suite", false, true);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Em manutenção")
    void testValidarDisponibilidadeQuarto_EmManutencao() {
        quartoStandard.setStatus("Manutencao");
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(1, dataInicio, dataFim,
                2, false, false, "Standard", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Datas se interceptam")
    void testValidarDisponibilidadeQuarto_DatasSeInterceptam() {
        Calendar cal = Calendar.getInstance();
        quartoStandard.setDataCheckIn(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 5);
        quartoStandard.setDataCheckOut(cal.getTime());

        boolean resultado = hospedagem.validarDisponibilidadeQuarto(1, dataInicio, dataFim,
                2, false, false, "Standard", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Período inválido")
    void testValidarDisponibilidadeQuarto_PeriodoInvalido() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(1, dataFim, dataInicio,
                2, false, false, "Standard", false, false);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Teste validarDisponibilidadeQuarto - Data no passado")
    void testValidarDisponibilidadeQuarto_DataNoPassado() {
        boolean resultado = hospedagem.validarDisponibilidadeQuarto(1, dataPassado, dataFim,
                2, false, false, "Standard", false, false);
        assertFalse(resultado);
    }


}