package mario;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

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
    private ListView<java.lang.String> listfechas;
    @FXML
    private ListView<java.lang.String> listnoms;
    @FXML
    private Button btnbuscar;
    @FXML
    private CheckBox checkcat;
    @FXML
    private CheckBox checkcas;
    @FXML
    private DatePicker datecontrol;

    java.lang.String idioma;
    java.lang.String nombreAbuscar;
    private void comprovarFecha(String mes){
        if(mes.equals("JANUARY")){
            mes = "de gener";
        }else if (mes.equals("FEBRUARY")){
            //// TODO: 22/04/2016  
        }
    }
    public void initialize(){
        datecontrol.setOnAction(event -> {
            LocalDate date = datecontrol.getValue();
            String dia = String.valueOf(date.getDayOfMonth());
            Month mes = date.getMonth();
            System.out.println(date.getMonth());
            MongoClient mongoclient = new MongoClient();
            MongoDatabase db = mongoclient.getDatabase("cendrassos");
            String fecha = "03 de febrer";

            if(dia.length()== 1){
                dia= "0"+dia;
            }
            String fecha2 = dia+ " de febrer";
            System.out.println(fecha2);
            FindIterable<org.bson.Document> fechas = db.getCollection("noms2").find(new org.bson.Document("sants",fecha2));
            if(listnoms.getItems().isEmpty()){
                listnoms.getItems().clear();
            }
            for(org.bson.Document fe : fechas){
                String cat = (String) fe.get("catala");
                listnoms.getItems().add(cat);
            }
        });
    }

    private void buscarporcataocasta(){
        if(checkcas.isSelected()){
            idioma = checkcas.getText().toLowerCase();
            nombreAbuscar = txtcas.getText();
        }if(checkcat.isSelected()){
            idioma = checkcat.getText().toLowerCase();
            nombreAbuscar = txtcat.getText();
        }

        MongoClient mongoclient = new MongoClient();
        MongoDatabase db = mongoclient.getDatabase("cendrassos");

        if(!idioma.isEmpty()){
            FindIterable<org.bson.Document> personas = db.getCollection("noms2").find(new org.bson.Document(idioma,nombreAbuscar));
            for (org.bson.Document persona : personas){
                if(persona.containsKey("observacions")){
                    txtAinfo.setText( persona.getString("observacions"));
                }else{
                    txtAinfo.setText("No hay observaciones");
                }
                if(!listfechas.getItems().isEmpty()){
                    listfechas.getItems().clear();
                }
                ArrayList<java.lang.String> sants= (ArrayList<java.lang.String>) persona.get("sants");
                for (int i = 0; i<sants.size();i++){
                    if(sants.get(i).length() == 0){
                        System.out.println("vacio ");
                        listfechas.getItems().add("No contiene fecha");
                    }else {
                        listfechas.getItems().add(sants.get(i));
                    }
                }
            }
        }
    }
    @FXML
    public void BuscarNombre(Event event) {
        buscarporcataocasta();
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
