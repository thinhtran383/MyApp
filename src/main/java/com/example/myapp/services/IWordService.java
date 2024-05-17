package com.example.myapp.services;

import com.example.myapp.models.Word;
import javafx.collections.ObservableList;

public interface IWordService {
    ObservableList<Word> getAllWords();
    void saveWord(Word word);
    void deleteWord(Word word);
    void updateWord(Word word);
    void speech(Word word);

    void importData();

}
