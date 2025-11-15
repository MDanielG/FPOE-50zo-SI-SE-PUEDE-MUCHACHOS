package com.example.fpoe_50zo.model.exceptions;
/**
 * An unchecked exception thrown when a player attempts to make
 * an invalid move according to the game rules.
 * (e.g., playing a card that exceeds the sum of 50).
 */
public class JugadaInvalidaException extends RuntimeException {

    /**
     * Constructs a new JugadaInvalidaException with the specified detail message.
     * @param message the detail message.
     */
    public JugadaInvalidaException(String message) {
        super(message);
    }
}
