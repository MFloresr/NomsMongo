package mario;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.sun.xml.internal.txw2.Document;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Vipi on 18/04/2016.
 */
public class Controller {
    @FXML
    private TextField txtcat;
    @FXML
    private TextField txtcas;
    @FXML
    private TextArea txtAinfo;
    @FXML
    private ListView listfechas;
    @FXML
    private Button btnbuscar;
    @FXML
    private CheckBox checkcat;
    @FXML
    private CheckBox checkcas;
    @FXML
    public void BuscarNombre(Event event){
        String nomcastella= txtcas.getText();
        String nomcatala= txtcat.getText();
        MongoClient mongoclient = new MongoClient();
        MongoDatabase db = mongoclient.getDatabase("cendrassos");

        if(!nomcastella.isEmpty()){
            FindIterable<org.bson.Document> nombreCastella = db.getCollection("noms2").find(new org.bson.Document("castella",nomcastella));
            for (org.bson.Document coll: nombreCastella){
                txtcat.setText((String) coll.get("catala"));
                if(coll.containsKey("observacions")){
                    txtAinfo.setText( coll.getString("observacions"));
                }else{
                    txtAinfo.setText("");
                }
                if(!listfechas.getItems().isEmpty()){
                    listfechas.getItems().clear();
                }
                if(!coll.get("sants").equals("")){
                    System.out.println("Contiene valores");
                    ArrayList sants= (ArrayList) coll.get("sants");
                    System.out.println(sants.isEmpty());
                    for(int i =0;i<sants.size();i++){
                        System.out.println(sants.get(i));
                        listfechas.getItems().add(sants.get(i));
                    }
                }

            }
        }if(!nomcatala.isEmpty() ){
            FindIterable<org.bson.Document> nombreCatala = db.getCollection("noms2").find(new org.bson.Document("catala",nomcatala));
            for (org.bson.Document coll: nombreCatala){
                System.out.println(coll);
            }
        }




    }
    @FXML
    public void  buscarCatala(Event event){
        if(checkcat.isSelected()){
            System.out.println("Hello wordl");
            txtcat.setDisable(false);
            checkcas.setSelected(false);
            txtcas.setDisable(true);
            txtcas.setText("");
        }else{
            txtcat.setDisable(true);
        }
    }
    public void buscarCastella(Event event){
        if(checkcas.isSelected()){
            System.out.println("Hello wordlcas");
            txtcas.setDisable(false);
            checkcat.setSelected(false);
            txtcat.setDisable(true);
            txtcat.setText("");
        }else{
            txtcas.setDisable(true);
        }
    }
}
