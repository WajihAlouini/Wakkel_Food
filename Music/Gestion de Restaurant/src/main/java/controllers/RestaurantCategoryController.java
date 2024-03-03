package controllers;

import Services.RestaurantCategoryService;
import entities.RestaurantCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RestaurantCategoryController {
    @FXML
    private TableView<RestaurantCategory> tableView;

    @FXML
    private TableColumn<RestaurantCategory, String> categoryNameColumn;

    @FXML
    private TableColumn<RestaurantCategory, String> categoryImageColumn;

    private final RestaurantCategoryService categoryService = new RestaurantCategoryService(); // Replace CategoryService with your actual service

    public void initialize() {
        // Initialize your TableView and TableColumn
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryImageColumn.setCellValueFactory(new PropertyValueFactory<>("categoryImage"));

        // Fetch data from the database
        ObservableList<RestaurantCategory> categoryList = FXCollections.observableArrayList();
        categoryList.addAll(categoryService.readAll()); // Replace with your actual method to fetch categories

        // Bind the data to the TableView
        tableView.setItems(categoryList);
    }


}
