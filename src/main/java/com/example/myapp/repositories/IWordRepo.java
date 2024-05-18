package com.example.myapp.repositories;

import com.example.myapp.models.Word;
import javafx.collections.ObservableList;

public interface IWordRepo { // ban thiet ke
    ObservableList<Word> getAllWords(); // observalbeList la 1 list == list
    void save(Word word);
    void delete(Word word);
    void importData(String filePath);
}
