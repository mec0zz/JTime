package it.unicam.cs.mpgc.jtime126225.persistency;

import it.unicam.cs.mpgc.jtime126225.model.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/**
 * Salva e carica i dati attraverso un file xml esterno
 */
public class XmlPersistency implements Persistency {
    private String xmlFile;

    public XmlPersistency() {
        this.xmlFile = "src/main/java/it/unicam/cs/mpgc/jtime126225/persistencyProjects.xml";
    }

    @Override
    public void saveProject(Project project) {
        try{
            Document doc=getDocument();

            Element root=doc.getDocumentElement();

            Element projectEl=doc.createElement("project");

            projectEl.setAttribute("hash",String.valueOf(project.hashCode()));

            appendElement(doc, projectEl, "id", project.getId());
            appendElement(doc, projectEl, "name", project.getName());
            appendElement(doc, projectEl, "description", project.getDescription());
            appendElement(doc, projectEl, "status", project.getStatus().name());


            projectEl.appendChild(doc.createElement("tasks"));

            root.appendChild(projectEl);

            writeDocument(doc);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveTaskInProject(Task task, Project project){
        try{
            Document doc=getDocument();

            NodeList projects=doc.getElementsByTagName("project");

            for(int i=0;i<projects.getLength();i++){
                Element projectEl=(Element)projects.item(i);

                if(project.hashCode()==Integer.parseInt(projectEl.getAttribute("hash"))){
                    Element tasksEl=(Element) projectEl.getElementsByTagName("tasks").item(0);

                    Element taskEl=doc.createElement("task");
                    taskEl.setAttribute("hash",String.valueOf(task.hashCode()));

                    appendElement(doc, taskEl, "id", task.getId());
                    appendElement(doc, taskEl, "name", task.getName());
                    appendElement(doc, taskEl, "description", task.getDescription());
                    appendElement(doc, taskEl, "status", task.getStatus().name());
                    appendElement(doc, taskEl, "startTime",  task.getStartTime().toString());

                    appendElement(doc, taskEl, "endTime",  (task.getEndTime()==null)
                            ?"":task.getEndTime().toString());

                    appendElement(doc, taskEl, "estimatedDuration", task.getEstimatedDuration());
                    appendElement(doc, taskEl, "realDuration", task.getRealDuration());

                    tasksEl.appendChild(taskEl);
                }
            }

            writeDocument(doc);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateProject(Project project) {
        //TODO implementare
    }

    @Override
    public void updateTaskInProject(Task task, Project project) {
        //TODO implementare
    }

    @Override
    public ArrayList<Project> load() {
        //TODO implementare
        return null;
    }

    /**
     * Restituisce il documento per la persistenza; se non
     * esiste, lo crea
     *
     * @return Document il documento per la persistenza XML
     */
    public Document getDocument(){
        try{
            File file=new File(this.xmlFile);

            DocumentBuilder builder= DocumentBuilderFactory.newInstance().newDocumentBuilder();

            if(!file.exists()){
                Document doc=builder.newDocument();
                Element root=doc.createElement("projects");
                doc.appendChild(root);
                return doc;
            }

            return builder.parse(file);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Crea un nodo XML all'interno del documento
     *
     * @param doc il documento dove creare il nodo XML
     * @param parent il padre del nodo XML che deve essere creato
     * @param tagName il nome del tag che deve essere creato
     * @param value il valore che deve essere inserito nel nodo
     */
    public void appendElement(Document doc, Element parent, String tagName, Object value){
        Element el=doc.createElement(tagName);
        el.setTextContent(String.valueOf(value));
        parent.appendChild(el);
    }

    /**
     * Scrive il contenuto del documento XML sul file
     *
     * @param doc il documento da cui scrivere
     */
    public void writeDocument(Document doc){
        try{
            Transformer transformer= TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT,"yes");

            transformer.transform(
                    new DOMSource(doc),
                    new StreamResult(new File(this.xmlFile))
            );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
