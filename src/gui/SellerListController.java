package gui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	// Sobre o que será nossa View
	@FXML
	private TableView<Seller> tableViewSeller;

	// Aqui estamos criando uma dependencia, de forma fraca
	private SellerService service;

	// Neste caso nossa TableColumn<O tipo da entidade, e depois o tipo da coluna>
	// Outra coisa, não é porque foi definido que as colunas irão funcionar,
	// precisaremos instancia-las
	// Com a função initialize da nossa interface, criamos a função para isso
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
			//	button.setOnAction(
				//		event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete ?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {

			throw new IllegalStateException("Servicewas null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}catch(DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
			}
	}

	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		//createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	// Injeção de dependencia manual
	public void setSellerService(SellerService service) {
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
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	// Esta função é para criarmos uma janela sobre um stage pai, por isso passamos
	// por parâmetro qual o stage que está criando
	// Uma tela
//	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//
//			// Carregamos o nosso pane, que será nosso formulario
//			Pane pane = loader.load();
//
//			// Aqui passaremos uma referencia para o nosso controlador, para que seja
//			// possivel
//			// Alterar seu conteudo
//			SellerFormController controller = loader.getController();
//
//			// Passamos nossa referencia como haviamos criado a nossa instanciação no
//			// arquivo pai
//			controller.setSeller(obj);
//
//			// Injeção de dependencia do SellerService
//			controller.setSellerService(new SellerService());
//
//			// Inscrição para o evento do subject
//			controller.subscribeDataChangeListener(this);
//
//			// E aqui atualizamos o conteudo presente no controller
//			controller.updateFormData();
//
//			// Quando vamos iniciar uma janela de dialogo em frente ao nosso stage, é
//			// necessario
//			// instancia um novo stage desa forma o dialogStage
//			Stage dialogStage = new Stage();
//
//			// E aqui entram as configurações
//			dialogStage.setTitle("Enter department data");
//
//			// Definimos qual sera o stage que iremos carregar
//			dialogStage.setScene(new Scene(pane));
//
//			// Se poderemos mudar o tamanho de nossa tela
//			dialogStage.setResizable(false);
//
//			// A partir de quem que ele irá surgir
//			dialogStage.initOwner(parentStage);
//
//			// E qual será o tipo de janela que ele irá ser
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//		} catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}

	// Basicamente este será o nosso observador, que a partir do momento que a outra
	// classe Controller,
	// Emitir o sinal, efetuaremos uma ação: nesse caso a atualização dos dados
	@Override
	public void onDataChange() {
		updateTableView();

	}

}
