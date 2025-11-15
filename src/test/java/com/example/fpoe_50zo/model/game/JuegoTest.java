package com.example.fpoe_50zo.model.game;

import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.card.Palo;
import com.example.fpoe_50zo.model.card.Valor;
import com.example.fpoe_50zo.model.exceptions.JugadaInvalidaException;
import com.example.fpoe_50zo.model.player.IJugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JuegoTest {

    @Test
    void testJuegoIniciaCorrectamente() {
        IJuego juego = new Juego();
        juego.iniciarJuego(2);


        assertEquals(3, juego.getJugadores().size());

        IJugador humano = juego.getJugadorActual();
        assertTrue(humano.esHumano());
        assertEquals("Player 1", humano.getNombre());

        for(IJugador jugador : juego.getJugadores()){
            assertEquals(4, jugador.getCartasEnMano());
        }

        assertNotNull(juego.getUltimaCartaJugada());

    }

    @Test
    void testPasaTurnoCorrectamente() {
        IJuego juego = new Juego();
        juego.iniciarJuego(2);

        IJugador j1 = juego.getJugadorActual();
        juego.siguienteTurno();
        IJugador j2 = juego.getJugadorActual();
        juego.siguienteTurno();
        IJugador j3 = juego.getJugadorActual();

        assertNotEquals(j1, j2);
        assertNotEquals(j2, j3);

        juego.siguienteTurno();
        IJugador j1_vuelta = juego.getJugadorActual();
        assertEquals(j1, j1_vuelta);
    }

    @Test
    void testJugadaInvalidaLanzaExcepcion() {
        IJuego juego = new Juego();
        juego.iniciarJuego(1);

        Carta cartaFalsa = new Carta(Palo.TREBOLES, Valor.AS);

        assertThrows(JugadaInvalidaException.class, () -> {
            juego.jugarCarta(cartaFalsa);
        });

        assertThrows(JugadaInvalidaException.class, () -> {
            juego.jugarCarta(cartaFalsa);
        });
    }

    @Test
    void testJugadorSeEliminaCorrectamente() {
        IJuego juego = new Juego();
        juego.iniciarJuego(1); // 2 jugadores
        assertEquals(2, juego.getJugadores().size());

        assertFalse(juego.isJuegoTerminado());

    }
}