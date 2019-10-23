package com.asysbang.plugin;

import com.asysbang.plugin.module.ModuleHelper;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.roots.ProjectRootManager;

import java.io.File;

public class AppComponent implements ApplicationComponent {

    /**
     * 检查插件需要的根路径，以及文件lock状态
     */
    private Runnable verifyRootDirRunnable = new Runnable() {
        @Override
        public void run() {
//            ProjectRootManager.getInstance(null).getContentSourceRoots();
            String root = System.getProperty("user.home");
            File f = new File(root + "/.asysbang/.");
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    };

    @Override
    public void initComponent() {
        ApplicationManager.getApplication().runWriteAction(verifyRootDirRunnable);
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

