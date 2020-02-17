package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView input,result;
    private StringBuilder str = new StringBuilder();
    private Queue<String> num = new LinkedList<>();
    private Stack<String> symbol = new Stack<>();
    private StringBuilder fix = new StringBuilder();
    private Stack<Double> res = new Stack<>();
    private void tvClear(){
        input.setText("");
        result.setText("");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.input);
        result = findViewById(R.id.result);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.div).setOnClickListener(this);
        findViewById(R.id.mul).setOnClickListener(this);
        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.del).setOnClickListener(this);
        findViewById(R.id.min).setOnClickListener(this);
        findViewById(R.id.num9).setOnClickListener(this);
        findViewById(R.id.num8).setOnClickListener(this);
        findViewById(R.id.num7).setOnClickListener(this);
        findViewById(R.id.num6).setOnClickListener(this);
        findViewById(R.id.num5).setOnClickListener(this);
        findViewById(R.id.num4).setOnClickListener(this);
        findViewById(R.id.num3).setOnClickListener(this);
        findViewById(R.id.num2).setOnClickListener(this);
        findViewById(R.id.num1).setOnClickListener(this);
        findViewById(R.id.num0).setOnClickListener(this);
        findViewById(R.id.dot).setOnClickListener(this);
        tvClear();
    }

    private boolean isOperator(String temp){
        if(temp.equals("+")||temp.equals("-")||temp.equals("*")||temp.equals("/"))
            return true;
        return false;
    }

    private void resolve(String temp){
        int len = str.length();
        switch (temp){
            case "-":
                if(len == 0) {
                    str.append("-");
                    return;
                }
                char ch = str.charAt(len-1);
                if((ch+"").equals("/")||(ch+"").equals("*")){
                    str.append("-");
                    return;
                }
                if(isOperator(ch+"")){
                    str.replace(len-1,len,temp);
                }else{
                    str.append("-");
                }
                break;
            case "+":
            case "*":
            case "/":
                if(len == 0)
                    return ;
                char pre = str.charAt(len-1);
                if(isOperator(pre+"")){
                    str.replace(len-1,len,temp);
                }else{
                    str.append(temp);
                }
                break;
        }
    }
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.clear:
                    tvClear();
                    str.setLength(0);
                    break;
                case R.id.div:
                    resolve("/");
                    input.setText(str.toString());
                    return;
                case R.id.mul:
                    resolve("*");
                    input.setText(str.toString());
                    return;
                case R.id.plus:
                    resolve("+");
                    input.setText(str.toString());
                    return;
                case R.id.min:
                    resolve("-");
                    input.setText(str.toString());
                    return;
                case R.id.num0:
                    str.append("0");
                    break;
                case R.id.num1:
                    str.append("1");
                    break;
                case R.id.num2:
                    str.append("2");
                    break;
                case R.id.num3:
                    str.append("3");
                    break;
                case R.id.num4:
                    str.append("4");
                    break;
                case R.id.num5:
                    str.append("5");
                    break;
                case R.id.num6:
                    str.append("6");
                    break;
                case R.id.num7:
                    str.append("7");
                    break;
                case R.id.num8:
                    str.append("8");
                    break;
                case R.id.num9:
                    str.append("9");
                    break;
                case R.id.del:
                    str.deleteCharAt(str.length()-1);
                    input.setText(str.toString());
                    break;
                case R.id.dot:
                    if (isOperator(str.charAt(str.length() - 1) + "")) {
                        str.append("0.");
                    } else {
                        str.append(".");
                    }
                    break;
                case R.id.equal:
                    if (str.length() == 0) return;
                    if (str.length() == 1 && str.charAt(0) == '-') return;
                    DecimalFormat df = new DecimalFormat("###.###############");
                    String numText = null;
                    double d = getResult();
                    if (Double.isNaN(d) || Double.isInfinite(d)) {
                        result.setText("cannot divide 0");
                    } else {
                        try {
                            numText = df.format(d);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        result.setText("-0".equals(numText) ? "0" : numText);
                        result.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                        //result.setTextColor(Color.parseColor("#ff00ff"));
                    }
                    return ;
            }
            input.setText(str.toString());
            int len = str.length();
            if (len != 0) {
                DecimalFormat df = new DecimalFormat("###.###############");
                String numText = null;
                double d = getResult();
                if (Double.isNaN(d) || Double.isInfinite(d)) {
                    result.setText("cannot divide 0");
                } else {
                    try {
                        numText = df.format(d);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result.setText("-0".equals(numText) ? "0" : numText);
                    result.setTextColor(getResources().getColor(R.color.colorMediaGrey));
                }
            }
        }catch (Exception e){
            result.setText("error");
            result.setTextColor(getResources().getColor(R.color.colorMediaGrey));
        }
        if(str.length() == 0){
            result.setText("");
        }
    }
    private  double getResult(){
        getPostfixExpression();
        return calculatePostfixExpression();
    }
    private void getPostfixExpression(){
        symbol.clear();
        num.clear();
        fix.setLength(0);
        int len = str.length();
        char now =  ' ';
        for(int i=0;i<len;i++){
            now = str.charAt(i);
            if(now >='0'&&now <='9'||now == '.'){
                fix.append(now+"");
            }else{
                if(i == 0||isOperator(str.charAt(i-1)+"")) {
                    fix.append(now);
                    continue;
                }
                num.add(fix.toString());
                fix.setLength(0);
                if(symbol.isEmpty()){
                    symbol.push(now+"");
                }else{
                    while(!symbol.isEmpty()){
                        if(getPriority(symbol.peek()) >= getPriority(now+""))
                            num.add(symbol.pop());
                        else
                            break;
                    }
                    symbol.push(now+"");
                }
            }
        }
        if(fix.length()!=0){
            num.add(fix.toString());
        }
        if(symbol.isEmpty()){
            symbol.push(now+"");
        }else{
            while(!symbol.isEmpty()){
                num.add(symbol.pop());
            }
        }
    }
    private double calculatePostfixExpression(){
        res.clear();
        double num1 = 0.0,num2 = 0.0,num3 = 0.0;
        while(!num.isEmpty()){
            String head = num.poll();
            if(!isOperator(head)){
                res.push(Double.valueOf(head));
            }else{
                if(res.isEmpty())
                    return 0.0;
                num2 = res.pop();
                if(res.isEmpty())
                    return num2;
                num1 = res.pop();
                switch (head){
                    case "-":
                        num3 = num1 - num2;
                        break;
                    case "+":
                        num3 = num1 + num2;
                        break;
                    case "*":
                        num3 = num1 * num2;
                        break;
                    case "/":
                        num3 = num1 / num2;
                        break;
                }
                res.push(num3);
            }
        }
        return res.peek();
    }
    private int getPriority(String temp){
        if(temp.equals("-")||temp.equals("+"))
            return 1;
        else
            return 2;
    }
}
