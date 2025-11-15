package com.example.fpoe_50zo.model.card;

import java.util.Objects;

/**
 * Representa una carta individual de la baraja.
 * Es una clase inmutable: una vez creada, su palo y valor no pueden cambiar.
 */
public class Carta {

    // Atributos finales, se asignan en el constructor y no cambian
    private final Palo palo;
    private final Valor valor;

    /**
     * Constructor para crear una nueva carta.
     * @param palo El Palo de la carta (ej. Palo.CORAZONES)
     * @param valor El Valor de la carta (ej. Valor.AS)
     */
    public Carta(Palo palo, Valor valor) {
        // Usamos Objects.requireNonNull para asegurar que no nos pasen nulls
        this.palo = Objects.requireNonNull(palo, "El palo no puede ser nulo");
        this.valor = Objects.requireNonNull(valor, "El valor no puede ser nulo");
    }

    // --- Getters ---

    public Palo getPalo() {
        return palo;
    }

    public Valor getValor() {
        return valor;
    }

    // --- Métodos de conveniencia (delegan a los enums) ---

    /**
     * Devuelve el valor numérico de la carta en el juego.
     * Delega la llamada al enum Valor.
     * @return El valor (ej. -10, 0, 5).
     */
    public int getValorJuego() {
        return valor.getValorJuego();
    }

    /**
     * Comprueba si esta carta es un As.
     * Delega la llamada al enum Valor.
     * @return true si es un As.
     */
    public boolean esAs() {
        return valor.esAs();
    }

    /**
     * Genera un nombre de archivo estándar para la imagen de esta carta.
     * Esto es VITAL para conectar el modelo con la vista (JavaFX).
     * La vista buscará un archivo llamado "A_PICAS.png" o "10_DIAMANTES.png".
     *
     * @return Un string como "AS_PICAS.png" o "REY_CORAZONES.png"
     */
    public String getNombreArchivoImagen() {
        // .name() devuelve el nombre del enum en mayúsculas (ej. "AS", "PICAS")
        return valor.name() + "_" + palo.name() + ".png";
    }

    /**
     * Representación textual de la carta, útil para depuración (debug).
     * @return String (ej. "As de Corazones")
     */
    @Override
    public String toString() {
        // Usamos los nombres amigables de los enums
        return valor.getStringRep() + " de " + palo.getNombreVisible();
    }

    // --- Métodos equals() y hashCode() ---
    // Importante para que podamos buscar y comparar cartas en listas.
    // Dos objetos "Carta" son iguales si tienen el mismo palo y valor.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return palo == carta.palo && valor == carta.valor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(palo, valor);
    }
}