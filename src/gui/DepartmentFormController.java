package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;

	private DepartmentService service;

	
	//Basicamente isso permite que outros objetos se inscrevam na lista para receber o evento
	//Que será o de visualizar caso haja alguma mudança 
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null ) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
		// Responsavel por pegar os dados das caixas e formar um novo departamento
		entity = getFormData();
		//lembrando que precisamos injetar nosso service no controller atual
		service.saveOrUpdate(entity);
		
		notifyDataChangeListeners();
		
		//Para quando terminar a operação, a janela seja fechada
		Utils.currentStage(event).close();
		} catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	
		
	}

	private void notifyDataChangeListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChange();
		}
		
	}

	//Aqui instanciamos um departamento a partir dos valores recebidos pelas caixas de texto
	//Basicamento criamos um aux para igualar ao que queremos
	private Department getFormData() {
		Department obj = new Department();

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent e) {
		Utils.currentStage(e).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	// Aqui mostraremos conteudo dentro do nosso txt de id e de name, de acordo com
	// o departamento em questao
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		// Transformamos o nosso ID que vem do bd em string para a nossa txtId
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());

	}

}
