/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selectordeslizamiento;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
*
* @author
*/
public class SelectorDeslizamiento extends AnchorPane {
    
    // Boton anterior
    @FXML
    private Button previousButton;
    // Etiqueta de en medio con valor seleccionado de la lista
    @FXML
    private Label label;
    // Boton siguiente
    @FXML
    private Button nextButton;
    
    // Array con la lista de objetos de la lista
    ArrayList<String> items;
    // Entero que indica el valor seleccionado en el selector
    int selectedIndex;
    // Atributo que indica si al llegar al final de la lista se vuelve al principio (carrusel), o si no
    private boolean repetitive;

    public SelectorDeslizamiento() {
        
        // Cargamos el apartado visual del componente
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLSelectorDeslizamiento.fxml"));
        
        fxmlLoader.setRoot(this); // Añadimos el cargador al contenedor que representa el propio componente (extiende de AnchorPane)
        fxmlLoader.setController(this); // Establecemos el propio componente como el controlador
        
        // Intentamos cargar el apartado visual
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        // Establecemos la lista (lista vacia)
        items = new ArrayList<>();
        // Establecemos el indice seleccionado en 0, comienzo de la lista
        selectedIndex = 0;
        
        // Si clicamos en el boton de anterior
        previousButton.setOnAction((ActionEvent event) -> {
            setPrevious(); // Cambiamos el indice de la lista al anterior
            fireEvent(event); // Ejecutamos el evento
        });
        
        // Si clicamos en el boton siguiente
        nextButton.setOnAction((ActionEvent event) -> {
            setNext(); // El indice pasa al siguiente elemento de la lista
            fireEvent(event); // Se ejecuta el evento
        });
    }
    
    // Metodo para establecer una lista de elementos y seleccionar el primero
    public void setItems(ArrayList<String> items) {
        this.items = items;
        selectFirst();
    }

    // Cuando hacemos click en anterior
    public void setPrevious() {
        updateItem(selectedIndex - 1); // Se actualiza el elemento mostrado en la etiqueta quitandole al indice seleccionado 1 (retrocedemos al elemento anterior)
    }

    // Cuando hacemos click en siguiente
    public void setNext() {
        updateItem(selectedIndex + 1); // Se actualiza el elemento mostrado en la etiqueta aumentandole al indice seleccionado 1 (avanzamos al siguiente elemento)
    }
    
    // Metodo para seleccionar el primer elemento de la lista
    public void selectFirst() {
        updateItem(0);
    }
    
    // Metodo para seleccionar el ultimo elemento de la lista
    private void selectLast() {
        updateItem(items.size() - 1);
    }

    // Metodo para actualizar el elemento mostrado
    private void updateItem() {
        // Si la lista está vacia
        if (items.isEmpty()) {
            label.setText("Vacio"); // Muestra vacio en la etiqueta
        } else { // Si no esta vacia..
            if (selectedIndex < 0) { // ..y es menor que 0 (en una lista el primer elemento se encuentra en la posicion 0)
                if (repetitive) { // Si está activo el modo carrusel
                    selectedIndex = items.size() - 1; // Se selecciona el ultimo elemento, damos la vuelta
                } else { // Si el modo carrusel esta desactivado
                    selectedIndex = 0; // No pasará de la primera posicion
                }
            }
            if (selectedIndex >= items.size()) { // Si el indice seleccionado se sale del array por la parte superior..
                if (repetitive) { // .. y el modo carrusel esta activo
                    selectedIndex = 0; // El indice pasará al primer elemento (da la vuelta)
                } else { // Si esta desactivado
                    selectedIndex = items.size() - 1; // Se mantiene en el ultimo elemento
                }
            }
            
            // Actualizamo la etiqueta con el valor del elemento en la lista en la posicion que haya quedado el selector
            label.setText(items.get(selectedIndex));
        }
    }
    
    // Podemos actualizar el elemento al indice que queramos directamente
    private void updateItem(int selectedIndex) {
        this.selectedIndex = selectedIndex; // Cambiamos el indice
        updateItem(); // Ejecutamos el metodo anterior para que se produzca el cambio en la etiqueta
    }
    
    // Metodo para establecer el modo carrusel
    public void setRepetitive(boolean cyclesThrough) {
        this.repetitive = cyclesThrough;
    }
    
    // Metodo para acceder al wrapper de la propiedad onAction
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }
    
    // Metodo para establecer el manejador del evento que se ejecutará al hacer click en algun boton
    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }
    
    // Metodo para acceder al manejador de la propiedad onAction
    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }
    
    // Propiedad onAction. En lugar de una SimpleObjectProperty como en MiControl, esta vez es un ObjectPropertyBase
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
            @Override
            protected void invalidated() {
                setEventHandler(ActionEvent.ACTION, get());
            }
            
            @Override
            public Object getBean() {
                return SelectorDeslizamiento.this;
            }
            
            @Override
            public String getName() {
                return "onAction";
            }
    };
}