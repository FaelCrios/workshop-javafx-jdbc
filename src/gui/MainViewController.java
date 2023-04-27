package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;

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
	

	//Para que possamos definir uma dependencia de forma certa e instanciar nosso contudo, passaremos a inicialização por parâmetro
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		} );
	}
	

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}

	//Como o nosso processo é multiThread sendo por causa da javaFx, o synchronized entra para proibir que nosso processo
	//seja interrompido
	//Como passaremos nossas inicializações por parâmetro
	//Precisamos utilizar nossa interface de consumer que foi visto antes, para que quando necessario
	//Possamos utilizar as expressões lambdas
	private synchronized<T> void loadView(String absoluteName, Consumer<T> initializingAction) {
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
		
		
		//Vai retornar o nosso controlador que desejamos
		//E assim vai executar o nosso genérico que nesse caso é a nossa expressão Lambda
		T controller = loader.getController();
		initializingAction.accept(controller);
		
		
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		}
	
}
