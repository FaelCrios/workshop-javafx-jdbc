package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	//Sobre o que será nossa View
	@FXML
	private TableView<Department> tableViewDepartment;
	
	
	//Neste caso nossa TableColumn<O tipo da entidade, e depois o tipo da coluna>
	//Outra coisa, não é porque foi definido que as colunas irão funcionar, precisaremos instancia-las 
	//Com a função initialize da nossa interface, criamos a função para isso
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	
	@FXML
	private Button btNew;
	
	public void onBtNewAction() {
		System.out.println("onBTnewAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}

	
	//Função para inicializar nossas colunas com os valores que desejamos
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		
		//Nos definimos um ponto de "apoio" para nossa janela, sendo ela a principal
		//Para que ela possa ter uma orientação de quais seriam as configurações desejadas
		//Com o (Stage)Main.getMainScene().getWindow() selecionamos a scene principal e castamos ela para um stage
		Stage stage	= (Stage)Main.getMainScene().getWindow();
		
		//E depois nós depifinos a nossa table com os valores de height sendo como os da tela em si
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
}
