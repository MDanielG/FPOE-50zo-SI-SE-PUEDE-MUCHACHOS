package com.example.fpoe_50zo.model.game;

import com.example.fpoe_50zo.model.exceptions.JugadaInvalidaException;
import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.player.IJugador;

import java.util.List;

/**
 * Interface that defines the contract for the main "Cincuentazo" game engine.
 * It specifies all the high-level operations the controller needs.
 * Note: It uses other interfaces (IJugador) in its contract.
 */
public interface IJuego {

    /**
     * Sets up and starts the game.
     * @param numMaquinas The number of AI players (1, 2, or 3).
     */
    void iniciarJuego(int numMaquinas);

    /**
     * Attempts to play a non-Ace card for the current player.
     * @param carta The card the player wants to play.
     * @throws JugadaInvalidaException if the move is not allowed.
     */
    void jugarCarta(Carta carta);

    /**
     * Attempts to play an Ace card for the current player with a chosen value.
     * @param carta The Ace card to play.
     * @param valorElegido The value chosen by the player (must be 1 or 10).
     * @throws JugadaInvalidaException if the move is not allowed.
     */
    void jugarCartaAs(Carta carta, int valorElegido);

    /**
     * Checks if the current player has any valid move.
     * If not, the player is eliminated.
     * @return true if the player can play, false if they were eliminated.
     */
    boolean revisarEstadoJugadorActual();

    /**
     * Advances the turn to the next player in the list.
     */
    void siguienteTurno();

    /**
     * Checks if the game has ended (only one player remains).
     * @return true if the game is over, false otherwise.
     */
    boolean isJuegoTerminado();

    // --- Getters for Game State (for the Controller) ---

    /**
     * @return The current sum on the table.
     */
    int getSumaMesa();

    /**
     * @return The last card played on the table.
     */
    Carta getUltimaCartaJugada();

    /**
     * @return The player whose turn it is currently.
     */
    IJugador getJugadorActual();

    /**
     * @return An unmodifiable list of all players still in the game.
     */
    List<IJugador> getJugadores();

    /**
     * @return The winning player, or null if the game is not over.
     */
    IJugador getGanador();
}