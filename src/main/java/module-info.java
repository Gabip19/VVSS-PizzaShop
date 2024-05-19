module pizzashop {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens pizzashop;
    exports pizzashop;
    opens pizzashop.model;
    exports pizzashop.model;
    opens pizzashop.controller;
    exports pizzashop.controller;
    opens pizzashop.service;
    exports pizzashop.service;
    opens pizzashop.repository;
    exports pizzashop.repository;
    opens pizzashop.gui;
    exports pizzashop.gui;
}
