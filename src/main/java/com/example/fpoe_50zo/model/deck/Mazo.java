package com.example.fpoe_50zo.model.deck;

import com.example.fpoe_50zo.model.deck.IMazo;

import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.card.Palo;
import com.example.fpoe_50zo.model.card.Valor;
import com.example.fpoe_50zo.model.exceptions.MazoVacioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the deck of 52 playing cards.
 * This class manages creating, shuffling, and dealing cards.
 */
public class Mazo implements IMazo {

    // We use a List to hold the cards, which is easy to shuffle.
    private final List<Carta> cartas;

    /**
     * Constructs a new Mazo.
     * It initializes a full 52-card deck and shuffles it.
     */
    public Mazo() {
        this.cartas = new ArrayList<>(52);
        this.crearMazoCompleto();
        this.barajar();
    }

    /**
     * Fills the deck with the standard 52 playing cards.
     * This method is called by the constructor.
     */
    private void crearMazoCompleto() {
        // Clear any existing cards, just in case
        this.cartas.clear();

        // Iterate over all suits (Palos)
        for (Palo palo : Palo.values()) {
            // Iterate over all values (Valor)
            for (Valor valor : Valor.values()) {
                // Add the new card to the deck
                this.cartas.add(new Carta(palo, valor));
            }
        }
    }

    /**
     * Randomly shuffles the cards currently in the deck.
     */
    public void barajar() {
        Collections.shuffle(this.cartas);
    }

    /**
     * Draws one card from the top of the deck.
     * The "top" is considered the end of the list for efficient removal.
     *
     * @return the top card from the deck.
     * @throws MazoVacioException if the deck is empty when trying to draw.
     */
    public Carta tomarCarta() throws MazoVacioException {
        if (estaVacio()) {
            // Throw our custom checked exception
            throw new MazoVacioException("Cannot draw a card, the deck is empty.");
        }
        // Remove and return the last card from the list (O(1) operation)
        return this.cartas.remove(this.cartas.size() - 1);
    }

    /**
     * Checks if the deck has any cards left.
     *
     * @return true if the deck is empty, false otherwise.
     */
    public boolean estaVacio() {
        return this.cartas.isEmpty();
    }

    /**
     * Gets the number of cards remaining in the deck.
     *
     * @return the count of remaining cards.
     */
    public int cartasRestantes() {
        return this.cartas.size();
    }

    /**
     * Adds a single card to the bottom of the deck.
     * Used for the rule: "Las cartas del jugador eliminado deben enviarse al final del mazo".
     *
     * @param carta The card to be added (must not be null).
     */
    public void agregarAlFinal(Carta carta) {
        Objects.requireNonNull(carta, "Cannot add a null card to the deck");
        // Add to the "bottom" of the deck (index 0)
        this.cartas.add(0, carta);
    }

    /**
     * Adds a list of cards to the bottom of the deck.
     * Used for the rule: "Si las cartas del mazo se terminan se deben tomar
     * las cartas de la mesa... y dejarlas disponibles en el mazo."
     *
     * @param cartasMesa The list of cards to add to the bottom of the deck.
     */
    public void agregarAlFinal(List<Carta> cartasMesa) {
        Objects.requireNonNull(cartasMesa, "Cannot add a null list of cards");
        // Adds the entire collection to the "bottom" (index 0)
        this.cartas.addAll(0, cartasMesa);
    }
}