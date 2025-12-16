package it.unicam.cs.mpgc.jtime126225.model;

import java.util.ArrayList;

/**
 * Rappresenta un tipo generale di progetto. Un progetto
 * è composto da una serie di Task e da uno status, dipendente
 * dallo status di tutte le sue task
 */
public class Project {
    private int id;
    private String name;
    private String description;
    private Status status;
    ArrayList<Task> tasks;

    //private XmlPersistency persistency; // serve per salvare le task

    /**
     * Crea un progetto a partire da alcune informazioni
     *
     * @param id l'id del progetto
     * @param name il nome del progetto
     * @param description la descrizione del progetto
     */
    public Project(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status=Status.IN_PROGRESS;
        this.tasks=new ArrayList<Task>();
    }

    /**
     * Crea un progetto tramite tutte le sue informazioni
     *
     * @param id l'id del progetto
     * @param name il nome del progetto
     * @param description la descrizione del progetto
     * @param status lo stato del progetto
     * @param tasks la lista di task contenute nel progetto
     */
    public Project(int id, String name, String description, Status status, ArrayList<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status=status;
        this.tasks=tasks;
    }

    /// Metodi getter
    public int getId() { return this.id; }
    public String getName(){ return this.name; }
    public String getDescription(){ return this.description; }
    public Status getStatus(){ return this.status; }
    public ArrayList<Task> getTasks(){ return this.tasks; }


    /**
     * Aggiunge una task alla lista delle task del progetto
     *
     * @param task la task da aggiungere alla lista
     */
    public void addTask(Task task){ this.tasks.add(task); }

    /**
     * Restituisce la task richiesta
     *
     * @param hash l'hashcode della task da restituire
     * @return Task la task richiesta
     */
    public Task getTask(int hash){
        for(Task t:this.tasks){
            if(t.hashCode()==hash){
                return t;
            }
        }
        return null;
    }

    /**
     * Completa una task del progetto
     *
     * @param hashCode l'hashCode della task da completare
     */
    public void completeTask(int hashCode){
        for(Task t:tasks){
            if(t.hashCode()==hashCode){
                t.completeTask();
            }
        }
    }

    /**
     * Controlla se il progetto è stato completato
     *
     * @return true se il progetto è completato, false altrimenti
     */
    public boolean isComplete(){ return (this.status == Status.COMPLETE)?true:false; }

    /**
     * Completa il progetto in base allo stato delle task
     * che contiene
     */
    public boolean completeProject(){
        if(!isComplete()){
            boolean complete=true;
            for(Task t:tasks){
                if(!t.isComplete()){
                    complete=false;
                }
            }

            if(complete){
                this.status=Status.COMPLETE;
            }
            return complete;
        }
        return true;
    }


    /**
     * Restituisce il numero di task completate nel progetto
     *
     * @retur int il numero di task completate
     */
    public int completedTasks() {
        int completed=0;
        for(Task t:tasks){
            if(t.isComplete()){
                completed++;
            }
        }
        return completed;
    }

    /**
     * Restituisce il numero di task non completate nel progetto
     *
     * @return int il numero di task non completate
     */
    public int notCompletedTasks() {
        return totalTasks()-completedTasks();
    }

    /**
     * Restituisce il numero di task nel progetto
     *
     * @return int il numero di task
     */
    public int totalTasks() {
        return this.tasks.size();
    }


    /**
     * Controlla che un id sia già presente nel progetto
     *
     * @param id l'id da controllare
     * @return true se l'id è presente, false altrimenti
     */
    public boolean idIsIn(int id) {
        for(Task task:tasks){
            if(id==task.getId()){
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo per calcolare l'hashcode del progetto, utilizzato
     * per riconoscere i progetti
     */
    public int hashCode(){
        return this.id*this.name.toLowerCase().hashCode()*7;
    }
}
