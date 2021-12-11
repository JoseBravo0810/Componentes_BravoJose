package temporizador;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author jose
 */
public class Temporizador extends VBox {
    
    // Atributos de la vista del componente
    @FXML
    private Label lbTempo;
    @FXML
    private TextField userSeconds;
    @FXML
    private Button btnActivaTempo;
    @FXML
    private Button pauseTempo;
    @FXML
    private Button resumeTempo;
    
    // Atributo donde se guardan los segundos de duracion del temporizador
    private int seconds;
    
    // Timeline para la animacion del contador de segundos
    private Timeline timeline = new Timeline();
    
    public Temporizador(){
        // Cargamos e FXML con la vista del componente
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLTemporizador.fxml"));
        
        // Establecemos la clase Temporizador como el nodo raiz del FXML y como el controlador
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        // Intentamos cargar el fichero FXML
        try{
            fxmlLoader.load();
        }catch (IOException ioex){
            throw new RuntimeException(ioex);
        }
        
        // Establecemos los KeyFrames de la animacion
        KeyFrame kFrameInit = new KeyFrame(Duration.seconds(0), x -> updateLabel()); // El KeyFrame inicial actualiza la etiqueta con los segundos en una duracion de 0 segundos
        KeyFrame kFrame1 = new KeyFrame(Duration.seconds(1), x -> seconds--); // KeyFrame1 -> Actualiza los segundos reduciendolos en una unidad (dura 1 segundo)
        KeyFrame kFrame2 = new KeyFrame(Duration.seconds(0), x -> updateLabel()); // KeyFrame2 -> Vuelve a actualizar la etiqueta (Dura 0 segundos)
        // Los KeyFrames se ejecutarán de uno en uno en orden por cada ciclo del Timeline
        timeline.getKeyFrames().addAll(kFrameInit, kFrame1, kFrame2); // Añadimos los tres KeyFrames creados
        
        timeline.setAutoReverse(false); // Deshabilitamos la vuelta del ciclo, solo ida
        
        // Establecemos la accion que realizará el boton Start
        btnActivaTempo.setOnAction((ActionEvent event) -> {
            timeline.stop(); // Detiene el Timeline (Esto lo hacemos por si aun está activo), Si el Timeline aun no ha terminado, lo termina. Si ya ha terminado, no tiene efecto alguno.
            setTempo(); // Ejecutamos el metodo setTempo() para establecer el temporizador iniciando uno nuevo con los segundos pasados en el TextField
            event.consume(); // Consumimos el evento
        });
        
        // Establecemos la accion que realizará el boton Pause
        pauseTempo.setOnAction((ActionEvent event) -> {
            timeline.pause(); // Pausa la animacion del Timeline (si está activo, sino no tendrá efecto)
            event.consume(); // Consumimos el evento
        });
        
        // Establecemos la accion que realizará el boton Resume
        resumeTempo.setOnAction((ActionEvent event) -> {
            if(timeline.getStatus() == Animation.Status.PAUSED) // Si el temporizador esta pausado
                timeline.play(); // Lo reanuda
            else if(timeline.getStatus() == Animation.Status.STOPPED) { // Si el temporizador esta detenido o finalizado
                lbTempo.setText("ERROR"); // Muestra error en la etiqueta
                System.out.println("\nERROR. Inicie otro temporizador, este ya ha llegado a su fin, no se puede reanudar un temporizador agotado\n"); // Explica el error en la consola
            }
            
            event.consume(); // Consumimos el evento
        });
    }
    
    // Metodo para pasar la cadena de texto del TextField a entero que se almacena en la variable seconds
    private boolean userSecToSec(){
        
        // Booleano para saber si se ha realizado el parseInt correctamente
        boolean valido = false;
        
        // Si el campo no está vacio
        if(!userSeconds.getText().isEmpty())
        {
            // Intentamos...
            try{
                seconds = Integer.parseInt(userSeconds.getText()); // ... Pasar la cadena de texto a entero
                valido = true; // Si llegamos aqui es porque no ha saltado la excepcion, el parse se ha realizado con exito y ponemos a true el booleano indicando que el valor introducido es valido
            } catch(NumberFormatException nfex) { // Si salta la excepcion
                lbTempo.setText("ERROR"); // Mostramos error en la etiqueta
                System.out.println("\nERROR. El campo de texto no puede contener caracteres no numericos, ya que será el tiempo que durará el temporizador.\n"); // Especificamos el error por consola
            }
        }
        else{ // Si está vacio
            lbTempo.setText("ERROR"); // Mostramos error en la etiqueta
            System.out.println("\nERROR. El campo de texto no puede estar vacio, debe indicar los segundos de duracion del temporizador\n"); // Especificamos error por consola
        }
        
        // Devolvemos el flag para saber si la conversion se ha realizado correctamente
        return valido;
    }
    
    // Metodo para establecer el temporizador
    private void setTempo() {
        
        // Si se ha conseguido pasar la etiqueta a segundos
        if(userSecToSec())
        {
            // Establecemos la duracion (ciclos que dará, ya que cada KeyFrame durará un segundo [(kFrameInit = 0 sec) + (kFrame1 = 1 sec) + (kFrame2 = 0 sec) = 1 sec])
            timeline.setCycleCount(seconds + 1); // Sumamos 1 para que se vea el 0 en la cuenta atrás, ya que si no, al llegar al 1 finaliza el temporizador, finaliza el numero de ciclos.
            
            // Establecemos la funcionalidad que tendrá el temporizador al finalizar
            timeline.setOnFinished((ActionEvent event) -> {
                lbTempo.setText("FIN"); // Muestra FIN en la etiqueta
                seconds = 0; // Establece los segundos a 0
                // Intentamos gestionar el evento con el handler que se le establezca a la propiedad onAction del componente. Si no mensaje.
                try {propertyOnAction.get().handle(event);}
                catch(NullPointerException npex) {System.out.println("\nNo hay manejador establecido, puede darle mayor funcionalidad al temporizador asignandole una accion a realizar cuando finalice el tiempo\n");}
            });
            
            // Comenzamos el temporizador
            timeline.play();
        }
        
    }
    
    // Metodo apra actualizar la etiqueta
    private void updateLabel() {
        lbTempo.setText("" + seconds); // Establecemos los segundos en la etiqueta
    }
    
    // Propiedad para darle accion al componete que se ejecutará al finalizar el temporizador
    private ObjectProperty<EventHandler<ActionEvent>> propertyOnAction = new SimpleObjectProperty<>();
    
    // Metodo que devuelve el wrapper con el manejador de evento de la propiedad
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return propertyOnAction;
    }
    
    // Metodo que devuelve el manejador
    public final EventHandler<ActionEvent> getOnAction() {
        return propertyOnAction.get();
    }
    
    // Metodo que establece el manejador
    public final void setOnAction(EventHandler<ActionEvent> handler) {
        propertyOnAction.set(handler);
    }
}


