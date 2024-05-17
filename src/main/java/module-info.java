module com.example.myapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.base;
    requires java.desktop;

    opens com.example.myapp to javafx.fxml;
    opens com.example.myapp.controllers to javafx.fxml;
    opens com.example.myapp.repositories to javafx.fxml;
    opens com.example.myapp.services to javafx.fxml;
    opens com.example.myapp.models to javafx.fxml;
    opens com.example.myapp.utils to javafx.fxml;

    exports com.example.myapp;
    exports com.example.myapp.controllers;
    exports com.example.myapp.repositories;
    exports com.example.myapp.services;
    exports com.example.myapp.models;
    exports com.example.myapp.utils;
}