package com.example.fpoe_50zo.model.player;

import com.example.fpoe_50zo.model.deck.IMazo;
import com.example.fpoe_50zo.model.exceptions.MazoVacioException;
import com.example.fpoe_50zo.model.card.Carta;

import java.util.List;

/**
 * Interface that defines the contract for a Player.
 * It specifies the operations a player can perform or that can be
 * performed on a player.
 */
public interface IJugador {

    /**
     * Draws a card from the deck and adds it to the player's hand.
     *
     * @param mazo The deck (as an IMazo interface) to draw from.
     * @throws MazoVacioException if the deck is empty.
     */
    void tomarCarta(IMazo mazo) throws MazoVacioException;

    /**
     * Removes a specific card from the player's hand.
     *
     * @param carta The card to be played (and removed).
     * @return true if the card was in the hand and removed, false otherwise.
     */
    boolean jugarCarta(Carta carta);

    /**
     * Checks if the player has at least one valid move.
     *
     * @param sumaMesa The current sum on the table.
     * @return true if the player has at least one card that can be played.
     */
    boolean puedeJugar(int sumaMesa);

    /**
     * Returns the player's entire hand and clears it.
     * Used when a player is eliminated.
     *
     * @return A List containing all cards from the player's hand.
     */
    List<Carta> dejarMano();

    /**
     * @return The player's hand (as an unmodifiable list).
     */
    List<Carta> getMano();

    /**
     * @return The player's name.
     */
    String getNombre();

    /**
     * @return true if the player is human, false if AI.
     */
    boolean esHumano();

    /**
     * @return The current number of cards in the player's hand.
     */
    int getCartasEnMano();
}