package com.galaxymerchantrading.handlingtransaction.controller;

import com.galaxymerchantrading.handlingtransaction.dao.DashoboardService;
import com.galaxymerchantrading.handlingtransaction.domain.Comodity;
import com.galaxymerchantrading.handlingtransaction.domain.Result;
import com.galaxymerchantrading.handlingtransaction.domain.RomanNumericConfig;
import com.galaxymerchantrading.handlingtransaction.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class DashboardController {
    @Autowired
    DashoboardService dashoboardService;

    @GetMapping("/dashboard")
    public String load(Model model) {
        List<RomanNumericConfig> data = dashoboardService.get();
        List<Comodity> comodities = dashoboardService.getComodity();

        model.addAttribute("data", new Transaction());
        model.addAttribute("listOption",data);
        model.addAttribute("comodity",comodities);
        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String processLoad(@ModelAttribute Transaction transaction, Model model) {
        Result result                   = new Result();
        List<RomanNumericConfig> data   = dashoboardService.get();
        List<Comodity> comodities       = dashoboardService.getComodity();
        ArrayList<String> numCode       = new ArrayList<>();
        Double comodityValue            = null;
        String comodityName             = "";
        Integer arabicFormat            = null;
        String[] arrCode = transaction.getCode().split(" ");
        for(int i=0;i<arrCode.length; i++){
            for(RomanNumericConfig val:data){
                if(val.getNumName().equals(arrCode[i])){
                    numCode.add(val.getNumCode());
                }
            }
        }

        for(Comodity com:comodities){
            if(com.getId().equals(transaction.getComodityId())){
                comodityValue   = com.getComodityValue();
                comodityName    = com.getComodityName();
            }
        }
        if(numCode.size() == arrCode.length){
            arabicFormat = this.convertRomanToInteger(numCode);
        }
        if(arabicFormat != null){
            result.setResultTransaction(transaction.getCode());
            result.setResultName(comodityName);
            result.setResultSum((long) (arabicFormat * comodityValue));
            model.addAttribute("result", result);
            return "result";
        }else{
            return "error";
        }

    }

    public Integer convertRomanToInteger(ArrayList<String> romanStr){
        String romanNumeral = "";
        for(int i=0; i<romanStr.size(); i++){
            romanNumeral += romanStr.get(i);
        }
        int size = romanNumeral.length();
        romanNumeral = romanNumeral + " ";
        int result = 0;

        for (int i = 0; i < size; i++) {
            char ch   = romanNumeral.charAt(i);
            char next_char = romanNumeral.charAt(i+1);
            if (ch == 'M') {
                result += 1000;
                if(next_char == 'M'){
                    result += 1000;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'M') {
                        result += 1000;
                        i++;
                        next_char = romanNumeral.charAt(i+1);
                        if(next_char == 'M'){
                            return null;
                        }
                    }
                }
            } else if (ch == 'I') {
                if (next_char == 'X') {
                    result += 9;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'I'){
                        return null;
                    }else if(next_char == 'X'){
                        return null;
                    }else if(next_char == 'V'){
                        return null;
                    }else if(next_char == 'M'){
                        return null;
                    } else if(next_char == 'L'){
                        return null;
                    }else if(next_char == 'C'){
                        return null;
                    }else if(next_char == 'D'){
                        return null;
                    }else{
                        return result;
                    }
                } else if (next_char == 'V') {
                    result += 4;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'I'){
                        return null;
                    }else if(next_char == 'X'){
                        return null;
                    }else if(next_char == 'V'){
                        return null;
                    }else if(next_char == 'M'){
                        return null;
                    }else if(next_char == 'L'){
                        return null;
                    }else if(next_char == 'C'){
                        return null;
                    }else if(next_char == 'D'){
                        return null;
                    }else{
                        return result;
                    }
                }else if (next_char == 'I') {
                    result += 1;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'L'){
                        return null;
                    }else if(next_char == 'M') {
                        return null;
                    }else if(next_char == 'I'){
                        result += 1;
                        i++;
                        next_char = romanNumeral.charAt(i+1);
                        if(next_char == 'I'){
                            return null;
                        }else{
                            result++;
                        }
                    }else{
                        result++;
                    }
                } else if(next_char == 'L'){
                    return null;
                }else if(next_char == 'M'){
                    return null;
                }else{
                    result++;
                }
            } else if (ch == 'C') {
                if (next_char == 'M') {
                    result += 900;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'M'){
                        return null;
                    }else if(next_char == 'D') {
                        return null;
                    }
                } else if (next_char == 'D') {
                    result += 400;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'M'){
                        return null;
                    }else if(next_char == 'D') {
                        return null;
                    }else if(next_char == 'C') {
                        return null;
                    }
                }else if (next_char == 'C') {
                    result += 100;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                   if(next_char == 'D') {
                        return null;
                    }else if(next_char == 'M') {
                        return null;
                    }else if(next_char == 'C'){
                        result += 100;
                        i++;
                        next_char = romanNumeral.charAt(i+1);
                        if(next_char == 'C'){
                            return null;
                        }else if(next_char == 'M'){
                           return null;
                       }else if(next_char == 'D'){
                            return null;
                        }else{
                            result += 100;
                        }
                    }else{
                        result += 100;
                    }
                } else {
                    result += 100;
                }
            } else if (ch == 'D') {
                if (next_char == 'D') {
                    return null;
                }else if (next_char == 'M') {
                    return null;
                }else{
                    result += 500;
                }
            } else if (ch == 'X') {
                if (next_char == 'C') {
                    result += 90;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'L'){
                        return null;
                    }else if(next_char == 'C'){
                        return null;
                    }else if(next_char == 'M'){
                        return null;
                    }else if(next_char == 'X'){
                        return null;
                    }else if(next_char == 'D'){
                        return null;
                    }
                } else if (next_char == 'L') {
                    result += 40;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'L'){
                        return null;
                    }else if(next_char == 'C'){
                        return null;
                    }else if(next_char == 'M'){
                        return null;
                    }else if(next_char == 'X'){
                        return null;
                    }else if(next_char == 'D'){
                        return null;
                    }
                }else if (next_char == 'X') {
                    result += 10;
                    i++;
                    next_char = romanNumeral.charAt(i+1);
                    if(next_char == 'L'){
                        return null;
                    }else if(next_char == 'C') {
                        return null;
                    }else if(next_char == 'D') {
                        return null;
                    }else if(next_char == 'M') {
                        return null;
                    }else if(next_char == 'X'){
                        result += 10;
                        i++;
                        next_char = romanNumeral.charAt(i+1);
                        if(next_char == 'X'){
                            return null;
                        }else{
                            result += 10;
                        }
                    }else{
                        result++;
                    }
                }else if(next_char == 'M'){
                    return null;
                }else if(next_char == 'D'){
                    return null;
                } else {
                    result += 10;
                }
            } else if (ch == 'L') {
                if(next_char == 'L'){
                    return null;
                }else if(next_char == 'C'){
                    return null;
                }else if(next_char == 'D'){
                    return null;
                }else if(next_char == 'M'){
                    return null;
                }else{
                    result += 50;
                }
            } else if (ch == 'V'){
                if(next_char == 'V'){
                    return null;
                }else if(next_char == 'X'){
                    return null;
                }else if(next_char == 'L'){
                    return null;
                }else if(next_char == 'C'){
                    return null;
                }else if(next_char == 'D'){
                    return null;
                }else if(next_char == 'M'){
                    return null;
                }else{
                    result += 5;
                }
            }
        }
        return result;
    }
}
