package com.asysbang.plugin.module;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import gherkin.deps.com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ModuleHelper {

    public MoBeans getCacheBean() {
        return mCacheBean;
    }

    private MoBeans mCacheBean;

    private static final String CONFIG_URL = "https://raw.githubusercontent.com/asysbang/plugin_android/master/modules/module.json";

    private Runnable configReadRunnable = new Runnable() {
        @Override
        public void run() {
            VirtualFile configFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(CONFIG_URL);
            configFile.refresh(true, true, new Runnable() {
                @Override
                public void run() {
                    try {
                        StringBuilder sb = new StringBuilder();
                        InputStream inputStream = configFile.getInputStream();
                        BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bis.readLine()) != null) {

                            sb.append(line);
                        }
                        parserModules(sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private List<MoBean> parserModules(String jsonStr) {
        System.out.println(jsonStr);
        Gson gson = new Gson();
        mCacheBean = gson.fromJson(jsonStr, MoBeans.class);
        System.out.println("===getVersion="+mCacheBean.getVersion());
        MoBean[] beans = mCacheBean.getModules();
        for (MoBean m:beans) {
            System.out.println("===="+m.getUrl());
        }
        return null;
    }

    public void loadConfig() {
        ApplicationManager.getApplication().invokeLater(configReadRunnable);
    }

    private ModuleHelper() {

    }

    private static class InstanceHolder {
        private static ModuleHelper sInst = new ModuleHelper();
    }

    public static ModuleHelper getInstance() {
        return InstanceHolder.sInst;
    }
}
