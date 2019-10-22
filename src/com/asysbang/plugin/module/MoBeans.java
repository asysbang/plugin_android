package com.asysbang.plugin.module;

public class MoBeans {


    private String version;
    private MoBean[] modules;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public MoBean[] getModules() {
        return modules;
    }

    public void setBeans(MoBean[] modules) {
        this.modules = modules;
    }
}
