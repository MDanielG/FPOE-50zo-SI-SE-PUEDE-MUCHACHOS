package com.example.fpoe_50zo.model.player;

import com.example.fpoe_50zo.model.player.IJugador;
import com.example.fpoe_50zo.model.deck.IMazo;

import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.deck.Mazo;
import com.example.fpoe_50zo.model.exceptions.MazoVacioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a base player in the "Cincuentazo" game.
 * This class holds the player's hand and basic info.
 * It's intended to be extended by specific player types (like JugadorMaquina).
 */
public class Jugador implements IJugador {

    // Player's name (e.g., "Player 1" or "CPU 1")
    protected final String nombre;

    // The player's current hand of cards.
    // Protected so subclasses (JugadorMaquina) can access it directly.
    protected final List<Carta> mano;

    // Flag to distinguish between human and AI
    private final boolean esHumano;

    /**
     * Constructs a new Player.
     *
     * @param nombre The display name for the player.
     * @param esHumano true if this is a human-controlled player, false otherwise.
     */
    public Jugador(String nombre, boolean esHumano) {
        this.nombre = Objects.requireNonNull(nombre, "Player name cannot be null");
        this.esHumano = esHumano;
        // The game rule states a hand of 4 cards
        this.mano = new ArrayList<>(4);
    }

    /**
     * Draws a card from the deck and adds it to the player's hand.
     *
     * @param mazo The deck to draw from.
     * @throws MazoVacioException if the deck is empty.
     */
    @Override
    public void tomarCarta(IMazo mazo) throws MazoVacioException {
        // The rule is to always have 4 cards, but this method just
        // handles the action of drawing one. The game logic will enforce the count.
        Carta carta = mazo.tomarCarta();
        this.mano.add(carta);
    }

    /**
     * Removes a specific card from the player's hand.
     * This is called when a player successfully plays a card.
     *
     * @param carta The card to be played (and removed).
     * @return true if the card was in the hand and removed, false otherwise.
     */
    public boolean jugarCarta(Carta carta) {
        return this.mano.remove(carta);
    }

    /**
     * Checks if the player has at least one valid move.
     * This is crucial for the "elimination" rule.
     *
     * @param sumaMesa The current sum on the table.
     * @return true if the player has at least one card that can be played
     * without exceeding 50, false otherwise.
     */
    public boolean puedeJugar(int sumaMesa) {
        // Check every card in the hand for a valid move
        for (Carta carta : this.mano) {

            // Check Ace (A)
            if (carta.esAs()) {
                // An Ace can be 1 or 10. If either is valid, the player can play.
                if (sumaMesa + 1 <= 50 || sumaMesa + 10 <= 50) {
                    return true;
                }
            }
            // Check all other cards
            else {
                if (sumaMesa + carta.getValorJuego() <= 50) {
                    return true;
                }
            }
        }

        // If the loop finished and no valid move was found
        return false;
    }

    /**
     * Returns the player's entire hand.
     * Used when a player is eliminated, to return their cards to the deck.
     *
     * @return A List containing all cards from the player's hand.
     */
    public List<Carta> dejarMano() {
        // Create a new list with the hand's contents
        List<Carta> manoParaDevolver = new ArrayList<>(this.mano);
        // Clear the player's hand
        this.mano.clear();
        return manoParaDevolver;
    }

    // --- Standard Getters ---

    /**
     * @return The player's hand (as an unmodifiable list to prevent external changes).
     */
    public List<Carta> getMano() {
        return Collections.unmodifiableList(this.mano);
    }

    /**
     * @return The player's name.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return true if the player is human, false if AI.
     */
    public boolean esHumano() {
        return esHumano;
    }

    /**
     * @return The current number of cards in the player's hand.
     */
    public int getCartasEnMano() {
        return this.mano.size();
    }

    @Override
    public String toString() {
        return nombre + " (Mano: " + mano.size() + " cartas)";
    }
}