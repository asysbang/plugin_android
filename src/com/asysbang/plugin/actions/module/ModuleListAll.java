package com.asysbang.plugin.actions.module;

import com.asysbang.plugin.bean.ModuleInfo;
import com.asysbang.plugin.bean.ModuleState;
import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 加载所有可用的module，并显示给用户
 */
public class ModuleListAll extends AnAction implements ModuleListPanel.SelectedCallback {

    private static final String CONFIG_URL = "https://raw.githubusercontent.com/asysbang/plugin_android/master/modules/module.json";

    private ModuleState mState;
    private Project mProject;
    private String basePath;
    private ModuleInfo mSelectedModule;


    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        mProject = anActionEvent.getProject();
        basePath = mProject.getBasePath();
        loadConfig();
        showPopupList(anActionEvent);
    }

    //官方config  和  本地~/.asysbang/module.json 两部分组成
    private void loadConfig() {
        VirtualFile configFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(CONFIG_URL);
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = configFile.getInputStream();
            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bis.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //parser json string
        Gson gson = new Gson();
        mState = gson.fromJson(sb.toString(), ModuleState.class);
        System.out.println("===getVersion=" + mState.getVersion());
        ModuleInfo[] modules = mState.getModules();
        for (ModuleInfo m : modules) {
            System.out.println("====" + m.getUrl());
        }
    }

    private JBPopup mModulePopup;
    private void showPopupList(AnActionEvent anActionEvent) {
        ModuleListPanel panel = new ModuleListPanel(mState,this);
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, panel.getList());
        mModulePopup = popupBuilder.setCancelOnWindowDeactivation(true).createPopup();
        JComponent component = (JComponent) anActionEvent.getInputEvent().getSource();
        mModulePopup.showCenteredInCurrentWindow(anActionEvent.getProject());
    }

    @Override
    public void selected(ModuleInfo info) {
        mSelectedModule = info;
        mModulePopup.dispose();
        downloadModule();
    }

    private void addSettings(File settingsFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(settingsFile));
            String line = null;
            // 内存流, 作为临时流
            CharArrayWriter tempStream = new CharArrayWriter();
            while ((line = br.readLine()) != null) {
                //应该用正则表达式
                if (line.trim().startsWith("include")) {
                    line += " ,':" + mSelectedModule.getName() + "'";
                    tempStream.write(line);
                    tempStream.append(System.getProperty("line.separator"));
                }
            }
            br.close();
            FileWriter out = new FileWriter(settingsFile);
            tempStream.writeTo(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addGradle(File gradleFile) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(gradleFile);
            Node setNode = doc.getElementsByTagName("set").item(0);
            Element newOption = doc.createElement("option");
            newOption.setAttribute("value", "$PROJECT_DIR$" + File.separator + mSelectedModule.getName());
            setNode.appendChild(newOption);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // 10、创建 Transformer 对象
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            // 文档字符编码
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            // 11、创建 DOMSource 对象
            DOMSource domSource = new DOMSource(doc);
            // 12、创建 StreamResult 对象
            StreamResult reStreamResult = new StreamResult(gradleFile);
            transformer.transform(domSource, reStreamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addModules(File modulesFile) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(modulesFile);
            Node modulesNode = doc.getElementsByTagName("modules").item(0);
            Element newModule = doc.createElement("module");
            newModule.setAttribute("fileurl", "file://$PROJECT_DIR$" + File.separator + mSelectedModule.getName()+File.separator+mSelectedModule.getName()+".iml");
            newModule.setAttribute("filepath", "$PROJECT_DIR$" + File.separator + mSelectedModule.getName()+File.separator+mSelectedModule.getName()+".iml");
            System.out.println("====addModules : " + modulesFile.getAbsolutePath());
            modulesNode.appendChild(newModule);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // 10、创建 Transformer 对象
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            // 文档字符编码
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            // 11、创建 DOMSource 对象
            DOMSource domSource = new DOMSource(doc);
            // 12、创建 StreamResult 对象
            StreamResult reStreamResult = new StreamResult(modulesFile);
            transformer.transform(domSource, reStreamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disposeModule() {
        File settingsFile = new File(basePath + File.separator + "settings.gradle");
        addSettings(settingsFile);
        File gradleFile = new File(basePath + File.separator + ".idea" + File.separator + "gradle.xml");
        addGradle(gradleFile);
        File modulesFile = new File(basePath + File.separator + ".idea" + File.separator + "modules.xml");
        addModules(modulesFile);
        //同步项目目录
        mProject.getBaseDir().refresh(false, true);
        //重新编译新的module

    }

    private void downloadModule() {
        //不确定怎么直接解压virtualfile， 所以先复制到本地目录
        VirtualFile zipFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(mSelectedModule.getUrl());
        System.out.println("===>>>getNameWithoutExtension:" + mSelectedModule.getName());
        zipFile.refresh(false, false, new Runnable() {
            @Override
            public void run() {
                try {
                    //copy
                    String root = System.getProperty("user.home");
                    File targetFile = new File(root + "/.asysbang/" + zipFile.getName());
                    if (!targetFile.exists()) {
                        InputStream fis = zipFile.getInputStream();
                        FileOutputStream fos = new FileOutputStream(targetFile.getAbsoluteFile());
                        int len = -1;
                        byte[] buf = new byte[1024 * 4];
                        while ((len = fis.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            fos.flush();
                        }
                        fos.close();
                        fis.close();
                    }
                    System.out.println("=====>>>" + targetFile.getAbsolutePath());
                    //unzip
                    String moduleRoot = basePath + File.separator + zipFile.getNameWithoutExtension();
                    ZipFile files = new ZipFile(targetFile);
                    Enumeration<? extends ZipEntry> entries = files.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        String name = entry.getName();
                        File newFile = new File(moduleRoot + File.separator + entry.getName());
                        if (entry.isDirectory()) {
                            newFile.mkdirs();
                        } else {
                            InputStream is = files.getInputStream(entry);
                            OutputStream os = new FileOutputStream(newFile);
                            byte[] buf1 = new byte[1024 * 4];
                            int len = -1;
                            while ((len = is.read(buf1)) != -1) {
                                os.write(buf1, 0, len);
                                os.flush();
                            }
                            os.close();
                            is.close();
                        }
                    }
                    //部署有问题，需要关闭在重新打开项目
                    disposeModule();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //应该在文件全部处理之后再弹出，需要知道怎么线程之间通信
        Messages.showMessageDialog("添加成功，等待项目刷新之后，关闭项目，再重新打开项目","新模块",Messages.getInformationIcon());
    }
}
