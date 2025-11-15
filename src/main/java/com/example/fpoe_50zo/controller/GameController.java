package com.example.fpoe_50zo.controller;

import com.example.fpoe_50zo.model.card.Carta;
import com.example.fpoe_50zo.model.game.IJuego;
import com.example.fpoe_50zo.model.game.Juego;
import com.example.fpoe_50zo.model.player.IJugador;
import com.example.fpoe_50zo.model.player.JugadorMaquina;
import com.example.fpoe_50zo.model.exceptions.JugadaInvalidaException;

// JavaFX (UI, Events, Animations)
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

// Utilies de Java
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * Controller for the GameView.fxml.
 * Manages all user interaction, updates the UI, and connects the
 * View (FXML) to the Model (Juego).
 */
public class GameController {

    // --- FXML Components (Linked from Scene Builder) ---
    @FXML private BorderPane rootPane;
    @FXML private Label sumaLabel;
    @FXML private ImageView ultimaCartaView;
    @FXML private Label mensajeLabel;
    @FXML private HBox manoJugadorBox;

    // (Optional: FXML IDs for opponent VBoxes to show/hide them)
    @FXML private VBox cpu1Box;
    @FXML private HBox cpu1HandBox;
    @FXML private Label cpu1NameLabel;

    @FXML private VBox cpu2Box;
    @FXML private HBox cpu2HandBox;
    @FXML private Label cpu2NameLabel;

    @FXML private VBox cpu3Box;
    @FXML private HBox cpu3HandBox;
    @FXML private Label cpu3NameLabel;

    // --- Game Logic (Model) ---
    private IJuego juego;
    private boolean esTurnoHumano = true; // Lock to prevent clicks when it's not player's turn

    private Image imagenReverso;
    // Path to the card images in the 'resources' folder
    private final String RUTA_IMAGENES = "/com/example/fpoe_50zo/images/";
    // Map to link a UI ImageView back to its Carta object
    private final Map<ImageView, Carta> mapaCartasJugador = new HashMap<>();

    /**
     * Called by JavaFX *after* the FXML file is loaded.
     * This is the entry point to set up the game.
     */
    @FXML
    public void initialize() {
        this.juego = new Juego();
        this.imagenReverso = cargarImagen("card_back.png");
    }

    /**
     * This method will be called by the SetupScreenController.
     * It receives the number of opponents and NOW starts the game.
     *
     * @param numOponentes The number of machines (1, 2, or 3)
     */
    public void prepararJuego(int numOponentes) {
        // Esta es la lógica que ANTES estaba en initialize()
        juego.iniciarJuego(numOponentes);

        actualizarVistaCompleta();
        mensajeLabel.setText("¡Juego iniciado! Es tu turno.");

        // Aquí puedes añadir la lógica para mostrar/ocultar
        // las VBox de los oponentes (cpu1Box, cpu2Box, etc.)
        // según el 'numOponentes'.
    }

