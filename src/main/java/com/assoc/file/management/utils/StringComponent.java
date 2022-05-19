package com.assoc.file.management.utils;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

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

    public static String removeExtension(String string){
        String [] s= string.split(DOT);
        return s[0];
    }
}
