package com.github.ScipioAM.scipio_utils_javafx.util;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Class: NumberTextFieldOperator
 * Description: 只允许数字输入
 * Author: Alan Min
 * Create Date: 2020/8/17
 */
public class NumericTextFieldOperator implements UnaryOperator<TextFormatter.Change> {

    private final int lengthLimit;//输入长度限制，如果为0则不限制

    public NumericTextFieldOperator() {
        this.lengthLimit = 0;
    }

    public NumericTextFieldOperator(int lengthLimit) {
        this.lengthLimit = lengthLimit;
    }

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText=change.getControlNewText();
        //如果输入为空，则通过改变
        if("".equals(newText)){
            return change;
        }
        //如果输入不为空，且为数字，则进一步处理
        else if(isIntNumeric(newText)){
            if(lengthLimit>0 && newText.length()<=lengthLimit)
                return change;
            else if(lengthLimit<=0)
                return change;
            else
                return null;
        }
        //如果输入不为空，且不为数字，则不通过改变
        else{
            return null;
        }
    }

    /**
     * 判断字符串是否为整数
     */
    public boolean isIntNumeric(String str) {
        Pattern pattern=Pattern.compile("\\d+");
        return pattern.matcher(str).matches();
    }

}
