package com.galaxymerchantrading.handlingtransaction.controller;

import com.galaxymerchantrading.handlingtransaction.dao.DashoboardService;
import com.galaxymerchantrading.handlingtransaction.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class DashboardController {
    @Autowired
    DashoboardService dashoboardService;

    @GetMapping("/dashboard")
    public String load(Model model) {
        List<IntergalacticUnitConfig> units = dashoboardService.getIntergalacticUnit();
        List<Comodity> comodities = dashoboardService.getComodity();

        model.addAttribute("data", new RequestBody());
        model.addAttribute("units",units);
        model.addAttribute("comodity",comodities);
        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String processLoad(@ModelAttribute RequestBody request, Model model) {
        Result result                   = new Result();
        String resp = "";
        String firstStr = checkFirstString(request.getInput());
        String lastStr = checkLastString(request.getInput());
        if(lastStr.equals("?") || firstStr.toLowerCase().equals("how")){
           resp = processInputQuestion(request.getInput());
        }else{
           resp = processInputStatement(request.getInput());
        }
        result.setResultTransaction(resp);
        model.addAttribute("result", result);
        return "result";

    }

    public String checkLastString(String input){
        char lastChar = input.charAt(input.length()-1);
        return String.valueOf(lastChar);
    }
    public String checkFirstString(String input){
        input = input.replaceAll("\\s+"," ").trim();
        String[] arrInput = input.split(" ");
        return arrInput[0];
    }

    public String processInputQuestion(String input){
        List<String> arrLst = new ArrayList<>();
        Double comodityValue  = null;
        String newStr = input.toLowerCase().replace("how","")
                        .replace("many","")
                        .replace("much","")
                        .replace("credits","")
                        .replace("credit","")
                        .replaceAll("\\?","");
        newStr = newStr.replaceAll("\\s+"," ").trim();
        String[] newStrArr = newStr.split(" ");
        for (int i=0;i<newStrArr.length;i++) {
            arrLst.add(newStrArr[i]);
            if (newStrArr[i].equals("is")) {
                if(i == newStrArr.length){
                    arrLst.remove(i-1);
                }else{
                    arrLst.remove(i);
                }

            }
        }

        if(dashoboardService.checkDataExist("comodity","comodity_name",arrLst.get(arrLst.size()-1))){
            Comodity comodity =  dashoboardService.findComodityByName(arrLst.get(arrLst.size()-1));
            comodityValue = comodity.getComodityValue();
            arrLst.remove(arrLst.size()-1);
            ArrayList<String> arrRomanNumeral       = new ArrayList<>();
            String resultStr = "";
            Integer arabicValue = null;
            for(int i=0; i<arrLst.size(); i++){
                arrRomanNumeral.add(arrLst.get(i));
                resultStr += arrLst.get(i) +" ";
            }
            if(assembleRomanNumeral(arrRomanNumeral).size() == arrLst.size()){
                arabicValue = this.convertRomanToInteger(assembleRomanNumeral(arrRomanNumeral));
                if(arabicValue != null){
                    Double totalValue = arabicValue * comodityValue;
                    BigDecimal bigTotalValue = new BigDecimal(totalValue);
                    return resultStr+comodity.getComodityName()+" is "+bigTotalValue+" Credits";
                }else{
                    return "Invalid input format";
                }
            }else{
                return "I have no idea what you are talking about";
            }
        }else{
            ArrayList<String> arrRomanNumeral       = new ArrayList<>();
            String resultStr = "";
            Integer arabicValue = null;
            for(int i=0; i<arrLst.size(); i++){
                arrRomanNumeral.add(arrLst.get(i));
                resultStr += arrLst.get(i) +" ";
            }
            if(assembleRomanNumeral(arrRomanNumeral).size() == arrLst.size()){
                arabicValue = this.convertRomanToInteger(assembleRomanNumeral(arrRomanNumeral));
                if(arabicValue != null){
                    return resultStr+" is "+arabicValue;
                }else{
                    return "Invalid input format";
                }
            }else{
                return "I have no idea what you are talking about";
            }
        }


    }

    public String processInputStatement(String input){
        input = input.replaceAll("\\s+"," ").trim();
        String[] arrInput = input.split(" ");
        if(arrInput.length == 3){
            if(arrInput[1].equalsIgnoreCase("is")){
                if(dashoboardService.checkDataExist("roman_numeric_config","num_code",arrInput[2].trim())){
                    IntergalacticUnitConfig unitConfig = new IntergalacticUnitConfig();
                    unitConfig.setInterGalacticUnitName(arrInput[0].trim());
                    unitConfig.setRomanNumeral(arrInput[2].trim());
                    dashoboardService.deleteIntergalacticUnitByName(unitConfig.getInterGalacticUnitName().toLowerCase());
                    dashoboardService.deleteIntergalacticUnitByRomanNumeral(unitConfig.getRomanNumeral());
                    dashoboardService.updateIntergalacticUnits(unitConfig);
                }
            }

        }else if(arrInput.length > 3){
            Integer valueGross = null;
            int index = -1;
            for (int i=0;i<arrInput.length;i++) {
                if (arrInput[i].toLowerCase().equals("is")) {
                    index = i;
                    break;
                }
            }
            try {
                valueGross = Integer.parseInt(arrInput[index+1]);
            }catch (NumberFormatException ex){
                ex.printStackTrace();
            }
            String identify2 = arrInput[index-1];
            if(!dashoboardService.checkDataExist("intergalactic_unit_config","intergalactic_unit_name",identify2)){
                ArrayList<String> arrRomanNumeral       = new ArrayList<>();
                Integer arabicValue = null;
               for(int i=0; i<index-1; i++){
                   arrRomanNumeral.add(arrInput[i]);
               }
               if(arrRomanNumeral.size() == assembleRomanNumeral(arrRomanNumeral).size()){
                   arabicValue = this.convertRomanToInteger(assembleRomanNumeral(arrRomanNumeral));
                  if(arabicValue != null){
                      double valuePerUnit = (double) valueGross/arabicValue;
                      Comodity comodity = new Comodity();
                      comodity.setComodityName(identify2);
                      comodity.setComodityValue(valuePerUnit);
                      dashoboardService.deleteComodityByName(identify2);
                      dashoboardService.updateComodity(comodity);
                  }
               }
            }
        }
        return input;
    }

    public ArrayList<String> assembleRomanNumeral(ArrayList<String> romanNumeral){
        List<IntergalacticUnitConfig> getUnit   = dashoboardService.getIntergalacticUnit();
        ArrayList<String> assembleStr = new ArrayList<>();
        for(int i=0;i<romanNumeral.size(); i++){
            for(IntergalacticUnitConfig val:getUnit){
                if(val.getInterGalacticUnitName().equals(romanNumeral.get(i))){
                    assembleStr.add(val.getRomanNumeral());
                }
            }
        }
        return assembleStr;
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
                        result += 10;
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
