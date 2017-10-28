package filebrowser;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FilePathTreeItem extends TreeItem<File>{
    public static Image folderCollapseImage=new Image(ClassLoader.getSystemResourceAsStream("filebrowser/folder.png"));
    public static Image folderExpandImage=new Image(ClassLoader.getSystemResourceAsStream("filebrowser/folder-open.png"));
    public static Image fileImage=new Image(ClassLoader.getSystemResourceAsStream("filebrowser/text-x-generic.png"));
    private boolean isLeaf;
    private boolean isFirstTimeChildren=true;
    private boolean isFirstTimeLeaf=true;
    private final File file;
    public File getFile(){return(this.file);}
    private final String absolutePath;
    public String getAbsolutePath(){return(this.absolutePath);}
    private final boolean isDirectory;
    public boolean isDirectory(){return(this.isDirectory);}
    public String location;
    public String getLocation(){return location;}
    public FilePathTreeItem Curr(){return this;}
    public FilePathTreeItem(File file){
        super(file);
        this.file=file;
        this.absolutePath=file.getAbsolutePath();
        this.isDirectory=file.isDirectory();
        
        this.location = this.file.getAbsoluteFile().getName();
        if(this.isDirectory){
            this.setGraphic(new ImageView(folderCollapseImage));
            this.addEventHandler(TreeItem.branchCollapsedEvent(),new EventHandler(){
                @Override
                public void handle(Event e){
                    FilePathTreeItem source=(FilePathTreeItem)e.getSource();
                    if(!source.isExpanded()){
                        ImageView iv=(ImageView)source.getGraphic();
                        iv.setImage(folderCollapseImage);
                    }
                }
            } );
            this.addEventHandler(TreeItem.branchExpandedEvent(),new EventHandler(){
                @Override
                public void handle(Event e){
                    FilePathTreeItem source=(FilePathTreeItem)e.getSource();
                    if(source.isExpanded()){
                        ImageView iv=(ImageView)source.getGraphic();
                        iv.setImage(folderExpandImage);
                        TextField t = new TextField();
                        t.setText(location);
                        
                        
                    }   
                    
                }
                       
            } );
           
        }
        else{
            this.setGraphic(new ImageView(fileImage));
        }
      
    }
        
             
        

    @Override
    public ObservableList<TreeItem<File>> getChildren(){
        if(isFirstTimeChildren){
            isFirstTimeChildren=false;
            super.getChildren().setAll(buildChildren(this));
        }
        return(super.getChildren());
    }
    
    @Override
    public boolean isLeaf(){
        if(isFirstTimeLeaf){
            isFirstTimeLeaf=false;
            isLeaf=this.file.isFile();
        }
        return(isLeaf);
    }
 
    public ObservableList<FilePathTreeItem> buildChildren(FilePathTreeItem treeItem){
        File f=treeItem.getFile();
        
        if((f!=null)&&(f.isDirectory())){
            File[] files=f.listFiles();
            if (files!=null){
                ObservableList<FilePathTreeItem> children=FXCollections.observableArrayList();
                for(File childFile:files){
                    children.add(new FilePathTreeItem(childFile));
                    
                }
                return(children);
            }
        }
        return FXCollections.emptyObservableList();
    }

}