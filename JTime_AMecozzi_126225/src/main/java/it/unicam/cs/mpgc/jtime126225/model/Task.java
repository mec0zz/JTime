package it.unicam.cs.mpgc.jtime126225.model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Rappresenta una task con le sue informazioni: id, nome,
 * descrizione, status, data di inizio e fine, durata stimata (in ore)
 * e durata effettiva (in ore)
 */
public class Task{
    private int id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long estimatedDuration;
    private long realDuration;

    /**
     * Crea una task a partire da determinate informazioni
     */
    public Task(int id, String name, String description, long estimatedDuration){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status=Status.IN_PROGRESS;
        this.startTime = LocalDateTime.now();
        this.endTime = null;
        this.estimatedDuration = estimatedDuration;
        this.realDuration = -1;
    }

    /// Metodi getter
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public Status getStatus(){return this.status;}
    public LocalDateTime getStartTime(){
        return this.startTime;
    }
    public LocalDateTime getEndTime(){
        return this.endTime;
    }
    public long getEstimatedDuration(){
        return this.estimatedDuration;
    }
    public long getRealDuration(){
        return this.realDuration;
    }

    /**
     * Controlla se la task è stata completata
     *
     * @return true se la task è completa, false altrimenti
     */
    public boolean isComplete(){
        return (this.status == Status.COMPLETE)?true:false;
    }

    /**
     * Completa la task, definendo tutte le informazioni interne
     */
    public void completeTask(){
        if(this.status == Status.IN_PROGRESS){
            this.endTime = LocalDateTime.now();
            this.realDuration = Duration.between(this.startTime, this.endTime).toHours();
            this.status = Status.COMPLETE;
        }
    }

    /**
     * Calcola un hashCode univoco per la task
     *
     * @return int l'hashCode della task
     */
    public int hashCode(){
        return this.id*this.name.toLowerCase().hashCode()*7;
    }
}
