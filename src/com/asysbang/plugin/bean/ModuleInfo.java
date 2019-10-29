package com.asysbang.plugin.bean;


/**
 * 名字需要修改，表示每一个module信息
 */
public class ModuleInfo {

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getMd5() {
        return md5;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    private String name;
    private String version;
    private String url;
    private String md5;
}
