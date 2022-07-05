package com.assoc.file.management.utils;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.StringJoiner;

@Component
@Log
public class StringComponent {

    public static final String UNDER_SCORE="_";
    public static final String DOT="[.]";
    private StringComponent(){

    }
    public static String getWord(String string, int position){
        String[] t=string.split(UNDER_SCORE);
        return t[position];
    }
    public static String getSplitName(String name){
        StringJoiner sj=new StringJoiner(" ");
        String[] nameSplit=name.split("(?=\\p{Upper})");
        for(String s: nameSplit){
            sj.add(s);
        }
        return sj.toString();
    }
    public static String getLastWord(String string){
        String[] t=string.split(UNDER_SCORE);
        return t[t.length-1];
    }

    public static String getNP(String string){
        String[] nameSplit=string.split("-");
        if (nameSplit.length<2){
            return "Error";
        }
        nameSplit[0]=nameSplit[0].toLowerCase();
        String s=nameSplit[0].substring(0,1).toUpperCase()+nameSplit[0].substring(1);
        return s+nameSplit[1];
    }

    public static String removeExtension(String string){
        String [] s= string.split(DOT);
        return s[0];
    }
    public static String getWordAfterName(String string, int position){
        String[] t=string.split(UNDER_SCORE);
        StringJoiner sj=new StringJoiner(UNDER_SCORE);
        for(int i=position+2; i<t.length;i++)
            sj.add(t[i]);
        return sj.toString();
    }
}
