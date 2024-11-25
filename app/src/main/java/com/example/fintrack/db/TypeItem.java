package com.example.fintrack.db;

public class TypeItem {
    int id;

    String typename;
    int imageid; // selected
    int simageid; // not selected

    int kind;

    public TypeItem(int id, String typename, int imageid, int simageid, int kind) {
        this.id = id;
        this.typename = typename;
        this.imageid = imageid;
        this.simageid = simageid;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public int getSimageid() {
        return simageid;
    }

    public void setSimageid(int simageid) {
        this.simageid = simageid;
    }
}