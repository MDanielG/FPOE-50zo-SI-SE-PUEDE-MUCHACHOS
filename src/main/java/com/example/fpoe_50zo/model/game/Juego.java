package com.example.fpoe_50zo.model.game;

import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.card.Valor;
import com.example.fpoe_50zo.model.deck.Mazo;
import com.example.fpoe_50zo.model.exceptions.JugadaInvalidaException;
import com.example.fpoe_50zo.model.exceptions.MazoVacioException;
import com.example.fpoe_50zo.model.player.Jugador;
import com.example.fpoe_50zo.model.player.JugadorMaquina;

import com.example.fpoe_50zo.model.game.IJuego;
import com.example.fpoe_50zo.model.player.IJugador;
import com.example.fpoe_50zo.model.deck.IMazo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the main "Cincuentazo" game engine.
 * This class manages the game state, players, deck, and enforces all game rules.
 * This is the core of the "Model" in the MVC architecture.
 */
public class Juego implements IJuego {

    private final IMazo mazo;
    private final List<IJugador> jugadores;
    private final List<Carta> cartasEnMesa;

    private int sumaMesa;
    private int indiceJugadorActual;

    /**
     * Constructs a new game instance.
     * Initializes the deck and player lists.
     */
    public Juego() {
        this.mazo = new Mazo();
        this.jugadores = new ArrayList<>();
        this.cartasEnMesa = new ArrayList<>();
        this.sumaMesa = 0;
        this.indiceJugadorActual = 0;
    }

    /**
     * Sets up and starts the game.
     * Creates players, deals initial hands, and places the first card on the table.
     *
     * @param numMaquinas The number of AI players (1, 2, or 3).
     */
    @Override
    public void iniciarJuego(int numMaquinas) {
        // 1. Create players
        jugadores.clear();
        jugadores.add(new Jugador("Player 1", true)); // The Human
        for (int i = 1; i <= numMaquinas; i++) {
            jugadores.add(new JugadorMaquina("CPU " + i));
        }

        // 2. Deal 4 cards to each player
        for (int i = 0; i < 4; i++) {
            for (IJugador jugador : jugadores) {
                try {
                    jugador.tomarCarta(mazo);
                } catch (MazoVacioException e) {
                    // This should never happen on a fresh deck, but good to handle
                    System.err.println("Error: Deck ran out during initial deal.");
                }
            }
        }

        // 3. Place the starting card and set initial sum
        iniciarMesa();

        // 4. Set the turn to the human player (always starts)
        this.indiceJugadorActual = 0;
    }

    /**
     * Helper method to place the first card on the table and set the initial sum
     * according to the special starting rules.
     */
    private void iniciarMesa() {
        try {
            Carta cartaInicial = mazo.tomarCarta();
            cartasEnMesa.add(cartaInicial);

            // "Otras consideraciones" rules for starting sum:
            if (cartaInicial.getValor().esAs()) {
                this.sumaMesa = 1; // Rule: "1 (Carta con la letra A)"
            } else if (cartaInicial.getValor() == Valor.NUEVE) {
                this.sumaMesa = 0; // Rule: "0 (Carta con número 9)"
            } else if (cartaInicial.getValorJuego() == -10) {
                this.sumaMesa = -10; // Rule: " -10 (Cartas con las letras J, Q, K)"
            } else {
                // All other cards (2-8, 10) use their standard game value
                this.sumaMesa = cartaInicial.getValorJuego();
            }

        } catch (MazoVacioException e) {
            // Should be impossible here
            e.printStackTrace();
        }
    }

    /**
     * Attempts to play a non-Ace card for the current player.
     *
     * @param carta The card the player wants to play.
     * @throws JugadaInvalidaException if the move is not allowed.
     */

    @Override
    public void jugarCarta(Carta carta) {
        Objects.requireNonNull(carta, "Card cannot be null");

        if (carta.esAs()) {
            throw new JugadaInvalidaException("Aces must be played using jugarCartaAs()");
        }

        int valorJugado = carta.getValorJuego();
        procesarJugada(carta, valorJugado);
    }

    /**
     * Attempts to play an Ace card for the current player with a chosen value.
     *
     * @param carta The Ace card to play.
     * @param valorElegido The value chosen by the player (must be 1 or 10).
     * @throws JugadaInvalidaException if the move is not allowed.
     */

    @Override
    public void jugarCartaAs(Carta carta, int valorElegido) {
        Objects.requireNonNull(carta, "Card cannot be null");

        if (!carta.esAs()) {
            throw new JugadaInvalidaException("This method is only for playing Aces.");
        }
        if (valorElegido != 1 && valorElegido != 10) {
            throw new JugadaInvalidaException("Ace value must be 1 or 10.");
        }

        procesarJugada(carta, valorElegido);
    }

