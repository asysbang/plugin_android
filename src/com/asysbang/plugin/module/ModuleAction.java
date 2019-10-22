package com.asysbang.plugin.module;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;


public class ModuleAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project= anActionEvent.getProject();
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                VirtualFile configFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl("https://raw.githubusercontent.com/asysbang/plugin_android/master/modules/module.json");
                boolean exists = configFile.exists();
                System.out.println("====="+exists);
                configFile.refresh(true, true, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("=====");
                        try {
                            InputStream inputStream = configFile.getInputStream();
//                            byte[] buff = new byte[1024];
//                            int len = -1;
//                            while ((len = inputStream.read(buff))!=-1) {
//                                System.out.println("===read==");
//                            }
                    BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while ((line = bis.readLine()) != null) {
                        System.out.println("===>>>"+line);
                    }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
//        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
//            @Override
//            public void run() {
//                VirtualFile configFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl("https://github.com/asysbang/plugin_android/blob/master/modules/module.json");
//                boolean exists = configFile.exists();
//                System.out.println("====="+exists);
//                try {
//                    InputStream inputStream = configFile.getInputStream();
//                    byte[] buff = new byte[1024];
//                    int len = -1;
//                    while ((len = inputStream.read(buff))!=-1) {
//                        System.out.println("===read==");
//                    }
////                    inputStream.read(buff);
////                    BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
////                    String line = null;
////                    while ((line = bis.readLine()) != null) {
////                        System.out.println("===>>>"+line);
////                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
