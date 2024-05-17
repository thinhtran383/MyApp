package com.example.myapp.repositories;

import com.example.myapp.models.Word;
import javafx.collections.ObservableList;

public interface IWordRepo {
    ObservableList<Word> getAllWords();
    void save(Word word);
    void delete(Word word);
    void importData(String filePath);
}
