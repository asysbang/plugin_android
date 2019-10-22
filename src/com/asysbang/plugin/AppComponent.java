package com.asysbang.plugin;

import com.asysbang.plugin.module.ModuleHelper;
import com.intellij.openapi.components.ApplicationComponent;

public class AppComponent implements ApplicationComponent {

    @Override
    public void initComponent() {

        String root = System.getProperty("user.home");
        System.out.println("===========initComponent==="+root);
        ModuleHelper.getInstance().loadConfig();
    }

    @Override
    public void disposeComponent() {
        System.out.println("===========disposeComponent===");
    }

    //https://github.com/asysbang/plugin_android/blob/master/modules/module.json
    private void loadModulesFromGithub() {

    }
}

