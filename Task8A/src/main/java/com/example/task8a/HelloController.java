package com.example.task8a;

import DAO.TaskDAO;
import DAO.TaskDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.DataItem;
import model.Plan;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML public TextField name;
    @FXML public TextField time;
    @FXML public TextField context;
    @FXML public TextField status;


    public ComboBox<DataItem> groupComboBox;
    public ComboBox<String> SemesterComboBox;
    public ComboBox<DataItem> eduFormComboBox2;
    public Label statsLabel;
    Connection conn;
    TaskDAOImpl impl;
    public TableView table;//таблица из fxml-определения
    private ObservableList<Plan> fxlist;// cпециальный список для работы GUI

    /**
     * Метод createtable() отвечает за создание таблицы и заполнение ее данными.
     */
    private void createtable() {

        TableColumn Col0 = new TableColumn("Группа");
        Col0.setCellValueFactory(new PropertyValueFactory<Plan, String>("groupName"));

        TableColumn Col1 = new TableColumn("Форма обучения");//отображаемый заголовок столбца
        Col1.setCellValueFactory(new PropertyValueFactory<Plan, String>("formOfEducation"));

        TableColumn Col2 = new TableColumn("Наименование дисцеплины");//отображаемый заголовок столбца
        Col2.setPrefWidth(250);
        Col2.setCellValueFactory(new PropertyValueFactory<Plan, String>("subject"));

        TableColumn Col3 = new TableColumn("Семестр");//отображаемый заголовок столбца
        Col3.setCellValueFactory(new PropertyValueFactory<Plan, String>("simest"));
        Col3.setPrefWidth(60);

        TableColumn Col4 = new TableColumn("Форма аттестации");//отображаемый заголовок столбца
        Col4.setCellValueFactory(new PropertyValueFactory<Plan, String>("formOfAttestation"));

        table.getColumns().clear();
        table.getColumns().addAll(Col0, Col1, Col2, Col3, Col4);
        table.setItems(fxlist);
    }

    /**
     * Метод selectionEventHandler() обрабатывает событие выбора элементов из комбо-боксов.
     * Забирает значения выбранных элементов и передает их методу getStatistics() для получения соответствующих статистических данных.
     * @param inputMethodEvent
     */
    public void selectionEventHandler(ActionEvent inputMethodEvent) {

        String[] options = new String[3];
        if (groupComboBox.getSelectionModel().getSelectedItem() != null) {
            options[0] = groupComboBox.getSelectionModel().getSelectedItem().getValueMember();
        }

        if (SemesterComboBox.getSelectionModel().getSelectedItem() != null) {
            options[1] = SemesterComboBox.getSelectionModel().getSelectedItem();
        }
        if (eduFormComboBox2.getSelectionModel().getSelectedItem() != null) {
            options[2] = eduFormComboBox2.getSelectionModel().getSelectedItem().getValueMember();
        }

        if(options[0] != null && options[1] != null){
            statsLabel.setText(getStatistics(options[0], options[1]));
            
            
        }

        table.setItems(FXCollections.observableList(impl.updatePlans(options[0], options[1], options[2])));
    }

    /**
     * Метод deliteButton() представляет обработчик события нажатия на кнопку "Удалить" в графическом интерфейсе приложения
     * @param inputMethodEvent
     */
    public void deliteButton(ActionEvent inputMethodEvent){


        try{

            Plan plan = (Plan) table.getSelectionModel().getSelectedItem();

            impl.deletePlan(plan);

            table.setItems(FXCollections.observableList(impl.getAllPlans()));
        }catch (Exception e){
            e.getMessage();
        }
    }

    /**
     * Метод getStatistics() предназначен для получения статистических данных о занятиях для выбранной группы и семестра
     * @param groupId
     * @param Simestr
     * @return
     */
    public String getStatistics(String groupId, String Simestr) {
        StringBuilder statsMessage =  new StringBuilder();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM (SELECT Studgroup.code_Studgroup, Discipline.name AS discipline_name, Discipline.semester, FormOfTraining.name AS form_of_training, AttestationForm.name as test FROM Discipline JOIN Studgroup ON Studgroup.faculty_id = Discipline.faculty_id AND Studgroup.educationalProgram_id = Discipline.formOfTraining_id JOIN FormOfTraining ON FormOfTraining.id = Discipline.formOfTraining_id JOIN AttestationForm ON AttestationForm.id = Discipline.attestationForm_id WHERE Discipline.semester =  " + Simestr + " AND Studgroup.id = " + groupId + " AND AttestationForm.name = 'Зачет')");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                statsMessage.append("Зачёты: ").append(rs.getString(1)).append("\r");
            }

            ps = conn.prepareStatement("SELECT COUNT(*) FROM (SELECT Studgroup.code_Studgroup, Discipline.name AS discipline_name, Discipline.semester, FormOfTraining.name AS form_of_training, AttestationForm.name as test FROM Discipline JOIN Studgroup ON Studgroup.faculty_id = Discipline.faculty_id AND Studgroup.educationalProgram_id = Discipline.formOfTraining_id JOIN FormOfTraining ON FormOfTraining.id = Discipline.formOfTraining_id JOIN AttestationForm ON AttestationForm.id = Discipline.attestationForm_id WHERE Discipline.semester =  " + Simestr + " AND Studgroup.id = " + groupId + " AND AttestationForm.name = 'Дифференцированный зачет')");
            rs = ps.executeQuery();

            while (rs.next()){
                statsMessage.append("Дифференцированные зачеты: ").append(rs.getString(1)).append("\r");
            }

            ps = conn.prepareStatement("SELECT COUNT(*) FROM (SELECT Studgroup.code_Studgroup, Discipline.name AS discipline_name, Discipline.semester, FormOfTraining.name AS form_of_training, AttestationForm.name as test FROM Discipline JOIN Studgroup ON Studgroup.faculty_id = Discipline.faculty_id AND Studgroup.educationalProgram_id = Discipline.formOfTraining_id JOIN FormOfTraining ON FormOfTraining.id = Discipline.formOfTraining_id JOIN AttestationForm ON AttestationForm.id = Discipline.attestationForm_id WHERE Discipline.semester =  " + Simestr + " AND Studgroup.id = " + groupId + " AND AttestationForm.name = 'Экзамен')");
            rs = ps.executeQuery();

            while (rs.next()){
                statsMessage.append("Экзамены: ").append(rs.getString(1));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf(statsMessage);
    }

    /**
     * Метод initialize(URL url, ResourceBundle resourceBundle) класса HelloController, который вызывается при открытии соответствующего FXML-файла.
     * Он содержит инициализацию элементов пользовательского интерфейса и подключение к базе данных.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createtable();

        impl = new TaskDAOImpl();
        table.setItems(FXCollections.observableList(impl.getAllPlans()));

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Plan plan = (Plan) table.getSelectionModel().getSelectedItem();
               // taskDAO.deletePlan(plan);
                System.out.println(plan.toString());
            }
        });

        ObservableList<DataItem> groupComboBoxOptions = FXCollections.observableArrayList();
        ObservableList<String> semesterComboBoxOptions = FXCollections.observableArrayList();

        try {
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");

            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT SEMESTER FROM DISCIPLINE");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                semesterComboBoxOptions.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SemesterComboBox.setItems(semesterComboBoxOptions);

        groupComboBox.setCellFactory(param -> new ListCell<DataItem>() {
            /**
             * Метод updateItem() задает отображение каждого элемента списка ComboBox в графическом интерфейсе на основе его состояния и свойств.
             * @param item
             * @param empty
             */
            @Override
            protected void updateItem(DataItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayMember());
                }
            }
        });
        groupComboBox.setConverter(new StringConverter<DataItem>() {

            /**
             * Он содержит метод toString(DataItem dataItem), который конвертирует объект типа DataItem в строку для отображения в пользовательском интерфейсе
             * @param dataItem
             * @return
             */
            @Override
            public String toString(DataItem dataItem) {
                if (dataItem == null) {
                    return null;
                } else {
                    return dataItem.getDisplayMember();
                }
            }
            @Override
            public DataItem fromString(String s) {
                return null;
            }
        });
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM STUDGROUP");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DataItem di = new DataItem(rs.getString(2), rs.getString(1));
                groupComboBoxOptions.add(di);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        groupComboBox.setItems(groupComboBoxOptions);


        ObservableList<DataItem> eduFormComboBoxOptions2 = FXCollections.observableArrayList();

        eduFormComboBox2.setCellFactory(param -> new ListCell<DataItem>() {
            /**
             * создаёт список опций для выпадающего списка groupComboBox и устанавливает его как опции выпадающего списка.
             * @param item
             * @param empty
             */
            @Override
            protected void updateItem(DataItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayMember());
                }
            }
        });
        eduFormComboBox2.setConverter(new StringConverter<DataItem>() {
            @Override
            public String toString(DataItem dataItem) {
                if (dataItem == null) {
                    return null;
                } else {
                    return dataItem.getDisplayMember();
                }
            }
            @Override
            public DataItem fromString(String s) {
                return null;
            }
        });
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM FormOfTraining");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DataItem di = new DataItem(rs.getString(2), rs.getString(1));
                eduFormComboBoxOptions2.add(di);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        eduFormComboBox2.setItems(eduFormComboBoxOptions2);


    }
    public void showAdminPanel(ActionEvent actionEvent) {
        Label disciplineLabel = new Label("Наименование дисциплины:");
        TextField semesterTextField = new TextField("");
        Label semesterLabel = new Label("Семестр:");

        ObservableList<DataItem> eduFormComboBoxOptions = FXCollections.observableArrayList();
        ComboBox<DataItem> eduFormComboBox = new ComboBox<>();
        eduFormComboBox.setPrefWidth(160);

        eduFormComboBox.setCellFactory(param -> new ListCell<DataItem>() {
            @Override
            protected void updateItem(DataItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayMember());
                }
            }
        });
        eduFormComboBox.setConverter(new StringConverter<DataItem>() {
            @Override
            public String toString(DataItem dataItem) {
                if (dataItem == null) {
                    return null;
                } else {
                    return dataItem.getDisplayMember();
                }
            }
            @Override
            public DataItem fromString(String s) {
                return null;
            }
        });
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM FormOfTraining");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DataItem di = new DataItem(rs.getString(2), rs.getString(1));
                eduFormComboBoxOptions.add(di);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        eduFormComboBox.setItems(eduFormComboBoxOptions);

        Label eduFormLabel = new Label("Форма обучения:");
        ObservableList<DataItem> attestationComboBoxOptions = FXCollections.observableArrayList();
        ComboBox<DataItem> attestationComboBox = new ComboBox<>();
        attestationComboBox.setPrefWidth(160);

        attestationComboBox.setCellFactory(param -> new ListCell<DataItem>() {
            @Override
            protected void updateItem(DataItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayMember());
                }
            }
        });
        attestationComboBox.setConverter(new StringConverter<DataItem>() {
            @Override
            public String toString(DataItem dataItem) {
                if (dataItem == null) {
                    return null;
                } else {
                    return dataItem.getDisplayMember();
                }
            }
            @Override
            public DataItem fromString(String s) {
                return null;
            }
        });
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM AttestationForm");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DataItem di = new DataItem(rs.getString(2), rs.getString(1));
                attestationComboBoxOptions.add(di);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        attestationComboBox.setItems(attestationComboBoxOptions);

        Label attestationLabel = new Label("Форма аттестации:");
        TextField nameTextField = new TextField("");

        Label facultyNameLabel = new Label("Факультет");
        ObservableList<DataItem> facultyComboBoxOptions = FXCollections.observableArrayList();
        ComboBox<DataItem> facultyComboBox = new ComboBox<>();
        facultyComboBox.setPrefWidth(160);


        facultyComboBox.setCellFactory(param -> new ListCell<DataItem>() {
            @Override
            protected void updateItem(DataItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayMember());
                }
            }
        });
        facultyComboBox.setConverter(new StringConverter<DataItem>() {
            @Override
            public String toString(DataItem dataItem) {
                if (dataItem == null) {
                    return null;
                } else {
                    return dataItem.getDisplayMember();
                }
            }
            @Override
            public DataItem fromString(String s) {
                return null;
            }
        });
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Faculty");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DataItem di = new DataItem(rs.getString(2), rs.getString(1));
                facultyComboBoxOptions.add(di);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        facultyComboBox.setItems(facultyComboBoxOptions);



        Button submitButton = new Button("Добавить дисциплину");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        GridPane.setMargin(disciplineLabel, new Insets(0, 5, 0, 0));
        GridPane.setMargin(semesterLabel, new Insets(0, 5, 0, 0));
        GridPane.setMargin(eduFormLabel, new Insets(0, 5, 0, 0));
        GridPane.setMargin(attestationLabel, new Insets(0, 5, 0, 0));
        GridPane.setMargin(nameTextField, new Insets(0, 0, 0, 5));

        gridPane.add(disciplineLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);
        gridPane.add(semesterLabel, 0, 1);
        gridPane.add(semesterTextField, 1, 1);
        gridPane.add(eduFormLabel, 0, 3);
        gridPane.add(eduFormComboBox, 1, 3);
        gridPane.add(attestationLabel, 0, 4);
        gridPane.add(attestationComboBox, 1, 4);

        gridPane.add(facultyNameLabel, 0, 5);
        gridPane.add(facultyComboBox, 1, 5);

        gridPane.add(submitButton, 1, 6);

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Метод handle (ActionEvent event) является обработчиком события (event handler) для кнопки "Добавить" в графическом интерфейсе приложения. Обработчик события содержит проверку наличия всех обязательных полей ввода данных.
             * Выполняется операция вставки новых данных в таблицу Discilpine базы данных с помощью SQL-запроса.
             * @param event
             */
            @Override
            public void handle(ActionEvent event) {
                if(nameTextField.getText() != "" && semesterTextField.getText() != "" && eduFormComboBox.getSelectionModel().getSelectedItem() != null && attestationComboBox.getSelectionModel().getSelectedItem() != null && facultyComboBox.getSelectionModel().getSelectedItem() != null) {
                    impl.addNewPlan(nameTextField.getText(), semesterTextField.getText(),  eduFormComboBox.getSelectionModel().getSelectedItem().getValueMember(), attestationComboBox.getSelectionModel().getSelectedItem().getValueMember(), facultyComboBox.getSelectionModel().getSelectedItem().getValueMember());
                    table.setItems(FXCollections.observableList(impl.getAllPlans()));
                }
            }
        });

        Scene scene = new Scene(gridPane, 400, 300);

        // create a new window and set the scene with grid pane
        Stage newWindow = new Stage();
        newWindow.setTitle("Панель добавления новой дисциплины");
        newWindow.setScene(scene);
        newWindow.show();
    }
}