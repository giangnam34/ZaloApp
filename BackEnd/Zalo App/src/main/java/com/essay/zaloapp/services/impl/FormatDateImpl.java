package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.services.FormatDate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FormatDateImpl implements FormatDate {

    @Override
    public String formatDate(Date date){
        String result = "";
        for (int i = 0; i < date.toString().length(); i++){
            if (date.toString().charAt(i) != '.'){
                if (date.toString().charAt(i) != ' ')
                    result += date.toString().charAt(i);
                else
                    result += 'T';
            }
            else break;
        }
        return result;
    }

}
