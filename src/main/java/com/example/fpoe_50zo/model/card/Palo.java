package com.example.fpoe_50zo.model.card;

/**
 * Representa los cuatro palos de una baraja de Poker.
 * Es un enum para garantizar que solo existan estas cuatro instancias.
 */
public enum Palo {
    // Definimos los cuatro valores posibles
    CORAZONES("Corazones"),
    DIAMANTES("Diamantes"),
    PICAS("Picas"),
    TREBOLES("Tréboles");

    // Atributo para un nombre "amigable" que se puede mostrar
    private final String nombreVisible;

    /**
     * Constructor privado del enum.
     * @param nombreVisible El nombre para mostrar en la UI o consola.
     */
    Palo(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    /**
     * Devuelve el nombre amigable del palo.
     * @return String (ej. "Corazones")
     */
    public String getNombreVisible() {
        return nombreVisible;
    }
}