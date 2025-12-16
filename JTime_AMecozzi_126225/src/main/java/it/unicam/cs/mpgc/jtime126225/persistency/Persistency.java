package it.unicam.cs.mpgc.jtime126225.persistency;

import it.unicam.cs.mpgc.jtime126225.model.*;

import java.util.ArrayList;

/**
 * Descrive i metodi per poter salvare o caricare i dati
 * da una fonte esterna
 */
public interface Persistency {
    /**
     * Salva un progetto all'interno della fonte esterna
     *
     * @param project il progetto da salvare
     */
    public void saveProject(Project project);

    /**
     * Salva una task all'interno del progetto
     *
     * @param task la task da salvare
     * @param project il progetto dentro cui salvare la task
     */
    public void saveTaskInProject(Task task,  Project project);

    /**
     * Aggiorna un progetto all'interno del file XML
     *
     * @param project il progetto da aggiornare
     */
    public void updateProject(Project project);

    /**
     * Aggiorna una task all'interno del file XML
     *
     * @param task la task da aggiornare
     * @param project il progetto dentro cui si trova la task da aggiornare
     */
    public void updateTaskInProject(Task task, Project project);

    /**
     * Carica i dati dalla fonte esterna
     *
     * @return la lista di progetti caricati
     */
    public ArrayList<Project> load();
}
