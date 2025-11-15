package com.example.fpoe_50zo.model.player;

import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.deck.IMazo;
import com.example.fpoe_50zo.model.deck.Mazo;
import com.example.fpoe_50zo.model.exceptions.MazoVacioException;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {

    @Test
    void testJugadorSeCreaCorrectamente() {
        IJugador jugador = new Jugador("Humano", true);
        assertEquals("Humano", jugador.getNombre());
        assertTrue(jugador.esHumano());
        assertEquals(0, jugador.getCartasEnMano());
        assertTrue(jugador.getMano().isEmpty());
    }

    @Test
    void testJugadorMaquinaSeCreaCorrectamente() {
        IJugador maquina = new JugadorMaquina("CPU 1");
        assertEquals("CPU 1", maquina.getNombre());
        assertFalse(maquina.esHumano());
        assertEquals(0, maquina.getCartasEnMano());
    }

    @Test
    void testJugadorTomaCartas() throws MazoVacioException {
        IMazo mazo = new Mazo();
        IJugador jugador = new Jugador("Test", true);

        jugador.tomarCarta(mazo);
        assertEquals(1, jugador.getCartasEnMano());

        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);
        assertEquals(3, jugador.getCartasEnMano());
        assertEquals(49, mazo.cartasRestantes()); // 52 - 3
    }

    @Test
    void testJugadorDejaLaMano() throws MazoVacioException {
        IMazo mazo = new Mazo();
        IJugador jugador = new Jugador("Test", true);
        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        assertEquals(2, jugador.getCartasEnMano());

        List<Carta> manoDevuelta = jugador.dejarMano();

        assertEquals(2, manoDevuelta.size());
        assertEquals(0, jugador.getCartasEnMano());
        assertTrue(jugador.getMano().isEmpty());
    }
}