    /**
     * Central method to refresh the entire UI based on the
     * current state of the 'juego' (Model).
     */
    private void actualizarVistaCompleta() {
        // 1. Update Sum and Last Card
        sumaLabel.setText("Suma: " + juego.getSumaMesa());
        if (juego.getUltimaCartaJugada() != null) {
            ultimaCartaView.setImage(cargarImagen(juego.getUltimaCartaJugada().getNombreArchivoImagen()));
        }

        // 2. Update Player's Hand (Human)
        manoJugadorBox.getChildren().clear();
        mapaCartasJugador.clear();

        IJugador humano = juego.getJugadores().get(0); // Assuming human is always at index 0
        for (Carta carta : humano.getMano()) {
            ImageView cartaView = crearVistaCarta(carta);
            manoJugadorBox.getChildren().add(cartaView);
            mapaCartasJugador.put(cartaView, carta);
        }

        // 3. Update Opponent's Hands (showing card backs)
        // (This is a simplified version; you can make this more robust)
        // ... (Code to show/hide cpu1Box, cpu2Box and show card_back images) ...
        // 1. Ocultar todos los contenedores de CPU (para resetear)
        if (cpu1Box != null) cpu1Box.setVisible(false);
        if (cpu2Box != null) cpu2Box.setVisible(false);
        if (cpu3Box != null) cpu3Box.setVisible(false);

        List<IJugador> jugadores = juego.getJugadores();

        // 2. Iterar la lista de jugadores, *saltando al humano* (i=1)
        for (int i = 1; i < jugadores.size(); i++) {
            IJugador jugadorActual = jugadores.get(i);
            int numCartas = jugadorActual.getCartasEnMano();

            // Variables temporales para el jugador actual
            VBox vBoxOponente = null;
            HBox hBoxMano = null;
            Label labelNombre = null;

            // 3. Asignar los FXML correctos según la posición
            if (i == 1 && cpu1Box != null) { // Oponente 1
                vBoxOponente = cpu1Box;
                hBoxMano = cpu1HandBox;
                labelNombre = cpu1NameLabel;
            } else if (i == 2 && cpu2Box != null) { // Oponente 2
                vBoxOponente = cpu2Box;
                hBoxMano = cpu2HandBox;
                labelNombre = cpu2NameLabel;
            } else if (i == 3 && cpu3Box != null) { // Oponente 3
                vBoxOponente = cpu3Box;
                hBoxMano = cpu3HandBox;
                labelNombre = cpu3NameLabel;
            }

            // 4. Si encontramos un contenedor para este oponente...
            if (vBoxOponente != null) {
                // ... lo hacemos visible
                vBoxOponente.setVisible(true);
                // ... actualizamos su nombre
                if (labelNombre != null) labelNombre.setText(jugadorActual.getNombre());

                // ... limpiamos su mano anterior
                if (hBoxMano != null) {
                    hBoxMano.getChildren().clear();

                    // ... y dibujamos la cantidad correcta de reversos
                    for (int j = 0; j < numCartas; j++) {
                        ImageView reversoView = new ImageView(this.imagenReverso);
                        reversoView.setFitHeight(100.0); // Más pequeñas que las del jugador
                        reversoView.setFitWidth(70.0);
                        reversoView.setPreserveRatio(true);
                        hBoxMano.getChildren().add(reversoView);
                    }
                }
            }
        }
    }

    /**
     * Creates a new ImageView for a card and sets its click event handler.
     * @param carta The Carta object this view represents.
     * @return A configured ImageView.
     */

    private ImageView crearVistaCarta(Carta carta) {
        Image img = cargarImagen(carta.getNombreArchivoImagen());
        ImageView cartaView = new ImageView(img);
        cartaView.setFitHeight(150.0);
        cartaView.setFitWidth(100.0);
        cartaView.setPreserveRatio(true);

        // Añadir sombra para que "flote"
        DropShadow shadow = new DropShadow(10, Color.BLACK);
        shadow.setRadius(10);
        cartaView.setEffect(shadow);

        // --- ¡MAGIA DE HOVER! ---
        TranslateTransition liftUp = new TranslateTransition(Duration.millis(150), cartaView);
        liftUp.setToY(-30); // Levantar 30 píxeles

        TranslateTransition dropDown = new TranslateTransition(Duration.millis(150), cartaView);
        dropDown.setToY(0);

        cartaView.setOnMouseEntered(event -> {
            if (esTurnoHumano) {
                liftUp.play();
            }
        });

        cartaView.setOnMouseExited(event -> {
            dropDown.play();
        });
        // --- Fin Magia de Hover ---

        cartaView.setOnMouseClicked(this::onCartaClickeada);

        return cartaView;
    }

