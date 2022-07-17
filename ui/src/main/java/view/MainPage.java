package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.HttpClient;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static utils.Utils.*;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */
public class MainPage implements Initializable {

    @FXML
    public ListView<FileItem> listView = new ListView<>();
    @FXML
    public Button folder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        folder.setOnMouseClicked(this::chooseFolder);
//        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectableItem();
    }

    private void getSelectableItem() {
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    System.out.println(listView.getSelectionModel().getSelectedIndices());
                    System.out.println(newValue);
//                    listView.getSelectionModel().getSelectedIndices().forEach(integer -> {
//                        Thread thread = new Thread(() -> {
//                            HttpClient httpClient = new HttpClient(SELECT);
//                            httpClient.createPostRequest(newValue.getFileName());
//                        });
//                        thread.start();
//                    });

                    if (oldValue != null && newValue != null) {
                        Thread thread = new Thread(() -> {
                            HttpClient httpClient = new HttpClient(SELECT);
                            httpClient.createPostRequest(oldValue.getFileName() + "\nSecondItem" + newValue.getFileName());
                        });
                        thread.start();
                    } else if (newValue != null) {
                        Thread thread = new Thread(() -> {
                            HttpClient httpClient = new HttpClient(SELECT);
                            httpClient.createPostRequest(newValue.getFileName());
                        });
                        thread.start();
                    }
                });
    }

    private void chooseFolder(MouseEvent mouseEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(MUSIC_PATH));
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        displayMP3Files(selectedDirectory.getAbsolutePath());
    }

    private void displayMP3Files(String absolutePath) {
        listView.getItems().clear();
        List<model.File> fileList;
        List<FileItem> fileItemList = new ArrayList<>();
        HttpClient httpClient = new HttpClient(SAVE);
        fileList = httpClient.createPostRequest(absolutePath);
        fileList.forEach(file -> {
            FileItem fileItem = new FileItem(file.getFileName(), file.getSize(), file.getLength());
            fileItemList.add(fileItem);
        });

        listView.getItems().addAll(fileItemList);
    }
}
