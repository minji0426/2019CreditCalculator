package com.example.creditcalculator;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFileReader {
    public static List<String> preprocessingFile(File name) throws IOException {
        List<String> retStr = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(name.toString()));
        String line;
        while ((line = in.readLine()) != null) {
// Split line on tab.
            //String s;
            String[] parts = line.split("\t");
            for (String part : parts) {
                retStr.add(part);
            }
        }
        in.close();
        return retStr;
    }

    public static InfoMemory readDataFromFile(File filepath) throws IOException {
        List<String> ret = preprocessingFile(filepath);
        InfoMemory info = new InfoMemory();

        Pattern subNo = Pattern.compile("^[A-Z]{2}[0-9]{6}$");// 학수번호 정규식
        Pattern grade = Pattern.compile("^[A-Z]+[0+-]*$");//성적(A+,B+) 정규식
        Pattern credit = Pattern.compile("^[0-9]{1}$"); //이수학점 정규식

        for(int i = 0;i<ret.size();i++){
            Matcher m_subNo = subNo.matcher(ret.get(i));
            Matcher m_grade = grade.matcher(ret.get(i));
            Matcher m_credit = credit.matcher(ret.get(i));

            if(m_subNo.find() && i> 85) {
                info.setSubNo(ret.get(i));
            }
            if(m_grade.find() && i> 85) {
                info.setGrade(ret.get(i));
            }
            if(m_credit.find() && i> 85) {
                info.setCredit(ret.get(i));
            }
        }

        return info;
    }
}

class InfoMemory{
    private String[] subNo = new String[100];
    private String[] grade = new String[100] ;
    private String[] credit = new String[100];
    private int i,j,k = 0;

    void setSubNo(String subNo){
        this.subNo[i] = subNo;
        i++;
    }

    void setGrade(String grade){
        this.grade[j] = grade;
        j++;
    }

    void setCredit(String credit){
        this.credit[k] = credit;
        k++;
    }

    public String[] getSubNo() {
        return subNo;
    }

    public String[] getGrade() {
        return grade;
    }

    public String[] getCredit() {
        return credit;
    }
}