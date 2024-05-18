package com.example.myapp.repositories;

import com.example.myapp.models.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WordRepoImpl implements IWordRepo {
    String FILE_PATH = "src/main/resources/com/example/myapp/dictionaries.txt";

    @Override
    public ObservableList<Word> getAllWords() {
        ObservableList<Word> wordList = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) { // doc tung  dong cho den khi het
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String english = parts[0].trim();
                    String vietnamese = parts[1].trim();
                    Word word = new Word(english, vietnamese);
                    wordList.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordList;
    }

    @Override
    public void save(Word word) { // luu hoac cap nhat (kiem tra neu ton tai thi la cap nhat <> them moi)
        List<Word> existingWords = new ArrayList<>();
        boolean wordExists = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String english = parts[0].trim();
                    String vietnamese = parts[1].trim();
                    Word existingWord = new Word(english, vietnamese);
                    existingWords.add(existingWord);

                    if (existingWord.getEnglish().equals(word.getEnglish())) {
                        wordExists = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Word existingWord : existingWords) {
                if (!existingWord.getEnglish().equals(word.getEnglish())) {
                    bw.write(existingWord.getEnglish() + "\t" + existingWord.getVietnamese());
                    bw.newLine();
                }
            }
            if (!wordExists) {
                bw.write(word.getEnglish() + "\t" + word.getVietnamese());
                bw.newLine();
            } else {
                bw.write(word.getEnglish() + "\t" + word.getVietnamese());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Word word) {
        List<Word> existingWords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String english = parts[0].trim();
                    String vietnamese = parts[1].trim();
                    Word existingWord = new Word(english, vietnamese);
                    if (!existingWord.getEnglish().equals(word.getEnglish())) {
                        existingWords.add(existingWord);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Word existingWord : existingWords) {
                bw.write(existingWord.getEnglish() + "\t" + existingWord.getVietnamese());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importData(String importFilePath) {
        List<Word> existingWords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String english = parts[0].trim();
                    String vietnamese = parts[1].trim();
                    Word word = new Word(english, vietnamese);
                    existingWords.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Đọc các từ từ tệp nhập và thêm vào danh sách nếu không tồn tại
        try (BufferedReader br = new BufferedReader(new FileReader(importFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String english = parts[0].trim();
                    String vietnamese = parts[1].trim();
                    boolean exists = false;
                    for (Word existingWord : existingWords) {
                        if (existingWord.getEnglish().equals(english)) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        existingWords.add(new Word(english, vietnamese));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ghi lại tất cả các từ vào tệp chính
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Word word : existingWords) {
                bw.write(word.getEnglish() + "\t" + word.getVietnamese());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
