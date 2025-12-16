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
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Salva e carica i dati attraverso un file xml esterno
 */
public class XmlPersistency implements Persistency {
    private String xmlFile;

    public XmlPersistency() {
        this.xmlFile = "src/main/resources/persistencyProjects.xml";
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
        try{
            Document doc=getDocument();
            NodeList projects=doc.getElementsByTagName("project");

            for(int i=0;i<projects.getLength();i++){
                Element projectEl=(Element)projects.item(i);
                if(Integer.parseInt(projectEl.getAttribute("hash"))==project.hashCode()){
                    projectEl.getElementsByTagName("id").item(0).setTextContent(String.valueOf(project.getId()));
                    projectEl.getElementsByTagName("name").item(0).setTextContent(project.getName());
                    projectEl.getElementsByTagName("description").item(0).setTextContent(project.getDescription());
                    projectEl.getElementsByTagName("status").item(0).setTextContent(project.getStatus().name());
                    break;
                }
            }
            writeDocument(doc);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTaskInProject(Task task, Project project) {
        try{
            Document doc=getDocument();
            NodeList projects=doc.getElementsByTagName("project");

            for(int i=0;i<projects.getLength();i++){
                Element projectEl=(Element)projects.item(i);
                if(Integer.parseInt(projectEl.getAttribute("hash"))==project.hashCode()){
                    Element tasksEl=(Element) projectEl.getElementsByTagName("tasks").item(0);
                    NodeList taskNodes=tasksEl.getChildNodes();
                    for(int j=0;j<taskNodes.getLength();j++){
                        if(taskNodes.item(j) instanceof Element taskEl){
                            Element taskElement=(Element) taskNodes.item(j);
                            if(Integer.parseInt(taskElement.getAttribute("hash"))==task.hashCode()){

                                taskElement.getElementsByTagName("id").item(0).setTextContent(String.valueOf(task.getId()));
                                taskElement.getElementsByTagName("name").item(0).setTextContent(task.getName());
                                taskElement.getElementsByTagName("description").item(0).setTextContent(task.getDescription());
                                taskElement.getElementsByTagName("status").item(0).setTextContent(task.getStatus().name());
                                taskElement.getElementsByTagName("startTime").item(0).setTextContent(task.getStartTime().toString());
                                taskElement.getElementsByTagName("endTime").item(0).setTextContent(task.getEndTime().toString());
                                taskElement.getElementsByTagName("estimatedDuration").item(0).setTextContent(String.valueOf(task.getEstimatedDuration()));
                                taskElement.getElementsByTagName("realDuration").item(0).setTextContent(String.valueOf(task.getRealDuration()));

                                break;
                            }
                        }
                    }
                    break;
                }
            }
            writeDocument(doc);
            //TODO implementare
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Project> load() {
        ArrayList<Project> projects=new ArrayList<Project>();

        try{
            File file=new File(xmlFile);
            if(!file.exists()){
                return projects;
            }

            DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc=builder.parse(file);

            NodeList projectNodes=doc.getElementsByTagName("project");

            for(int i=0;i<projectNodes.getLength();i++){
                Element projectEl=(Element)projectNodes.item(i);

                int id=Integer.parseInt(projectEl.getElementsByTagName("id").item(0).getTextContent());
                String name=projectEl.getElementsByTagName("name").item(0).getTextContent();
                String description=projectEl.getElementsByTagName("description").item(0).getTextContent();
                Status status=Status.valueOf(projectEl.getElementsByTagName("status").item(0).getTextContent());

                ArrayList<Task> taskList=new ArrayList<Task>();

                Element tasksEl=(Element)projectEl.getElementsByTagName("tasks").item(0);
                NodeList taskNodes=tasksEl.getChildNodes();

                for(int j=0;j<taskNodes.getLength();j++){
                    if(taskNodes.item(j) instanceof Element taskEl){
                        Element taskElement=(Element) taskNodes.item(j);

                        int taskId=Integer.parseInt(taskElement.getElementsByTagName("id").item(0).getTextContent());
                        String taskName=taskElement.getElementsByTagName("name").item(0).getTextContent();
                        String taskDesc=taskElement.getElementsByTagName("description").item(0).getTextContent();
                        Status taskStatus=Status.valueOf(taskElement.getElementsByTagName("status").item(0).getTextContent());
                        LocalDateTime taskStart=LocalDateTime.parse(taskElement.getElementsByTagName("startTime").item(0).getTextContent());

                        LocalDateTime taskEnd=(taskElement.getElementsByTagName("endTime").item(0).getTextContent().isEmpty())?
                                null:LocalDateTime.parse(taskElement.getElementsByTagName("endTime").item(0).getTextContent());

                        Long taskEstDur=Long.parseLong(taskElement.getElementsByTagName("estimatedDuration").item(0).getTextContent());
                        Long taskRealDur=Long.parseLong(taskElement.getElementsByTagName("realDuration").item(0).getTextContent());

                        Task createdTask=new Task(taskId, taskName, taskDesc, taskStatus, taskStart, taskEnd, taskEstDur, taskRealDur);
                        taskList.add(createdTask);
                    }
                }

                Project  project=new Project(id, name, description, status, taskList);
                projects.add(project);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return projects;
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
            removeEmptyTextNodes(doc.getDocumentElement());

            Transformer transformer= TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");
            transformer.setOutputProperty(OutputKeys.METHOD,"xml");

            doc.getDocumentElement().normalize();

            transformer.transform(new DOMSource(doc), new StreamResult(new File(this.xmlFile)));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo ricorsivo per la formattazione del file XML:
     * rimuove le righe vuote tra un tag e l'altro
     *
     * @param node il nodo da cui rimuovere le righe vuote
     */
    private void removeEmptyTextNodes(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                if (child.getTextContent().trim().isEmpty()) {
                    node.removeChild(child);
                }
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyTextNodes(child);
            }
        }
    }
}