    /**
     * Event handler for when the human player clicks a card in their hand.
    * This method triggers the card-playing animation and logic.
     *
    * @param event The MouseEvent generated by the click.
    */
    @FXML
    private void onCartaClickeada(MouseEvent event) {
        if (!esTurnoHumano) {
            mensajeLabel.setText("Por favor, espera tu turno.");
            return;
        }

        ImageView cartaView = (ImageView) event.getSource();
        Carta cartaClickeada = mapaCartasJugador.get(cartaView);

        if (cartaClickeada == null) return; // Safety check

        // Disable hand to prevent double-clicks
        esTurnoHumano = false;
        manoJugadorBox.setDisable(true);

        // Trigger the animation. The game logic runs *after* it finishes.
        animarJugada(cartaView, () -> {
            // This Runnable executes when the animation is complete
            try {
                if (cartaClickeada.esAs()) {
                    // Ace requires a dialog, which has its own logic flow
                    mostrarDialogoAs(cartaClickeada);
                } else {
                    // Play a regular card
                    juego.jugarCarta(cartaClickeada);
                    procesarSiguienteTurno();
                }
            } catch (JugadaInvalidaException e) {
                // Handle illegal move
                mensajeLabel.setText("¡Jugada inválida! " + e.getMessage());
                // Re-enable hand since the turn wasn't valid
                esTurnoHumano = true;
                manoJugadorBox.setDisable(false);
            }
        });
    }

    /**
     * Animates a card "flying" from the player's hand to the discard pile.
     *
     * @param cartaView The ImageView to animate.
     * @param onAnimationFinished A Runnable to execute when the animation ends.
     */
    private void animarJugada(ImageView cartaView, Runnable onAnimationFinished) {

        // 1. Move the card from the HBox to the rootPane
        // This allows it to fly *over* other UI elements.
        manoJugadorBox.getChildren().remove(cartaView);

        // Convert local coordinates to scene coordinates
        Bounds startBounds = cartaView.localToScene(cartaView.getBoundsInLocal());
        Bounds endBounds = ultimaCartaView.localToScene(ultimaCartaView.getBoundsInLocal());

        rootPane.getChildren().add(cartaView);
        cartaView.setTranslateX(0); // Reset hover animations
        cartaView.setTranslateY(0);

        // Place it at its starting position in the rootPane
        cartaView.setLayoutX(startBounds.getMinX());
        cartaView.setLayoutY(startBounds.getMinY());

        // 2. Create the animation path
        Path path = new Path();
        path.getElements().add(new MoveTo(0, 0)); // Start at 0,0 (relative to the card)

        // Calculate destination
        double endX = endBounds.getMinX() - startBounds.getMinX() + (endBounds.getWidth() - startBounds.getWidth()) / 2;
        double endY = endBounds.getMinY() - startBounds.getMinY() + (endBounds.getHeight() - startBounds.getHeight()) / 2;

        path.getElements().add(new LineTo(endX, endY));

        PathTransition transition = new PathTransition(Duration.millis(400), path, cartaView);
        transition.setCycleCount(1);
        transition.setOnFinished(event -> {
            // 3. Clean up after animation
            rootPane.getChildren().remove(cartaView); // Remove from rootPane
            onAnimationFinished.run(); // Execute game logic
        });

        transition.play();
    }

    /**
     * Displays a dialog box for the player to choose the value of an Ace (1 or 10).
     *
     * @param as The Ace card that was played.
     */
    /**
     * Shows a dialog for the player to choose the value of an Ace.
     * @param as The Ace card that was played.
     */
    private void mostrarDialogoAs(Carta as) {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, 10);
        dialog.setTitle("Elegir Valor del As");
        dialog.setHeaderText("El As puede valer 1 o 10.");
        dialog.setContentText("Elige un valor:");

