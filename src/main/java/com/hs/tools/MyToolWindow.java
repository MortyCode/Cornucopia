package com.hs.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.Icons;
import com.intellij.util.ui.ColorIcon;
import org.apache.commons.lang.StringEscapeUtils;

import javax.swing.*;

/**
 * @author ：河神
 * @date ：Created in 2021/6/25 3:43 下午
 */
public class MyToolWindow {

    private JPanel myToolWindowContent;
    private JTextArea textContent;
    private Project project;

    private JButton formatButton;
    private JButton unFormatButton;
    private JButton transferredMeaning;
    private JButton unTransferredMeaning;

    private Gson unFormatGson = new Gson();
    private Gson formatGson = new GsonBuilder().setPrettyPrinting().create();;


    public MyToolWindow(Project project) {
        this.project = project;

        formatButton.addActionListener(e -> format());
        unFormatButton.addActionListener(e-> unFormat());
        transferredMeaning.addActionListener(e-> transferredMeaning());
        unTransferredMeaning.addActionListener(e-> unTransferredMeaning());
    }


    public void transferredMeaning(){
        String text = textContent.getText();
        JsonObject jsonObject = unFormatGson.fromJson(text, JsonObject.class);
        String toJson = unFormatGson.toJson(jsonObject);
        String escapeJava = StringEscapeUtils.escapeJava(toJson);
        textContent.setText(escapeJava);
    }

    public void unTransferredMeaning(){
        String text = textContent.getText();
        String unescapeJava = StringEscapeUtils.unescapeJava(text);
        textContent.setText(unescapeJava);
    }

    public void format() {
        try {
            String text = textContent.getText();
            JsonObject jsonObject = formatGson.fromJson(text, JsonObject.class);
            String toJson = formatGson.toJson(jsonObject);
            textContent.setText(toJson);
        }catch (JsonSyntaxException jsonSyntaxException){
            Messages.showMessageDialog( project,"JSON格式化失败", "JSON格式化",Icons.DELETE_ICON);
        }
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
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }



}
