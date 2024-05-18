package com.example.myapp.controllers;

import com.example.myapp.App;
import com.example.myapp.models.Word;
import com.example.myapp.services.IWordService;
import com.example.myapp.services.WordServiceImpl;
import com.example.myapp.utils.AlertUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TableView<Word> tbWords;
    @FXML
    private TableColumn<String, Word> colEnglish;
    @FXML
    private TableColumn<String, Word> colVietnamese;
    @FXML
        private TextField txtSearch;
        @FXML
        private TextField txtEnglish;
        @FXML
    private TextField txtVietnamese;

    private final IWordService wordService;

    public MainController(){
        wordService = new WordServiceImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showOnTable();
    }

    private void showOnTable(){
        colEnglish.setCellValueFactory(new PropertyValueFactory<>("english"));
        colVietnamese.setCellValueFactory(new PropertyValueFactory<>("vietnamese"));

        tbWords.setItems(wordService.getAllWords());
    }

    private boolean isNull(Object ...o){ // spread operator
        for(Object object: o){
            if(object == null || object.toString().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public void onClickRefresh(ActionEvent actionEvent) {
        txtVietnamese.clear();
        txtEnglish.clear();
        txtSearch.clear();
        tbWords.getSelectionModel().clearSelection();
    }

    public void onClickUpdate(ActionEvent actionEvent) {
        Optional<Word> selectedWord = Optional.ofNullable(tbWords.getSelectionModel().getSelectedItem());

        if(selectedWord.isPresent()){
            saveOrUpdate();
            tbWords.setItems(wordService.getAllWords());
        }
    }

    private void saveOrUpdate() {
        String vietnamese = txtVietnamese.getText();
        String english = txtEnglish.getText();

        if(isNull(vietnamese, english)){
            AlertUtil.showAlert(Alert.AlertType.ERROR,"Error", null,"Please fill all the te xtfield!");
            return;
        }

        wordService.saveWord(new Word(english, vietnamese));
    }

    public void onClickDelete(ActionEvent actionEvent) {
        Optional<Word> selectedWord = Optional.ofNullable(tbWords.getSelectionModel().getSelectedItem());
        if(selectedWord.isPresent()){
            wordService.deleteWord(selectedWord.get());
            tbWords.setItems(wordService.getAllWords());
        }
    }

    public void onClickAdd(ActionEvent actionEvent) {
        saveOrUpdate();

        tbWords.setItems(wordService.getAllWords());

    }
    public void onSelected(MouseEvent mouseEvent) {
        Optional<Word> selectedWord = Optional.ofNullable(tbWords.getSelectionModel().getSelectedItem());

        if(selectedWord.isPresent()){
            txtEnglish.setText(selectedWord.get().getEnglish());
            txtVietnamese.setText(selectedWord.get().getVietnamese());
        }
    }

    public void onClickSpeech(ActionEvent actionEvent) {
        Optional<Word> selectedWord = Optional.ofNullable(tbWords.getSelectionModel().getSelectedItem());
        selectedWord.ifPresent(wordService::speech);
    }

    public void onTyping(KeyEvent keyEvent) {
        String keyword = txtSearch.getText();



        ObservableList<Word> words = wordService.getAllWords();
        if(keyword.isEmpty()){
            tbWords.setItems(wordService.getAllWords());
            return;
        }



        ObservableList<Word> filteredWords = words.filtered(word -> word.getEnglish().contains(keyword));
        tbWords.setItems(filteredWords);

    }

    public void onClickImport(ActionEvent actionEvent) {
        wordService.importData();
        tbWords.setItems(wordService.getAllWords());
    }

    public void onClickGame(ActionEvent actionEvent) throws IOException {
        App.setRootPop("GameFrm", "Game", false);
    }
}