        Optional<Integer> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            int valorElegido = resultado.get();
            try {
                juego.jugarCartaAs(as, valorElegido);
                esTurnoHumano = false; // Turn is over
                procesarSiguienteTurno();
            } catch (JugadaInvalidaException e) {
                // This could happen if they choose 10 but sum is 45
                mensajeLabel.setText("¡Jugada inválida! " + e.getMessage());
            }
        }
        // If dialog is canceled, do nothing and let the player try again.
    }

    /**
     * Manages the game flow after a player's move.
     * It checks for eliminations, game over, and AI turns.
     */
    private void procesarSiguienteTurno() {
        // 1. Refresh UI after player's move
        actualizarVistaCompleta();

        // 2. Check if player's move ended the game
        if (verificarFinDeJuego()) return;

        // 3. Pass turn
        juego.siguienteTurno();

        // 4. Check if the *new* player can play.
        // If not, eliminate them and repeat.
        while (!juego.revisarEstadoJugadorActual()) {
            mensajeLabel.setText("¡" + juego.getJugadorActual().getNombre() + " ha sido eliminado!");

            // Check if elimination ended the game
            if (verificarFinDeJuego()) return;

            juego.siguienteTurno(); // Pass turn again
        }

        // 5. The current player *can* play. Is it human or AI?
        if (juego.getJugadorActual().esHumano()) {
            esTurnoHumano = true;
            manoJugadorBox.setDisable(false);
            mensajeLabel.setText("¡Tu turno!");
            actualizarVistaCompleta();
        } else {
            // It's AI's turn
            mensajeLabel.setText("Turno de " + juego.getJugadorActual().getNombre() + "...");
            ejecutarTurnoMaquina();
        }
    }

    /**
     * Runs the AI's turn on a separate thread to avoid freezing the UI.
     * Fulfills the "al menos dos hilos" requirement (this is Thread 1).
     */
    private void ejecutarTurnoMaquina() {
        IJugador jugadorActual = juego.getJugadorActual();

        if (!(jugadorActual instanceof JugadorMaquina cpu)) {
            // Si no es una JugadorMaquina (p.ej. es humano), hay un error de lógica.
            System.err.println("Error: Se esperaba un JugadorMaquina pero no lo era.");
            esTurnoHumano = true; // Devolver control
            return;
        }

        // Use a JavaFX Task for background work (Thread)
        Task<Carta> turnoAITask = new Task<>() {
            @Override
            protected Carta call() throws Exception {
                // REQUIREMENT: THREAD 1 - Simulates AI "thinking"
                Thread.sleep(1500); // 1.5 second delay

                // AI logic (from Model)
                return cpu.decidirMejorJugada(juego.getSumaMesa());
            }
        };

        // What to do *after* the task (thread) is done
        turnoAITask.setOnSucceeded(event -> {
            Carta cartaJugada = turnoAITask.getValue();

            if (cartaJugada != null) {
                // AI has a valid card to play
                try {
                    if (cartaJugada.esAs()) {
                        // AI logic for Ace: use 10 if possible, else 1
                        int valor = (juego.getSumaMesa() + 10 <= 50) ? 10 : 1;
                        juego.jugarCartaAs(cartaJugada, valor);
                        mensajeLabel.setText(cpu.getNombre() + " jugó un As como " + valor);
                    } else {
                        juego.jugarCarta(cartaJugada);
                        mensajeLabel.setText(cpu.getNombre() + " jugó un " + cartaJugada.getValor().getStringRep());
                    }
                } catch (Exception e) {
                    // This should not happen if AI logic is correct
                    System.err.println("Error en lógica de IA: " + e.getMessage());
                }
            } else {
                // AI has no card to play (this should be impossible if
                // revisarEstadoJugadorActual() worked, but good to check)
            }

            // After AI move, pass the turn
            procesarSiguienteTurno();
        });

        // Start the thread
        new Thread(turnoAITask).start();
    }

    /**
     * Checks if the game has ended. If so, shows a winner message.
     * @return true if the game ended, false otherwise.
     */
    private boolean verificarFinDeJuego() {
        if (juego.isJuegoTerminado()) {
            IJugador ganador = juego.getGanador();
            mensajeLabel.setText("¡Juego terminado! El ganador es " + ganador.getNombre());

            // Show a final alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Fin del Juego!");
            alert.setHeaderText("El ganador es " + ganador.getNombre());
            alert.showAndWait();

            // Disable interactions
            esTurnoHumano = false;
            manoJugadorBox.setDisable(true);
            return true;
        }
        return false;
    }

    /**
     * Helper utility to load an image from the resources folder.
     * @param nombreArchivo (e.g., "AS_PICAS.png")
     * @return The Image object, or null on failure.
     */
    private Image cargarImagen(String nombreArchivo) {
        String path = RUTA_IMAGENES + nombreArchivo;
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Error: No se pudo encontrar la imagen: " + path);
            return null;
        }
        return new Image(url.toExternalForm());
    }
}