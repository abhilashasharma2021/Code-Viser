package com.codeviser.Model;

public class HomeModel {
    private String userimage;
    private String name;
    private String path;
    private String lastMsg;
    private String Id;
    private String help_status;
    private String status;

    public String getHelp_status() {
        return help_status;
    }

    public void setHelp_status(String help_status) {
        this.help_status = help_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private String groupID;
    private String lastTime;

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    private String lastDate;


}
