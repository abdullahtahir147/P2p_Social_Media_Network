package com.example.dropboxtest.Objects;

public class Friend {
    private String Name;
    private String eMail;
    private String accountId;
    private String folderPath;
    private String folderId;
    private String ppPath;



    public String getAccountId(){
        return accountId;
    }
    public void setAccountId(String accountId){
        this.accountId=accountId;
    }

    public String getPpPath(){
        return ppPath;
    }
    public void setPpPath(String ppPath){
        this.ppPath=ppPath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String filePath) {
        this.folderPath = filePath;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }
}
