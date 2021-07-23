package com.galaxymerchantrading.handlingtransaction.controller;

import com.galaxymerchantrading.handlingtransaction.dao.DashoboardService;
import com.galaxymerchantrading.handlingtransaction.domain.*;
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

        model.addAttribute("data", new RequestBody());
        model.addAttribute("listOption",data);
        model.addAttribute("comodity",comodities);
        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String processLoad(@ModelAttribute RequestBody request, Model model) {
        Result result                   = new Result();
        Boolean isStatement = false;
        Boolean isQuestion = false;
       String lastChar = checkInput(request.getInput());
       if(lastChar.equals("?")){
           isQuestion = true;
       }else{
           processInputStatement(request.getInput());
       }

//        for(int i=0;i<arrCode.length; i++){
//            for(RomanNumericConfig val:data){
//                if(val.getNumName().equals(arrCode[i])){
//                    numCode.add(val.getNumCode());
//                }
//            }
//        }

//        for(Comodity com:comodities){
//            if(com.getId().equals(transaction.getComodityId())){
//                comodityValue   = com.getComodityValue();
//                comodityName    = com.getComodityName();
//            }
//        }
//        if(numCode.size() == arrCode.length){
//            arabicFormat = this.convertRomanToInteger(numCode);
//        }
//        if(arabicFormat != null){
            result.setResultTransaction(request.getInput());
//            result.setResultName(comodityName);
//            result.setResultSum((long) (arabicFormat * comodityValue));
            model.addAttribute("result", result);
            return "result";
//        }else{
//            return "error";
//        }

    }

    public String checkInput(String input){
        char lastChar = input.charAt(input.length()-1);
        return String.valueOf(lastChar);
    }

//    public void processInputQuestion(String){
//
//    }

    public void processInputStatement(String input){
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
            System.err.println("length array "+ arrInput.length);
            int index1 = -1;
            String identify1 = "is";
            for (int i=0;i<arrInput.length;i++) {
                if (arrInput[i].equals(identify1)) {
                    index1 = i;
                    break;
                }
            }
            Integer valueA = null;
            try {
                valueA = Integer.parseInt(arrInput[index1+1]);
            }catch (NumberFormatException ex){
                ex.printStackTrace();
            }
            String identify2 = arrInput[index1-1];
            if(!dashoboardService.checkDataExist("intergalactic_unit_config","intergalactic_unit_name",identify2)){
                ArrayList<String> arrRomanNumeral       = new ArrayList<>();
                Integer arabicValue = null;
               for(int i=0; i<index1-1; i++){
                   arrRomanNumeral.add(arrInput[i]);
               }
               if(arrRomanNumeral.size() == assembleRomanNumeral(arrRomanNumeral).size()){
                   arabicValue = this.convertRomanToInteger(assembleRomanNumeral(arrRomanNumeral));
                  if(arabicValue != null){
                      double valuePerUnit = valueA/arabicValue;
                      Comodity comodity = new Comodity();
                      comodity.setComodityName(identify2);
                      comodity.setComodityValue(valuePerUnit);
                      dashoboardService.deleteComodityByName(identify2);
                      dashoboardService.updateComodity(comodity);
                  }
               }
//               System.err.println("roman numeral "+numCode);
            }
        }
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
