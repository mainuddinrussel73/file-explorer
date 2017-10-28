package filebrowser;

import static java.awt.Color.white;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javax.swing.filechooser.FileSystemView;

public class FXMLcontroller implements Initializable{

    @FXML
    private ListView<File> listviewFiles;

    @FXML
    private TreeView<File> treeviewFileBrowse;

    @FXML 
    private TextField search;

    //@FXML
    //private Button go;
    
    
    @FXML
    private AnchorPane pan;
     
    @FXML
    private Label icon;
    
    @FXML
    private Label last;
    
    @FXML
    private Label size;
    
    @FXML
    private Label file;
    private int Flag = -1;
    @FXML
    private   GridPane root;
    
    @FXML
    private ScrollPane s;
    
    @FXML
    private MenuItem go;
    
    @FXML
    private MenuItem go1;
// @FXML
    //private TilePane root;
    
    ObservableList<File> data = FXCollections.observableArrayList();
    public FXMLcontroller(){
        s = null;
    
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<File> rootNode=new TreeItem<>(new File("This PC"),new ImageView(new Image(ClassLoader.getSystemResourceAsStream("filebrowser/computer.png"))));
        Iterable<Path> rootDirectories=FileSystems.getDefault().getRootDirectories();
      
        for(Path name:rootDirectories){
            FilePathTreeItem treeNode=new FilePathTreeItem(new File(name.toString()));
            treeviewFileBrowse.getSelectionModel().selectedItemProperty().addListener(new ChangeListenerImpl());
            //rootNode.getParent();
            
            rootNode.getChildren().add(treeNode);
        }
        
     
      
       //listf("c:/");
       listviewFiles.setItems(data);
       listviewFiles.setCellFactory(new CallbackImpl());
       
        AnchorPane.setTopAnchor(listviewFiles, 10.0);
        AnchorPane.setLeftAnchor(listviewFiles, 10.0);
        AnchorPane.setRightAnchor(listviewFiles, 15.0); 
        AnchorPane.setLeftAnchor(icon, 20.0);
        AnchorPane.setTopAnchor(icon, -10.0);
        AnchorPane.setLeftAnchor(last, 100.0);
        AnchorPane.setTopAnchor(last, -10.0);
        AnchorPane.setLeftAnchor(size, 240.0);
        AnchorPane.setTopAnchor(size, -10.0);
        AnchorPane.setLeftAnchor(file, 330.0);
        AnchorPane.setTopAnchor(file, -10.0);
        pan.getChildren().remove(s);
        pan.getChildren().addAll(listviewFiles,icon,last,size,file);
    
      
        rootNode.setExpanded(true);
        this.treeviewFileBrowse.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.treeviewFileBrowse.setRoot(rootNode);
       
        CreateViewFactory c = new CreateViewFactory();
        go1.setOnAction(c.setView("Table"));
        go.setOnAction(c.setView("Tile"));
    }
    private  class CreateViewFactory {
        public  EventHandler<ActionEvent> e;

        public CreateViewFactory() {
            
        }
        public EventHandler<ActionEvent> setView(String s){
            if(s.equals("Tile")){
                e = new EventHandlerImpl();
            
            }
            else if(s.equals("Table")){
                e = new EventHandlerImpl1();
            }
            return e;
        }
        
    };
     private static class AttachmentListCell extends ListCell<File> {
        String buildString(char c, int n) {
            char[] arr = new char[n];
            Arrays.fill(arr, c);
            return new String(arr);
        }
        
