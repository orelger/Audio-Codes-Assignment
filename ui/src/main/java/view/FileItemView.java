package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */
public class FileItemView {
    @FXML
    public Label fileName = new Label();
    @FXML
    public Label Size = new Label();
    @FXML
    public Label Length = new Label();

    public FileItemView(String fileName, String size, String length) {
        this.fileName.setText(fileName);
        ;
        this.Size.setText(size);
        this.Length.setText(length);
    }
}
