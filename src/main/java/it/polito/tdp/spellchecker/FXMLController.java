/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.spellchecker;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import it.polito.tdp.spellchecker.model.Dictionary;
import it.polito.tdp.spellchecker.model.RichWord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Dictionary model;
	private List<String> testoLista;
	private List<RichWord> testoOutput;
	
	boolean linear = false;
	boolean dichotomic = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxLingua"
    private ComboBox<String> boxLingua; // Value injected by FXMLLoader

    @FXML // fx:id="clearTextButton"
    private Button clearTextButton; // Value injected by FXMLLoader

    @FXML // fx:id="lblErrori"
    private Label lblErrori; // Value injected by FXMLLoader

    @FXML // fx:id="lblStato"
    private Label lblStato; // Value injected by FXMLLoader

    @FXML // fx:id="spellCheckButton"
    private Button spellCheckButton; // Value injected by FXMLLoader

    @FXML // fx:id="txtCorretto"
    private TextArea txtCorretto; // Value injected by FXMLLoader

    @FXML // fx:id="txtDaCorreggere"
    private TextArea txtDaCorreggere; // Value injected by FXMLLoader

    @FXML
    void doActivation(ActionEvent event) {
    	if(boxLingua.getItems() != null) {
    		txtDaCorreggere.setDisable(false);
        	txtDaCorreggere.clear();
        	txtCorretto.setDisable(false);
        	spellCheckButton.setDisable(false);
        	clearTextButton.setDisable(false);
    	}
    }

    @FXML
    void doClearText(ActionEvent event) {
    	txtDaCorreggere.clear();
    	txtCorretto.clear();
    	lblErrori.setText("Number of Errors:");
    	lblStato.setText("Spell Check Status:");
    }

    @FXML
    void doSpellCheck(ActionEvent event) {
    	txtCorretto.clear();
    	testoLista = new ArrayList<String>();
//    	testoLista = new LinkedList<String>();
    	
    	if(!model.loadDictionary(boxLingua.getValue())) {
    		txtDaCorreggere.setDisable(true);
    		txtDaCorreggere.setText("Errore nel caricamento del dizionario.");
    		return;
    	}
    	
    	String testo = txtDaCorreggere.getText().toLowerCase();
    	
    	if(testo==null || testo.equals("")) {
    		txtDaCorreggere.setText("Inserire il testo.");
    		return;
    	}
    	
    	testo = testo.replaceAll("[.,\\/#?!$%\\^&\\*;:{}=\\-_`~()\\[\\]\"]", "");
    	testo = testo.replaceAll("\n", " ");
    	
    	StringTokenizer st = new StringTokenizer(testo, " ");
    	if(!st.hasMoreTokens()) {
    		txtDaCorreggere.setText("Inserire il testo.");
    		return;
    	}
    	
    	while(st.hasMoreTokens()) {
    		testoLista.add(st.nextToken());
    	}
    	
    	long start = System.nanoTime();
    	if(linear) {
    		testoOutput = model.spellCheckTextLinear(testoLista);
    	} else if(dichotomic) {
    		testoOutput = model.spellCheckTextDichotomic(testoLista);
    	} else {
    		testoOutput = model.spellCheckText(testoLista);
    	}
    	long end = System.nanoTime();
    	
    	int numErrori = 0;
    	for(RichWord rw : testoOutput) {
    		if(!rw.isCorrect()) {
    			txtCorretto.appendText(rw + "\n");
    			numErrori++;
    		}
    	}
    	
    	lblErrori.setText("The text contains "+numErrori+" errors");
    	lblStato.setText("Spell check completed in "+(end-start)/1E9+" seconds");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxLingua != null : "fx:id=\"boxLingua\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clearTextButton != null : "fx:id=\"clearTextButton\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblErrori != null : "fx:id=\"lblErrori\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblStato != null : "fx:id=\"lblStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert spellCheckButton != null : "fx:id=\"spellCheckButton\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCorretto != null : "fx:id=\"txtCorretto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDaCorreggere != null : "fx:id=\"txtDaCorreggere\" was not injected: check your FXML file 'Scene.fxml'.";
        
        boxLingua.getItems().clear();
        boxLingua.getItems().addAll("Italian","English");
        
        txtDaCorreggere.setDisable(true);
    	txtDaCorreggere.setText("Selezionare una lingua");
    	txtCorretto.setDisable(true);
    	spellCheckButton.setDisable(true);
    	clearTextButton.setDisable(true);
        
    }
    
    public void setModel(Dictionary model) {
    	this.model = model;
    }

}
