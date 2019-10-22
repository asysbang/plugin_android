package com.asysbang.plugin;

import com.intellij.openapi.components.ApplicationComponent;

public class AppComponent implements ApplicationComponent {

    @Override
    public void initComponent() {

        String root = System.getProperty("user.home");
        System.out.println("===========initComponent==="+root);
    }

    @Override
    public void disposeComponent() {
        System.out.println("===========disposeComponent===");
    }
}

