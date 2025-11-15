package com.example.fpoe_50zo.model.exceptions;

/**
 * A checked exception thrown when an attempt is made to draw a card
 * from an empty deck (Mazo).
 */
public class MazoVacioException extends Exception {

    /**
     * Constructs a new MazoVacioException with a default detail message.
     */
    public MazoVacioException() {
        super("The deck is empty. Cannot draw more cards.");
    }

    /**
     * Constructs a new MazoVacioException with the specified detail message.
     * @param message the detail message.
     */
    public MazoVacioException(String message) {
        super(message);
    }
}