/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campotextonumerico;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;

/**
 *
 * @author jose
 */
public class CampoTextoNumerico extends TextField {
    
    public CampoTextoNumerico(){
        
        super();
    
    }
    
    @Override
    public void replaceText(int start, int end, String text) {
        if(!text.matches("[a-zA-Z]"))
        {
            super.replaceText(start, end, text);
        }
    }
    
    @Override
    public void replaceSelection(String text) {
        if(!text.matches("[a-zA-Z]"))
        {
            super.replaceSelection(text);
        }
    }
    
}
