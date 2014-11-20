package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class mainCtrl implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		slideCount.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				labelCount.setText(String.valueOf(newValue.intValue()));
			}
			
		});
		
		 tok = new tokenate(this);
	}

	@FXML
	private RadioButton radTypeSG;
	
	@FXML
	private RadioButton radTypeCG;
	
	@FXML
	private TextField inputCID;

	@FXML
	private TextField inputGID;
	
	@FXML
	private Slider slideCount;
	
	@FXML
	private Label labelCount;
	
	@FXML
	public TextArea txtOut;
	
	@FXML
	public ProgressBar barProgress;
	
	@FXML
	public ComboBox<Object> srvList;
	
	tokenate<Object> tok;
	
	public void deTheBug() {
		// TODO Auto-generated method stub 
		tok.getServers();
	}

	
	public void typeChange() {
		// TODO Auto-generated method stub
		if (radTypeSG.isSelected()) {
			inputCID.setDisable(true);
		} else {
			inputCID.setDisable(false);
		}
	}
	
	public void doGenerate() {
		// TODO Auto-generated method stub
		
		
		if (radTypeSG.isSelected()) {
			tok.genTokens(Integer.parseInt(labelCount.getText()), Integer.parseInt(inputGID.getText()));
		}else {
			tok.genTokens(Integer.parseInt(labelCount.getText()), Integer.parseInt(inputGID.getText()), Integer.parseInt(inputCID.getText()));
		}
		
	}
	
	public void saveText() {
		// TODO Auto-generated method stub
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Save Tokens as Text...");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
		
		File file = fc.showSaveDialog((Stage) inputCID.getScene().getWindow());
		Iterator<CharSequence> toks = txtOut.getParagraphs().iterator();
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
			bf.append("-----------------------------");
			bf.newLine();
			bf.append("--------TS3 Tokenator--------");
			bf.newLine();
			bf.append("-----------------------------");
			bf.newLine();
			
			while (toks.hasNext()) {
				CharSequence token = (CharSequence) toks.next();
				bf.append(token);
				bf.newLine();
			}
			
			bf.flush();
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void doQuit() {
		// TODO Auto-generated method stub
		
		//doDisconnect();
		
		// get a handle to the stage
	    Stage stage = (Stage) inputCID.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}

}
