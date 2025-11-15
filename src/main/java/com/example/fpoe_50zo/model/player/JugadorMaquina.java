package com.example.fpoe_50zo.model.player;

import com.example.fpoe_50zo.model.card.Carta;

import java.util.List;

/**
 * Represents an AI-controlled (Machine) player.
 * It extends the base Jugador class and adds logic
 * to decide which card to play.
 */
public class JugadorMaquina extends Jugador {

    /**
     * Constructs a new AI player.
     *
     * @param nombre The display name for the AI (e.g., "CPU 1").
     */
    public JugadorMaquina(String nombre) {
        // Call the parent constructor, setting esHumano to false.
        super(nombre, false);
    }

    /**
     * Decides which card to play based on the current game sum.
     * This is the "AI" logic.
     *
     * Strategy:
     * 1. Tries to play an Ace (A) as 10 if possible.
     * 2. Tries to play any other non-Ace card that is valid.
     * 3. As a last resort, plays an Ace (A) as 1 if possible.
     * 4. If no card is playable, returns null.
     *
     * @param sumaActual The current sum on the table.
     * @return The Carta to be played, or null if no valid move exists.
     */
    public Carta decidirMejorJugada(int sumaActual) {
        // We use 'mano' directly, which is 'protected' in the Jugador class.

        Carta asComoUno = null; // To store an Ace-as-1 as a last resort

        // --- First Pass: Look for Aces-as-10 and regular cards ---
        for (Carta carta : this.mano) {

            if (carta.esAs()) {
                // Priority 1: Can we use an Ace as 10?
                if (sumaActual + 10 <= 50) {
                    return carta; // Play this card
                }
                // Last Resort: Can we use it as 1?
                if (asComoUno == null && sumaActual + 1 <= 50) {
                    asComoUno = carta; // Save it as an option
                }
            }
            // Priority 2: Check any other card
            else {
                if (sumaActual + carta.getValorJuego() <= 50) {
                    return carta; // Play this card
                }
            }
        }

        // --- Second Pass (Decision) ---

        // If no regular card or Ace-as-10 was playable,
        // play the Ace-as-1 if we found one.
        if (asComoUno != null) {
            return asComoUno;
        }

        // If no card was playable at all
        return null;
    }
}