    /**
     * Private helper to process the logic common to all plays.
     * 1. Validates the move
     * 2. Updates game state
     * 3. Makes player draw a new card
     */
    private void procesarJugada(Carta carta, int valorJugado) {
        IJugador jugadorActual = getJugadorActual();

        // 1. Validation
        if (!jugadorActual.getMano().contains(carta)) {
            throw new JugadaInvalidaException("Player does not have that card.");
        }
        if (this.sumaMesa + valorJugado > 50) {
            throw new JugadaInvalidaException("Move exceeds 50. Sum: " + this.sumaMesa + ", Card: " + valorJugado);
        }

        // 2. Execution: Update game state
        jugadorActual.jugarCarta(carta);
        this.cartasEnMesa.add(carta);
        this.sumaMesa += valorJugado;

        // 3. Execution: Player draws a new card (Rule: "siempre... 4 cartas")
        robarCartaPara(jugadorActual);
    }

    /**
     * Makes the specified player draw one card, handling deck-empty logic.
     * @param jugador The player who needs to draw.
     */
    private void robarCartaPara(IJugador jugador) {
        try {
            jugador.tomarCarta(mazo);
        } catch (MazoVacioException e) {
            // Rule: "Si las cartas del mazo se terminan..."
            System.out.println("Deck is empty. Reshuffling table...");
            gestionarMazoVacio();
            // Retry drawing after reshuffle
            try {
                jugador.tomarCarta(mazo);
            } catch (MazoVacioException e2) {
                // This can happen if table was also empty (e.g., only 1 card)
                // In this rare case, the player simply cannot draw.
                System.err.println("Failed to draw card after reshuffle. No cards available.");
            }
        }
    }

    /**
     * Manages the "deck is empty" rule.
     * Takes all cards from the table except the last one,
     * adds them to the deck, and shuffles.
     */
    private void gestionarMazoVacio() {
        if (cartasEnMesa.size() <= 1) {
            // Cannot reshuffle if there's only one card (or zero) on the table.
            return;
        }

        // 1. Get the last played card
        Carta ultimaJugada = cartasEnMesa.remove(cartasEnMesa.size() - 1);

        // 2. Get all other cards from the table
        List<Carta> cartasARebarajar = new ArrayList<>(this.cartasEnMesa);

        // 3. Clear the table and put the last card back
        this.cartasEnMesa.clear();
        this.cartasEnMesa.add(ultimaJugada); // "excepto la última jugada"

        // 4. Add the old cards to the deck and shuffle
        this.mazo.agregarAlFinal(cartasARebarajar);
        this.mazo.barajar();
    }

    /**
     * Checks if the current player has any valid move.
     * If not, the player is eliminated.
     *
     * @return true if the player can play, false if they were eliminated.
     */

    @Override
    public boolean revisarEstadoJugadorActual() {
        IJugador jugador = getJugadorActual();

        if (!jugador.puedeJugar(this.sumaMesa)) {
            // Rule: "quedará eliminado"
            eliminarJugador(jugador);
            return false;
        }
        return true;
    }

    /**
     * Eliminates a player from the game and returns their cards to the deck.
     * @param jugador The player to eliminate.
     */
    private void eliminarJugador(IJugador jugador) {
        // Rule: "Las cartas del jugador eliminado deben enviarse al final del mazo"
        List<Carta> manoEliminada = jugador.dejarMano();
        for (Carta carta : manoEliminada) {
            mazo.agregarAlFinal(carta);
        }

        this.jugadores.remove(jugador);

        // Adjust the index so the turn passes correctly
        // If we removed a player *before* the current index, the index is now off
        // By using modulo, we ensure it points to a valid player
        if (this.indiceJugadorActual >= this.jugadores.size()) {
            this.indiceJugadorActual = 0; // Wrap around to the start
        }
    }

    /**
     * Advances the turn to the next player in the list.
     */

    @Override
    public void siguienteTurno() {
        if (this.jugadores.isEmpty()) return; // Game over

        this.indiceJugadorActual = (this.indiceJugadorActual + 1) % this.jugadores.size();
    }

    /**
     * Checks if the game has ended (only one player remains).
     * @return true if the game is over, false otherwise.
     */

    @Override
    public boolean isJuegoTerminado() {
        return this.jugadores.size() <= 1;
    }

    // --- Getters for the Controller ---

    @Override
    public IJugador getJugadorActual() {
        if (jugadores.isEmpty()) return null;
        return this.jugadores.get(this.indiceJugadorActual);
    }

    @Override
    public int getSumaMesa() {
        return sumaMesa;
    }

    @Override
    public List<IJugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    @Override
    public Carta getUltimaCartaJugada() {
        if (cartasEnMesa.isEmpty()) return null;
        return cartasEnMesa.get(cartasEnMesa.size() - 1);
    }

    @Override
    public IJugador getGanador() {
        if (isJuegoTerminado() && !jugadores.isEmpty()) {
            return jugadores.get(0);
        }
        return null;
    }
}