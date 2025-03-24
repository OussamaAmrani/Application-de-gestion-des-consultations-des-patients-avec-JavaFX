package ma.enset.demo;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GestionConsultations extends Application {

    // List to store consultations
    private final ObservableList<Consultation> consultations = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Consultations Cliniques");

        // UI Elements
        TextField patientNameField = new TextField();
        patientNameField.setPromptText("Nom du patient");

        TextField doctorNameField = new TextField();
        doctorNameField.setPromptText("Nom du docteur");

        DatePicker consultationDate = new DatePicker();

        TextArea notesField = new TextArea();
        notesField.setPromptText("Notes");

        Button addButton = new Button("Ajouter");
        Button deleteButton = new Button("Supprimer");
        Button modifyButton = new Button("Modifier");

        TableView<Consultation> tableView = new TableView<>(consultations);

        TableColumn<Consultation, String> patientColumn = new TableColumn<>("Patient");
        patientColumn.setCellValueFactory(data -> data.getValue().patientNameProperty());

        TableColumn<Consultation, String> doctorColumn = new TableColumn<>("Docteur");
        doctorColumn.setCellValueFactory(data -> data.getValue().doctorNameProperty());

        TableColumn<Consultation, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data -> data.getValue().consultationDateProperty());

        TableColumn<Consultation, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(data -> data.getValue().notesProperty());

        tableView.getColumns().addAll(patientColumn, doctorColumn, dateColumn, notesColumn);

        // Layout
        VBox inputForm = new VBox(10, patientNameField, doctorNameField, consultationDate, notesField, addButton, modifyButton, deleteButton);
        inputForm.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setLeft(inputForm);
        root.setCenter(tableView);

        // Event Handlers
        addButton.setOnAction(e -> {
            String patientName = patientNameField.getText();
            String doctorName = doctorNameField.getText();
            String date = consultationDate.getValue() != null ? consultationDate.getValue().toString() : "";
            String notes = notesField.getText();

            if (!patientName.isEmpty() && !doctorName.isEmpty() && !date.isEmpty()) {
                consultations.add(new Consultation(patientName, doctorName, date, notes));
                patientNameField.clear();
                doctorNameField.clear();
                consultationDate.setValue(null);
                notesField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs obligatoires.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        deleteButton.setOnAction(e -> {
            Consultation selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                consultations.remove(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une consultation à supprimer.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        modifyButton.setOnAction(e -> {
            Consultation selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Update the fields with selected consultation's values
                patientNameField.setText(selected.getPatientName());
                doctorNameField.setText(selected.getDoctorName());
                consultationDate.setValue(java.time.LocalDate.parse(selected.getConsultationDate()));
                notesField.setText(selected.getNotes());

                // When the "Modifier" button is clicked, update the selected consultation
                addButton.setText("Mettre à jour");
                addButton.setOnAction(event -> {
                    String patientName = patientNameField.getText();
                    String doctorName = doctorNameField.getText();
                    String date = consultationDate.getValue() != null ? consultationDate.getValue().toString() : "";
                    String notes = notesField.getText();

                    if (!patientName.isEmpty() && !doctorName.isEmpty() && !date.isEmpty()) {
                        selected.setPatientName(patientName);
                        selected.setDoctorName(doctorName);
                        selected.setConsultationDate(date);
                        selected.setNotes(notes);

                        // Refresh table
                        tableView.refresh();

                        // Clear the fields and revert button text
                        patientNameField.clear();
                        doctorNameField.clear();
                        consultationDate.setValue(null);
                        notesField.clear();
                        addButton.setText("Ajouter");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs obligatoires.", ButtonType.OK);
                        alert.showAndWait();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une consultation à modifier.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Inner Class for Consultation
    public static class Consultation {
        private final StringProperty patientName;
        private final StringProperty doctorName;
        private final StringProperty consultationDate;
        private final StringProperty notes;

        public Consultation(String patientName, String doctorName, String consultationDate, String notes) {
            this.patientName = new SimpleStringProperty(patientName);
            this.doctorName = new SimpleStringProperty(doctorName);
            this.consultationDate = new SimpleStringProperty(consultationDate);
            this.notes = new SimpleStringProperty(notes);
        }

        public String getPatientName() {
            return patientName.get();
        }

        public void setPatientName(String patientName) {
            this.patientName.set(patientName);
        }

        public String getDoctorName() {
            return doctorName.get();
        }

        public void setDoctorName(String doctorName) {
            this.doctorName.set(doctorName);
        }

        public String getConsultationDate() {
            return consultationDate.get();
        }

        public void setConsultationDate(String consultationDate) {
            this.consultationDate.set(consultationDate);
        }

        public String getNotes() {
            return notes.get();
        }

        public void setNotes(String notes) {
            this.notes.set(notes);
        }

        public StringProperty patientNameProperty() {
            return patientName;
        }

        public StringProperty doctorNameProperty() {
            return doctorName;
        }

        public StringProperty consultationDateProperty() {
            return consultationDate;
        }

        public StringProperty notesProperty() {
            return notes;
        }
    }
}
