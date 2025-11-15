package com.example.fpoe_50zo.model.deck;

import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.card.Palo;
import com.example.fpoe_50zo.model.card.Valor;
import com.example.fpoe_50zo.model.exceptions.MazoVacioException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the Deck class.
 * Does not require JavaFX @BeforeAll because our model is pure logic.
 */
class MazoTest {

    @Test
    void testMazoSeCreaCorrectamente() {
        IMazo mazo = new Mazo();
        assertNotNull(mazo);
        assertFalse(mazo.estaVacio());
        assertEquals(52, mazo.cartasRestantes());
    }

    @Test
    void testMazoTomaTodasLasCartasLanzaExcepcion() {
        IMazo mazo = new Mazo();
        int contador = 0;

        // Loop to take the 52 cards
        try {
            while (!mazo.estaVacio()) {
                mazo.tomarCarta();
                contador++;
            }
        } catch (MazoVacioException e) {
            fail("Se lanzó MazoVacioException antes de tiempo.");
        }

        // Verifications
        assertEquals(52, contador);
        assertTrue(mazo.estaVacio());

        // Check that it now throws the exception
        assertThrows(MazoVacioException.class, mazo::tomarCarta);
    }

    @Test
    void testMazoReciclaCartasDeLaMesa() throws MazoVacioException {
        IMazo mazo = new Mazo();

        // Empty the deck
        while (!mazo.estaVacio()) {
            mazo.tomarCarta();
        }
        assertTrue(mazo.estaVacio());

        // Create a list of tabletop cards (simulated)
        List<Carta> cartasMesa = new ArrayList<>();
        cartasMesa.add(new Carta(Palo.PICAS, Valor.AS));
        cartasMesa.add(new Carta(Palo.CORAZONES, Valor.DOS));

        // Add the table list to the deck
        mazo.agregarAlFinal(cartasMesa);

        // The deck must now have 2 cards
        assertFalse(mazo.estaVacio());
        assertEquals(2, mazo.cartasRestantes());

        // Test that we can take those 2 cards
        assertNotNull(mazo.tomarCarta());
        assertNotNull(mazo.tomarCarta());
        assertTrue(mazo.estaVacio());
    }
}