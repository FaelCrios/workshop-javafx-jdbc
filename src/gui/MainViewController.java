package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{

	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartmente;
	
	@FXML
	private MenuItem menuItemAbout;
	
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	

	@FXML
	public void onMenuItemDepartmentAction() {
		System.out.println("onMenuItemDepartmentAction");
	}
	

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}

	//Como o nosso processo é multiThread sendo por causa da javaFx, o synchronized entra para proibir que nosso processo
	//seja interrompido
	private synchronized void loadView(String absoluteName) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		VBox newVBox = loader.load();
		
		//Aqui criamos uma referencia para nossa cena principal
		Scene mainScene = Main.getMainScene();
		
		//Aqui nós pegamos o conteudo que está dentro dela
		//(ScrollPane)mainScene.getRoot()) nós pegamos o primeiro filho da scene e cascamos ele para ScrollPane que é oque ele é
		//Após isso com (VBox)((ScrollPane)mainScene.getRoot()).getContent(); nós pegamos o conteúdo presente dentro dele e castamos
		//Para VBox sendo essa a scene que nós vamos utilizar para manipular.
		
		VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
		
		//Selecionamos o item que queremos salvar, no caso esse menu
		Node mainMenu = mainVBox.getChildren().get(0);
		//excluimos o conteudo da antiga página 
		mainVBox.getChildren().clear();
		
		//E por fim adicionamos o conteudo Menu, e o conteudo da página que desejamos adicionar
		mainVBox.getChildren().add(mainMenu);
		mainVBox.getChildren().addAll(newVBox.getChildren());
		
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		}
	
}
