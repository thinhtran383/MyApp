package com.example.myapp.services;

import com.example.myapp.models.Word;
import com.example.myapp.repositories.WordRepoImpl;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WordServiceImpl implements IWordService {
    private final WordRepoImpl wordRepo;
    private final String region = "eastasia";
    private final String apiKey = "b2732572853145d599061f47de724a0f";
    private final String issueTokenUrl = "https://eastasia.api.cognitive.microsoft.com/sts/v1.0/issueToken";
    private final String textToSpeedUrl = "https://eastasia.tts.speech.microsoft.com/cognitiveservices/v1";
    private String token;
    public WordServiceImpl() {
        wordRepo = new WordRepoImpl();
    }

    @Override
    public ObservableList<Word> getAllWords() {
        return wordRepo.getAllWords();
    }

    @Override
    public void saveWord(Word word) {
        wordRepo.save(word);
    }

    public void deleteWord(Word word){
        wordRepo.delete(word);
    }

    @Override
    public void updateWord(Word word) {
        wordRepo.save(word);
    }

    @Override
    public void speech(Word word) {
        textToSpeech(word.getEnglish(),"eng");
    }

    @Override
    public void importData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println(selectedFile.getAbsolutePath());
            wordRepo.importData(selectedFile.getAbsolutePath());
        }
    }

    public void textToSpeech(String text, String language) {
        String data;
        if(language.equals("vi-VN")) {
            data = "<speak version='1.0' xml:lang='vi-VN'>\n" +
                    "    <voice xml:lang='vi-VN' xml:gender='Female' name='vi-VN-HoaiMyNeural'>\n" +
                    text + "\n" +
                    "    </voice>\n" +
                    "</speak>";
        }
        else {
            data = "<speak version='1.0' xml:lang='en-US'>\n" +
                    "    <voice xml:lang='en-US' xml:gender='Male' name='en-US-ChristopherNeural'>\n" +
                    text + "\n" +
                    "    </voice>\n" +
                    "</speak>";
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create(textToSpeedUrl))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/ssml+xml")
                .header("X-Microsoft-OutputFormat", "riff-24khz-16bit-mono-pcm")
                .header("Authorization", "Bearer " + token)
                .header("Ocp-Apim-Subscription-Key", this.apiKey)
                .header("Accept", "*/*")
                .build();
        try {
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() == 200) {
                byte[] bytes = response.body().readAllBytes();
                try (OutputStream os = new FileOutputStream("out.wav")) {
                    os.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("out.wav"));
                // Get a Clip instance
                Clip clip = AudioSystem.getClip();

                // Open the audioInputStream
                clip.open(audioInputStream);

                // Start playing the clip
                clip.start();

                // Sleep while the clip is playing
                Thread.sleep(clip.getMicrosecondLength() / 1000);

                // Close the clip
                clip.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
