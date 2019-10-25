package com.asysbang.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
 * 测试 下载一个module
 */
public class MainMenuAction extends AnAction {

    private static final String TEST_URL = "https://github.com/asysbang/plugin_android/blob/master/modules/hellomodule.zip?raw=true";

    private String basePath;

    private String mModuleName;

    private Project mProject;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        mProject = anActionEvent.getProject();
        basePath = mProject.getBasePath();
        downloadModuleZip();
//        Messages.showMessageDialog("Add Successful","New Module",Messages.getInformationIcon());
    }

    private void downloadModuleZip() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                //不确定怎么直接解压virtualfile， 所以先复制到本地目录
                VirtualFile zipFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(TEST_URL);
                mModuleName = zipFile.getNameWithoutExtension();
                System.out.println("===>>>getNameWithoutExtension:" + mModuleName);
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
                            disposeModule();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                //应该在文件全部处理之后再弹出，需要知道怎么线程之间通信
                Messages.showMessageDialog("Add Successfully, please close project and re-open project after 5 seconds","New Module",Messages.getInformationIcon());
            }
        });
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
                    line += " ,':" + mModuleName + "'";
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
            newOption.setAttribute("value", "$PROJECT_DIR$" + File.separator + mModuleName);
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
            newModule.setAttribute("fileurl", "file://$PROJECT_DIR$" + File.separator + mModuleName+File.separator+mModuleName+".iml");
            newModule.setAttribute("filepath", "$PROJECT_DIR$" + File.separator + mModuleName+File.separator+mModuleName+".iml");
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
}