         @Override
        public void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
                
            } else {
                try {
                    
                    String space = " ";
                    BasicFileAttributes view = Files.getFileAttributeView(item.toPath(), BasicFileAttributeView.class).readAttributes();
                    //System.out.println(view.creationTime());
                    FileTime date = view.creationTime();
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String dateCreated = df.format(date.toMillis());
                        DecimalFormat df1 = new DecimalFormat("####.#");
                        Number n = (double)view.size()/(1024*1024);
                        Double d = n.doubleValue();
                        
                    if(item.getParent() != null){
                        if(item.isFile()){
                            Image fxImage = getFileIcon(item.toURI().toString());
                            ImageView imageView = new ImageView(fxImage);
                            setGraphic(imageView);
                        
                            setText("                    "+dateCreated+"                        "+df1.format(d)+"MB"+"                   "+item.getName());
                        }
                        else if(!item.isFile()){
                            Image image = new Image("filebrowser/janina.png");
                            ImageView imageView = new ImageView(image);
                            setGraphic(imageView);
                        
                            setText("                    "+dateCreated+"                        "+df1.format(d)+"MB"+"                   "+item.getName());
                        
                        }
                    }
                    else {
                        Image fxImage = new Image("filebrowser/_drive.png");
                        ImageView imageView = new ImageView(fxImage);
                        setGraphic(imageView);    
                        setText("                    "+dateCreated+"                        "+df1.format(d)+"MB"+"                   "+item.getAbsolutePath());
                    
                    }
                    
                    
                    } catch (IOException ex) {
                       
                        Image fxImage = new Image("filebrowser/_drive.png");
                        ImageView imageView = new ImageView(fxImage);
                        setGraphic(imageView);    
                        setText("                    "+"27/05/2015"+"                      "+"  0MB"+"                   "+item.getAbsolutePath());
                }
            }
        }
    }

       static HashMap<String, Image> mapOfFileExtToSmallIcon = new HashMap<>();

        private static String getFileExt(String fname) {
            String ext = ".";
            int p = fname.lastIndexOf('.');
            if (p >= 0) {
                ext = fname.substring(p);
            }
            return ext.toLowerCase();
        }

        private static javax.swing.Icon getJSwingIconFromFileSystem(File file) {

       
            FileSystemView view = FileSystemView.getFileSystemView();
            javax.swing.Icon icon = view.getSystemIcon(file);
       

            return icon;
        }

        private static Image getFileIcon(String fname) {
            final String ext = getFileExt(fname);

            Image fileIcon = mapOfFileExtToSmallIcon.get(ext);
            if (fileIcon == null) {

            javax.swing.Icon jswingIcon = null; 

            File file = new File(fname);
            //file.e
            if (file.exists()) {
                jswingIcon = getJSwingIconFromFileSystem(file);
            }
            else {
                File tempFile = null;
                try {
                    tempFile = File.createTempFile("icon", ext);
                    jswingIcon = getJSwingIconFromFileSystem(tempFile);
                }
                catch (IOException ignored) {
                    
                }
                finally {
                    if (tempFile != null) tempFile.delete();
                }
            }

            if (jswingIcon != null) {
                fileIcon = jswingIconToImage(jswingIcon);
                mapOfFileExtToSmallIcon.put(ext, fileIcon);
            }
        }

        return fileIcon;
    }

        private static Image jswingIconToImage(javax.swing.Icon jswingIcon) {
            BufferedImage bufferedImage = new BufferedImage(jswingIcon.getIconWidth(), jswingIcon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            jswingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
           
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }

    private class EventHandlerImpl implements EventHandler<ActionEvent> {

        public EventHandlerImpl() {
        }

        @Override
        public void handle(ActionEvent e) {
            //listviewFiles.getItems().clear();
            pan.getChildren().remove(s);
            pan.getChildren().remove(listviewFiles);
            pan.getChildren().remove(icon);
            pan.getChildren().remove(last);
            pan.getChildren().remove(size);
            pan.getChildren().remove(file);
            Flag = 1;
            //s.setContent(root);
            root.setPadding(new Insets(5,5,5,5));
            root.setHgap(5);
            root.setVgap(5);
         
            root.setMaxSize(600,500);
            //root.setPrefColumns(5);
            root.setVisible(true);
            root.autosize();
            
            //root.setBackground(Background.EMPTY);
           
            
            
            //root.getAlignment();
            //s.setHbarPolicy(ScrollBarPolicy.ALWAYS);
            //s.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            //pan = new AnchorPane();
            s.getChildrenUnmodifiable().remove(root);
            pan.getChildren().remove(s);
            //s.requestLayout();
            
            pan.getChildren().add(s);
            int i = 0,j=0,k=1;
            for (File f : data) {
                Label l = new Label();
                Label g = new Label();
                l.setText(f.getName());
                BasicFileAttributes view = null;
                try {
                    view = Files.getFileAttributeView(f.toPath(), BasicFileAttributeView.class).readAttributes();
                } catch (IOException ex) {
                    //Logger.getLogger(FXMLcontroller.class.getName()).log(Level.SEVERE, null, ex);
                }
                DecimalFormat df1 = new DecimalFormat("####.#");
                Number n = (double)view.size()/(1024);
                Double d = n.doubleValue();
                if(f.isFile())g.setText(df1.format(d)+"KB");
                if(f.getParent() != null){
                    if(f.isFile()){
                        Image fxImage = getFileIcon(f.getName());
                        ImageView imageView = new ImageView(fxImage);
                        //Image image = new Image(f.toURI().toString());
                        //ImageView imageView = new ImageView(image);
                        if(i<=5){
                            root.add(imageView, i, j);
                            root.add(l,i+1,j);
                            root.add(g,i+1,j+1);
                            i+=2;
                        }
                        else {i = 0;j+=2;}
                        s.setContent(root);
                    }else if(!f.isFile()) {
                        Image image = new Image("filebrowser/janina.png");
                        ImageView imageView = new ImageView(image);
                        if(i<=5){
                            root.add(imageView, i, j);
                            root.add(l,i+1,j);
                            root.add(g,i+1,j+1);
                            i+=2;
                        }
                        else {i = 0;j+=2;}
                        s.setContent(root);
                    }
                }else if(f.getParent() == null){
                    Label o = new Label();
                    o.setText(f.getAbsolutePath());
                    Image image = new Image("filebrowser/_drive.png");
                    ImageView imageView = new ImageView(image);
                    if(i<=5){
                        root.add(imageView, i, j);
                        root.add(o,i,k);
                        i++;
                    }
                    else {i = 0;j+=2;k+=2;}
                    s.setContent(root);
                    
                }
                //root.setAlignment(l,Pos.BOTTOM_CENTER);
                
                
            }
            root.setStyle( 
                   
                    "-fx-background-color: white;"
            );
        }
    }

    private class EventHandlerImpl1 implements EventHandler<ActionEvent> {

        public EventHandlerImpl1() {
        }

        @Override
        public void handle(ActionEvent e) {
            pan.getChildren().remove(s);
            pan.getChildren().add(listviewFiles);
            pan.getChildren().add(icon);
            pan.getChildren().add(last);
            pan.getChildren().add(size);
            pan.getChildren().add(file);
            //s = null;
        }
    }

    private static class CallbackImpl implements Callback<ListView<File>, ListCell<File>> {

        public CallbackImpl() {
        }

        @Override
        public ListCell<File> call(ListView<File> listviewFiles1) {
            return new AttachmentListCell();
        }
    }

    private class ChangeListenerImpl implements ChangeListener {

        public ChangeListenerImpl() {
        }

        @Override
        public void changed(ObservableValue observable, Object oldValue,
                Object newValue) {
            data.clear();
           
            TreeItem<File> selectedItem = (TreeItem<File>) newValue;
            //System.out.println("Selected Text : " + selectedItem.getValue());
            search.setText(selectedItem.getValue().toString());
            TreeItem<File> item1 = treeviewFileBrowse.getSelectionModel().getSelectedItem();
            //if(item1.getParent().getValue()==null) return ;
            item1.getChildren().stream().forEach(new ConsumerImpl());
            root.getChildren().clear();
            //s.setContent(root);
            //s.setHbarPolicy(ScrollBarPolicy.ALWAYS);
            //s.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            //pan = ;
            //pan.getChildren().remove(s);
            //pan.getChildren().add(s);
            s.getChildrenUnmodifiable().remove(root);
            //pan.getChildren().remove(s);
            if(Flag==1){
              // s = new ScrollPane();
                 //root.setHgrow(s, Priority.ALWAYS);
               // root = new GridPane();
                int i = 0,j=0,k=1;
                for (File f : data) {
                    Label l = new Label();
                     Label g = new Label();
                    l.setText(f.getName());
                    BasicFileAttributes view = null;
                try {
                    view = Files.getFileAttributeView(f.toPath(), BasicFileAttributeView.class).readAttributes();
                } catch (IOException ex) {
                    //Logger.getLogger(FXMLcontroller.class.getName()).log(Level.SEVERE, null, ex);
                }
                DecimalFormat df1 = new DecimalFormat("####.#");
                Number n = (double)view.size()/(1024);
                Double d = n.doubleValue();
                if(f.isFile())g.setText(df1.format(d)+"KB");
                    if(f.getParent() != null){
                        if(f.isFile()){
                            Image fxImage = getFileIcon(f.getName());
                            ImageView imageView = new ImageView(fxImage);
                            if(i<=5 ){
                            root.add(imageView, i, j);
                            root.add(l,i+1,j);
                            root.add(g,i+1,j+1);
                            i+=2;
                        }
                        else {i = 0;j+=2;}
                            root.setStyle("-fx-background-fill: white;");
                            s.setContent(root);
                        }else if(!f.isFile()) {
                            Image image = new Image("filebrowser/janina.png");
                            ImageView imageView = new ImageView(image);
                             if(i<=5 ){
                            root.add(imageView, i, j);
                            root.add(l,i+1,j);
                            root.add(g,i+1,j+1);
                            i+=2;
                        }
                        else {i = 0;j+=2;}
                            root.setStyle("-fx-background-fill: white;");
                            s.setContent(root);
                        }
                    }else if(f.getParent() == null){
                        Label o = new Label();
                        o.setText(f.getAbsolutePath());
                        Image image = new Image("filebrowser/_drive.png");
                        ImageView imageView = new ImageView(image);
                        if(i<=5){
                            root.add(imageView, i, j);
                            root.add(o,i,k);
                            i++;
                        }
                        else {i = 0;j+=2;k+=2;}
                        root.setStyle("-fx-background-fill: white;");
                        s.setContent(root);
                        
                    }
                }
                
            }
             root.setStyle( 
                   
                    "-fx-background-color: white;"
            );
        }

        private class ConsumerImpl implements Consumer<TreeItem<File>> {

            public ConsumerImpl() {
            
            }

            @Override
            public void accept(TreeItem<File> s) {
                //if(s.getValue().toString().equals("This PC")){}
                data.add(s.getValue());
            }
        }
    }
}  