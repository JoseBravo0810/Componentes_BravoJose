/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temporizador;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 *
 * @author jose
 */
public class temporizador extends AnchorPane {
    
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
    
    private Timeline timeline;
    
    private int seconds;
    
    public temporizador(){
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLTemporizador.fxml"));
        
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try{
            fxmlLoader.load();
        }catch (IOException ioex){
            throw new RuntimeException(ioex);
        }
        
        btnActivaTempo.setOnAction((ActionEvent event) -> {
            userSecToSec();
            setTempo();
        });
        
        pauseTempo.setOnAction((ActionEvent event) -> {
            timeline.pause();
        });
        
        resumeTempo.setOnAction((ActionEvent event) -> {
            timeline.play();
        });
    }
    
    private boolean userSecToSec(){
        
        boolean valido = false;
        
        if(!userSeconds.getText().isEmpty())
        {
            try{
                seconds = Integer.parseInt(userSeconds.getText());
                valido = true;
            } catch(NumberFormatException nfex) {
                lbTempo.setText("ERROR. Numbers only.");
            }
        }
        else{
            lbTempo.setText("ERROR. Empty field.");
        }
        
        
        return valido;
    }
    
    private void setTempo() {
        
        if(userSecToSec())
        {
            // Establecemos la duracion (ciclos que dará, ya que cada KeyFrame durará un segundo)
            timeline.setCycleCount(seconds);
            
            KeyFrame kFrameInit = new KeyFrame(Duration.seconds(0), x -> updateLabel());
            KeyFrame kFrame1 = new KeyFrame(Duration.seconds(1), x -> seconds--);
            timeline.getKeyFrames().addAll(kFrameInit, kFrame1);
            
            timeline.setOnFinished((ActionEvent event) -> {
                fireEvent(event);
            });
            
            timeline.play();
        }
        else
        {
            System.out.println("ERROR. El campo de texto no puede contener caracteres no numericos, ya que será el tiempo que durará el temporizador, tampoco puede estar vacio");
        }
        
    }
    
    private void updateLabel() {
        lbTempo.setText("" + seconds);
    }
}


