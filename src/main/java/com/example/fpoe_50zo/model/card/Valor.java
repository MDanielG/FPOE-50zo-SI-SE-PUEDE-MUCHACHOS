package com.example.fpoe_50zo.model.card;

/**
 * Representa los 13 valores posibles de una carta de Poker (A, 2-10, J, Q, K).
 * Cada valor almacena su representación en String (ej. "K")
 * y su valor numérico específico para el juego "Cincuentazo".
 */
public enum Valor {

    // Formato: ENUM(valorJuego, stringRepresentacion)

    // El As es especial. Su valor base es 1, pero el controlador
    // deberá preguntar si se usa como 1 o 10.
    AS(1, "A"),
    DOS(2, "2"),
    TRES(3, "3"),
    CUATRO(4, "4"),
    CINCO(5, "5"),
    SEIS(6, "6"),
    SIETE(7, "7"),
    OCHO(8, "8"),

    // Regla: Todas las cartas con número 9 suman 0.
    NUEVE(0, "9"),

    // Regla: 10 suma su número.
    DIEZ(10, "10"),

    // Regla: Todas las cartas con letras J, Q, K restan 10.
    JOTA(-10, "J"),
    QUINA(-10, "Q"), // Reina o "Queen"
    REY(-10, "K");  // Rey o "King"

    // --- Atributos y Métodos del Enum ---

    private final int valorJuego;
    private final String stringRep; // Representación para UI (ej. "K", "A", "9")

    /**
     * Constructor privado del enum.
     * @param valorJuego El valor que esta carta aporta a la suma del juego.
     * @param stringRep La letra o número que representa la carta.
     */
    Valor(int valorJuego, String stringRep) {
        this.valorJuego = valorJuego;
        this.stringRep = stringRep;
    }

    /**
     * Devuelve el valor numérico que esta carta aporta a la suma de la mesa.
     * Para el As, este será su valor base (1).
     * @return int Valor en el juego (ej. -10 para J, 0 para 9, 2 para 2).
     */
    public int getValorJuego() {
        return valorJuego;
    }

    /**
     * Devuelve la representación en String de la carta (ej. "A", "K", "10").
     * @return String
     */
    public String getStringRep() {
        return stringRep;
    }

    /**
     * Método de utilidad para saber si esta carta es un As.
     * Esto es crucial para que el controlador pregunte (1 o 10).
     * @return true si el valor es AS, false en caso contrario.
     */
    public boolean esAs() {
        return this == AS;
    }
}