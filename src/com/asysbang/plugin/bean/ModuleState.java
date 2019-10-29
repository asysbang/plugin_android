package com.asysbang.plugin.bean;

public class ModuleState {

    public void setVersion(String version) {
        this.version = version;
    }

    public void setModules(ModuleInfo[] modules) {
        this.modules = modules;
    }

    public String getVersion() {
        return version;
    }

    public ModuleInfo[] getModules() {
        return modules;
    }

    private String version;

    private ModuleInfo[] modules;
}
