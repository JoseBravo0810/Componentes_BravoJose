/*
 * Ejercicio 4.5 Creacion de componentes FXML -> Ejemplo
 */
package miscontroles;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 *
 * @author jose
 */
public class MiControl extends Region{
    
    public MiControl(){
        super();
        // marca la zona donde hacer click
        setStyle("-fx-border-color:red;");
        setPrefSize(100, 100);
        
        // Registramos un handler para capturar los eventos de raton de tipo click -> Instanciacion anonima en los apuntes, aqui lo transformamos en lambda
        // registramos el evento de tipo click
        this.setOnMouseClicked((MouseEvent event) -> {
            // Como es una interfaz, hayq ue implementar el metodo handle
            onActionProperty().get().handle(event);
        });
    }
    
    // Esto deberia ser solo onAction, pero le ponemos el property para distinguirlo del metodo heredado por el boton
    // Es un ObjectProperty de tipo EventHandler que registra los eventos de raton
    // Esta es la forma de hacer que el manejador pueda ser utilizado desde fuera
    private ObjectProperty<EventHandler<MouseEvent>> propertyOnAction = new SimpleObjectProperty<EventHandler<MouseEvent>>();
    
    // Metodo para devolver el manejador
    public final ObjectProperty<EventHandler<MouseEvent>> onActionProperty() {
        return propertyOnAction;
    }
    
    public final void setOnAction(EventHandler<MouseEvent> handler) {
        // Set para el objeto intrinseco que es el EventHandler
        // El set es generico porque estamos creando la property
        propertyOnAction.set(handler);
    }

    public final EventHandler<MouseEvent> getOnAction() {
        return propertyOnAction.get();
    }
}
