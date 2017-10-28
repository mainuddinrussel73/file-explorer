package filebrowser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FileBrowser extends Application{
    
    @Override
    public void start(Stage stage) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("FileExplorer.fxml"));
        Scene scene=new Scene(root);
        stage.setTitle("Windows File Explorer");
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("filebrowser/unnamed.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
    
}