package com.assoc.file.management.utils;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Log
public class StringComponent {

    private StringComponent(){

    }
    public static String getWord(String string, int position){
        String[] t=string.split("_");
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
}
