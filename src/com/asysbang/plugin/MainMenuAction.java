package com.asysbang.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.ex.http.HttpFileSystem;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 测试 下载一个module
 */
public class MainMenuAction extends AnAction {

    private static final String TEST_URL = "https://github.com/asysbang/plugin_android/blob/master/modules/hellomodule.zip?raw=true";

    private String basePath;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project= anActionEvent.getProject();
        basePath = project.getBasePath();
        VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileListener() {
            @Override
            public void propertyChanged(@NotNull VirtualFilePropertyEvent event) {
                System.out.println("===>>>propertyChanged");
            }

            @Override
            public void contentsChanged(@NotNull VirtualFileEvent event) {
                System.out.println("===>>>contentsChanged");
            }

            @Override
            public void fileCreated(@NotNull VirtualFileEvent event) {
                System.out.println("===>>>fileCreated");
            }

            @Override
            public void fileDeleted(@NotNull VirtualFileEvent event) {
                System.out.println("===>>>fileDeleted");
            }

            @Override
            public void fileMoved(@NotNull VirtualFileMoveEvent event) {
                System.out.println("===>>>fileMoved");
            }

            @Override
            public void fileCopied(@NotNull VirtualFileCopyEvent event) {
                System.out.println("===>>>fileCopied");
            }

            @Override
            public void beforePropertyChange(@NotNull VirtualFilePropertyEvent event) {
                System.out.println("===>>>beforePropertyChange");
            }

            @Override
            public void beforeContentsChange(@NotNull VirtualFileEvent event) {
                System.out.println("===>>>beforeContentsChange");
            }

            @Override
            public void beforeFileDeletion(@NotNull VirtualFileEvent event) {
                System.out.println("===>>>beforeFileDeletion");
            }

            @Override
            public void beforeFileMovement(@NotNull VirtualFileMoveEvent event) {
                System.out.println("===>>>beforeFileMovement");
            }
        });
        System.out.println("===>>>zipFile");
        VirtualFile zipFile = HttpFileSystem.getInstance().refreshAndFindFileByPath(TEST_URL);
        boolean downloaded = HttpFileSystem.getInstance().isFileDownloaded(zipFile);
        System.out.println("===>>>zipFile:"+downloaded);
        zipFile.refresh(true,true);

//        downloadModuleZip();
//        disposeModule();
//        Messages.showMessageDialog("Add Successful","New Module",Messages.getInformationIcon());
    }

    private void downloadModuleZip() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                //不确定怎么直接解压virtualfile， 所以先复制到本地目录
                VirtualFile zipFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(TEST_URL);
                System.out.println("===>>>zipFile:"+zipFile.getName());

                zipFile.refresh(false, false, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //copy
                            System.out.println("===>>>zipFile:"+zipFile.getLength());
                            String root = System.getProperty("user.home");
                            File targetFile = new File(root+"/.asysbang/"+zipFile.getName());
                            if (!targetFile.exists()) {
                                System.out.println("===>>>targetFile:"+targetFile.getAbsolutePath());
                                BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream()));
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)))  ;
                                String line = null;
                                while ((line = br.readLine())!=null) {
                                    bw.write(line);
                                    bw.flush();
                                }
                                bw.flush();
                                bw.close();
                                br.close();
                            }
                            System.out.println("=====>>>"+targetFile.getAbsolutePath());
//                            //unzip
//                            ZipFile files = new ZipFile(targetFile);
//                            Enumeration<? extends ZipEntry> entries = files.entries();
//                            while (entries.hasMoreElements()) {
//                                System.out.println("=====entries");
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }
                });
            }
        });
    }

    private void disposeModule() {

    }
}
