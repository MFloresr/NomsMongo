package mario;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private ListView<java.lang.String> listnomscat;
    @FXML
    private ListView<java.lang.String> listnomscas;

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

    public void initialize(){
        datecontrol.setOnAction(event -> {
            LocalDate date = datecontrol.getValue();
            String dia = String.valueOf(date.getDayOfMonth());
            String mes = String.valueOf(date.getMonth());
            String mescat =comprovarFecha(mes);
            String fecha = dia+" "+ mescat;

            String diaamb0 = null;
            String fecha2 = null;
            if(dia.length()==1){
                diaamb0 = "0"+dia;
                fecha2 = diaamb0 +" "+mescat;
            }
            if(!listnomscat.getItems().isEmpty() || !listnomscas.getItems().isEmpty()){
                listnomscat.getItems().clear();
                listnomscas.getItems().clear();
            }

            MongoClient mongoclient = new MongoClient();
            MongoDatabase db = mongoclient.getDatabase("cendrassos");
            MongoCollection<Document> col = db.getCollection("noms2");

            DBObject orquery = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            obj.add(new BasicDBObject("sants",fecha));
            obj.add(new BasicDBObject("sants",fecha2));
            orquery.put("$or",obj);
            FindIterable<Document> fechas = col.find((Bson) orquery);

            for (Document fe: fechas) {
                listnomscat.getItems().add((String) fe.get("catala"));
                listnomscas.getItems().add((String) fe.get("castella"));
            }
        });
    }

    @FXML
    public void BuscarNombre(Event event) {
        buscarporcataocasta();
    }

    @FXML
    public void  buscarCatala(Event event){
        if(checkcat.isSelected()){
            txtcat.setDisable(false);
            checkcas.setSelected(false);
            txtcas.setDisable(true);
            txtcas.setText("");
        }else{
            txtcat.setDisable(true);
        }
    }

    @FXML
    public void buscarCastella(Event event){
        if(checkcas.isSelected()){
            txtcas.setDisable(false);
            checkcat.setSelected(false);
            txtcat.setDisable(true);
            txtcat.setText("");
        }else{
            txtcas.setDisable(true);
        }
    }

    private String comprovarFecha(String mes) {
        if (mes.equals("JANUARY")) {
            return "de gener";
        } else if (mes.equals("FEBRUARY")) {
            return "de febrer";
            //// TODO: 22/04/2016
        } else if (mes.equals("MARCH")) {
            return "de març";
        } else if (mes.equals("APRIL")) {
            return "d'abril";
        } else if (mes.equals("MAY")) {
            return "de maig";
        } else if (mes.equals("JUNE")) {
            return "de juny";
        } else if (mes.equals("JULY")) {
            return "de juliol";
        } else if (mes.equals("AUGUST")) {
            return "d'agost";
        } else if (mes.equals("SEPTEMBER")) {
            return "de setembre";
        } else if (mes.equals("OCTOBER")) {
            return "d'octubre";
        } else if (mes.equals("NOVEMBER")) {
            return "de novembre";
        } else if (mes.equals("DECEMBER")) {
            return "de desembre";
        }
        return "";
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
                        listfechas.getItems().add("No contiene fecha");
                    }else {
                        listfechas.getItems().add(sants.get(i));
                    }
                }
            }
        }
    }
}
