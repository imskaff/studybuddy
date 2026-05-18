package client.viewmodel;

import client.model.Model;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public abstract class CreateProfileViewModel
{
  protected Model model;

  private final StringProperty firstName   = new SimpleStringProperty("");
  private final StringProperty lastName    = new SimpleStringProperty("");
  private final StringProperty email       = new SimpleStringProperty("");
  private final StringProperty phoneNo     = new SimpleStringProperty("");
  private final StringProperty gender      = new SimpleStringProperty("");
  private final StringProperty password    = new SimpleStringProperty("");
  private final StringProperty dateOfBirth = new SimpleStringProperty("");
  private final ObjectProperty<UserType> userType = new SimpleObjectProperty<>();

  private final ObservableList<String> languages =
      FXCollections.observableArrayList();
  private final StringProperty languageInput = new SimpleStringProperty("");

  public CreateProfileViewModel(Model model, UserType type){
    this.model = model;
    this.userType.set(type);
  }

  public abstract void register() throws SQLException;

  public StringProperty firstNameProperty(){ return firstName; }
  public StringProperty lastNameProperty(){ return lastName; }
  public StringProperty emailProperty(){ return email; }
  public StringProperty phoneNoProperty(){ return phoneNo; }
  public StringProperty genderProperty(){ return gender; }
  public StringProperty passwordProperty(){ return password; }
  public StringProperty dateOfBirthProperty(){ return dateOfBirth; }
  public ObservableList<String> getLanguages()   { return languages; }
  public ReadOnlyObjectProperty<UserType> userTypeReadOnlyObjectProperty(){return userType;}
}
