package it.unicam.cs.mpgc.jtime126225.model;

import it.unicam.cs.mpgc.jtime126225.persistency.*;

import java.util.ArrayList;

/**
 * Classe per la gestione di tutti quanti i progetti. Permette di
 * crearne nuovi o chiuderli
 */
public class Manager {
    private ArrayList<Project> projects;
    private static Manager manager;
    private Persistency persistency;

    /**
     * Costruisce il manager
     */
    private Manager(){
        persistency=new XmlPersistency();
        this.projects=persistency.load();
    }

    /**
     * Crea l'istanza del manager (se non esiste)
     * e la restitusice
     */
    public static Manager getManager(){
        if(manager==null){
            manager=new Manager();
        }
        return manager;
    }

    public ArrayList<Project> getProjects(){
        return this.projects;
    }

    /**
     * Aggiunge un progetto alla lista di progetti
     *
     * @param createdProject progetto da inserire
     */
    public void addProject(Project createdProject){
        this.projects.add(createdProject);
        persistency.saveProject(createdProject);
        //TODO fare in modo che salvi su xml
    }

    /**
     * Completa un progetto nella lista
     *
     * @param hash l'hashCode del progetto da completare
     */
    public boolean closeProject(int hash){
        for(Project p:this.projects){
            if(p.hashCode()==hash){
                boolean complete=p.completeProject();
                if(complete){
                    persistency.updateProject(p);
                }
                return complete;
            }
        }
        return false;
    }

    /**
     * Restituisce il numero totale di progetti
     *
     * @return int il numero totale di progetti
     */
    public int totalProjects(){
        return this.projects.size();
    }

    /**
     * Restituisce il numero di progetti completati
     *
     * @return int il numero di progetti completati
     */
    public int completedProjects() {
        int  completedProjects=0;
        for(Project p:this.projects){
            if(p.isComplete()){
                completedProjects++;
            }
        }
        return completedProjects;
    }

    /**
     * Restituisce il numero di progetti non completati
     *
     * @return int il numero di progetti non completati
     */
    public int notCompletedProjects() {
        return totalProjects()-completedProjects();
    }

    /**
     * Controlla che nella lista ci sia un progetto con
     * un determinato id
     *
     * @param id l'id di cui controllare la presenta
     * @return true se è presente, false altrimenti
     */
    public boolean idIsIn(int id) {
        for(Project p:this.projects){
            if(p.getId()==id){
                return true;
            }
        }
        return false;
    }

    /**
     * Inserisce una task in un progetto, riconosciuto tramite il suo hashCode
     *
     * @param createdTask la task da inserire nel progetto
     * @param hashCode l'hashCode del progetto dentro cui
     *                 inserire la task
     */
    public void addTaskInProject(Task createdTask, int hashCode) {
        for(Project project:this.projects){
            if(hashCode == project.hashCode()){
                project.addTask(createdTask);
                persistency.saveTaskInProject(createdTask, project);
            }
        }
    }

    /**
     * Permette di completare una task all'interno del progetto
     * identificato dall'hashCode
     *
     * @param taskHashCode l'hashCode della task da completare
     * @param projHashCode l'hashCode del progetto dove completare
     *                 la task
     */
    public void completeTaskInProject(int taskHashCode, int projHashCode) {
        for(Project p:this.projects){
            if(p.hashCode()==projHashCode){
                p.completeTask(taskHashCode);
                persistency.updateTaskInProject(p.getTask(taskHashCode),p);
            }
        }
    }
}
