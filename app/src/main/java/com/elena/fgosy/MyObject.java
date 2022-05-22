package com.elena.fgosy;

import java.io.Serializable;

//класс объекта по варианту и его поля

public class MyObject  implements Serializable {
    int Id;
    String ObjectName;
    int ObjectNumber;
    String ObjectInfo;

    public MyObject(int id, String objectName, int objectNumber, String objectInfo){
        this.Id = id;
        this.ObjectName = objectName;
        this.ObjectNumber = objectNumber;
        this.ObjectInfo = objectInfo;
    }

    public int getId(){
        return Id;
    }

     public void setId(int id){
        this.Id = id;
     }

    public String getObjectName(){
        return ObjectName;
    }

    public void setObjectName(String objectName){
        this.ObjectName = objectName;
    }

    public int getObjectNumber(){
        return ObjectNumber;
    }

    public void setObjectNumber(int objectNumber){
        this.ObjectNumber = objectNumber;
    }

    public String getObjectInfo(){
        return ObjectInfo;
    }

    public void setObjectInfo(String objectIfo){
        this.ObjectInfo = objectIfo;
    }
}
