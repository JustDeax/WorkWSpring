package com.justdeax.StringCalc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("ALL")
@RestController
public class CalculatorContoller {
    ExpressionParser calc = new ExpressionParser();

    @GetMapping("/calculator")
    public String calculating(@RequestParam(value = "expr", defaultValue = "0") String expression) {
        List<String> a = calc.parse(expression);
        StringBuilder str = new StringBuilder();
        for (String i : a) {
            str.append(i);
        }
        str.append(" > ");
        str.append(calc.calc(calc.parse(expression)));
        return str.toString();
    }
}
