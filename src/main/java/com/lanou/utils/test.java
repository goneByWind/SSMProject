package com.lanou.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dllo on 17/10/24.
 */
public class test {

    @Test
    public void test1() throws ParseException {
        String idcardNo = "210302199903062413";
        String sub = idcardNo.substring(6, 10)+"-"+idcardNo.substring(10, 12)+"-"+idcardNo.substring(12, 14)+" 00:00:00";
        SimpleDateFormat birthDate =new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = birthDate.parse(sub);

        System.out.println(date);
    }

    @Test
    public void test2(){
        String string1 = new String (",100,105");
        for (int i=0;i<string1.length()/4;i++){
            String substring2 = string1.substring(i * 4 + 1, (i + 1) * 4 );
            int i1 = Integer.parseInt(substring2);
            System.out.println(i1);
        }
    }
}
