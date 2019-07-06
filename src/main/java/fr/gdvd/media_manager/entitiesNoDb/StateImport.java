package fr.gdvd.media_manager.entitiesNoDb;

import lombok.ToString;

@ToString
public class StateImport {

    public StateImport() {
        this.alreadyExisteInDB = 0;
        this.updateInDB = 0;
        this.addtoDB = 0;
    }

    public StateImport(int alreadyExisteInDB, int updateInDB, int addtoDB) {
        this.alreadyExisteInDB = alreadyExisteInDB;
        this.updateInDB = updateInDB;
        this.addtoDB = addtoDB;
    }

    private int alreadyExisteInDB;
    private int updateInDB;
    private int addtoDB;

    public int getAlreadyExisteInDB() {
        return alreadyExisteInDB;
    }

    public void setAlreadyExisteInDB(int alreadyExisteInDB) {
        this.alreadyExisteInDB = alreadyExisteInDB;
    }

    public int getUpdateInDB() {
        return updateInDB;
    }

    public void setUpdateInDB(int updateInDB) {
        this.updateInDB = updateInDB;
    }

    public int getAddtoDB() {
        return addtoDB;
    }

    public void setAddtoDB(int addtoDB) {
        this.addtoDB = addtoDB;
    }

    public void addOneAlreadyExisteInDB(){
        this.alreadyExisteInDB++;
    }
    public void addOneUpdateInDB(){
        this.updateInDB++;
    }
    public void addOneAddtoDB(){
        this.addtoDB++;
    }

}
