package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	// Sobre o que será nossa View
	@FXML
	private TableView<Department> tableViewDepartment;

	// Aqui estamos criando uma dependencia, de forma fraca
	private DepartmentService service;

	// Neste caso nossa TableColumn<O tipo da entidade, e depois o tipo da coluna>
	// Outra coisa, não é porque foi definido que as colunas irão funcionar,
	// precisaremos instancia-las
	// Com a função initialize da nossa interface, criamos a função para isso
	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	public void onBtNewAction() {
		System.out.println("onBTnewAction");
	}

	// Injeção de dependencia manual
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	// Função para inicializar nossas colunas com os valores que desejamos
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Nos definimos um ponto de "apoio" para nossa janela, sendo ela a principal
		// Para que ela possa ter uma orientação de quais seriam as configurações
		// desejadas
		// Com o (Stage)Main.getMainScene().getWindow() selecionamos a scene principal e
		// castamos ela para um stage
		Stage stage = (Stage) Main.getMainScene().getWindow();

		// E depois nós depifinos a nossa table com os valores de height sendo como os
		// da tela em si
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

	}
 
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}

}
