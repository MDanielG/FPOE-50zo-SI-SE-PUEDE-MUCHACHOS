package com.example.fpoe_50zo.model.deck;

import com.example.fpoe_50zo.model.exceptions.MazoVacioException;
import com.example.fpoe_50zo.model.card.Carta;

import java.util.List;

/**
 * Interface that defines the contract for a deck of cards (Mazo).
 * It specifies the operations that can be performed on a deck,
 * abstracting the implementation details.
 */
public interface IMazo {

    /**
     * Randomly shuffles the cards currently in the deck.
     */
    void barajar();

    /**
     * Draws one card from the top of the deck.
     *
     * @return the top card from the deck.
     * @throws MazoVacioException if the deck is empty when trying to draw.
     */
    Carta tomarCarta() throws MazoVacioException;

    /**
     * Checks if the deck has any cards left.
     *
     * @return true if the deck is empty, false otherwise.
     */
    boolean estaVacio();

    /**
     * Gets the number of cards remaining in the deck.
     *
     * @return the count of remaining cards.
     */
    int cartasRestantes();

    /**
     * Adds a single card to the bottom of the deck.
     *
     * @param carta The card to be added (must not be null).
     */
    void agregarAlFinal(Carta carta);

    /**
     * Adds a list of cards to the bottom of the deck.
     *
     * @param cartasMesa The list of cards to add to the bottom of the deck.
     */
    void agregarAlFinal(List<Carta> cartasMesa);
}
