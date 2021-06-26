package com.hs.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.Icons;
import org.apache.commons.lang.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * @author ：河神
 * @date ：Created in 2021/6/25 11:47 下午
 */
public class SelectWindow {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;

    private JTextArea textContent;
    private JButton formatButton;
    private JButton unFormatButton;
    private JButton transferredMeaning;
    private JButton unTransferredMeaning;

    private Gson unFormatGson = new Gson();
    private Gson formatGson = new GsonBuilder().setPrettyPrinting().create();;


    private Project project;
    private ToolWindow toolWindow;

    public SelectWindow(Project project,ToolWindow toolWindow) {
        this.project = project;
        this.toolWindow = toolWindow;
        formatButton.addActionListener(e -> format());
        unFormatButton.addActionListener(e-> unFormat());
        transferredMeaning.addActionListener(e-> transferredMeaning());
        unTransferredMeaning.addActionListener(e-> unTransferredMeaning());
    }

    public JPanel getContent() {
        return panel1;
    }

    public void init(){
        textContent.paste();
        format();
    }

    public void copyAll(){
        textContent.selectAll();
        textContent.copy();
    }


    public void transferredMeaning(){
        String text = textContent.getText();
        JsonObject jsonObject = unFormatGson.fromJson(text, JsonObject.class);
        String toJson = unFormatGson.toJson(jsonObject);
        String escapeJava = StringEscapeUtils.escapeJava(toJson);
        textContent.setText(escapeJava);

        copyAll();
    }

    public void unTransferredMeaning(){
        String text = textContent.getText();
        String unescapeJava = StringEscapeUtils.unescapeJava(text);
        textContent.setText(unescapeJava);

        copyAll();
    }

    public void format() {
        String text = textContent.getText();
        boolean flag = true;
        try {
            JsonObject jsonObject = formatGson.fromJson(text, JsonObject.class);
            String toJson = formatGson.toJson(jsonObject);
            textContent.setText(toJson);
        }catch (JsonSyntaxException jsonSyntaxException){
            flag = false;
        }

        if (!flag&&text.contains("\\")){
            try {
                String unescapeJava = StringEscapeUtils.unescapeJava(text);
                JsonObject jsonObject = formatGson.fromJson(unescapeJava, JsonObject.class);
                String toJson = formatGson.toJson(jsonObject);
                textContent.setText(toJson);
                flag = true;
            }catch (JsonSyntaxException jsonSyntaxException){
                flag = false;
            }
        }

        if (!flag){
            Messages.showMessageDialog( project,"JSON格式错误", "JSON格式化", Icons.DELETE_ICON);
        }

        copyAll();
    }

    public void unFormat(){
        try {
            String text = textContent.getText();
            JsonObject jsonObject = unFormatGson.fromJson(text, JsonObject.class);
            String toJson = unFormatGson.toJson(jsonObject);
            textContent.setText(toJson);
        }catch (JsonSyntaxException jsonSyntaxException){
            Messages.showMessageDialog(project,  "JSON压缩失败","JSON格式化", Icons.DELETE_ICON);
        }

        copyAll();
    }

}
