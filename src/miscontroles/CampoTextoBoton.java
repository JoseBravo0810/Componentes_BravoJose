package miscontroles;

import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class CampoTextoBoton extends HBox {
    
    @FXML
    private TextField textField;
    @FXML
    private Button btn;

    public CampoTextoBoton() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom_control.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        // Implementacion para gestionar eventos para el ejercicio 4.6.2 
        btn.setOnAction((ActionEvent event) -> {
            propertyOnAction.get().handle(event);
        });
    }

    // Metodo que devuelve el contenido del TextField
    public String getText() {
        return textProperty().get();
    }

    // Metodo que establece el contenido del TextField
    public void setText(String value) {
        textProperty().set(value);
    }
    
    // Metodo añadido por mi para el ejercicio 4.6.2 para limpiar el campo de texto
    public void clearText() {
        textProperty().set("");
    }
    
    // Metodo que devuelve la propiedad del TextField (StringProperty)
    public StringProperty textProperty() {
        return textField.textProperty();
    }

    // De aqui hacia abajo se implementó para la realizacion del ejercicio 4.6.2
    // Propiedad onAction para poder manejar el evento al pulsar el boton de Grabar
    
    private ObjectProperty<EventHandler<ActionEvent>> propertyOnAction = new SimpleObjectProperty<>();
    
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return propertyOnAction;
    }
    
    public final EventHandler<ActionEvent> getOnAction() {
        return propertyOnAction.get();
    }
    
    public final void setOnAction(EventHandler<ActionEvent> handler) {
        propertyOnAction.set(handler);
    }
}