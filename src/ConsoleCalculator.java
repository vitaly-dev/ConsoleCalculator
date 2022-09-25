import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.Math.abs;

/**
 * Добрый день
 */
public class ConsoleCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите выражение: ");
        char[] chars = scanner.nextLine().replace(" ", "").toCharArray();
        List<String> listStr = new ArrayList<>();
        String num = "";
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (isOperator(aChar)) {
                listStr.add(String.valueOf(aChar));
            } else if ((i + 1 <= chars.length - 1) && isOperator(chars[i + 1]) || i == chars.length - 1) {
                listStr.add(num.equals("") ? String.valueOf(aChar) : num + aChar);
                num = "";
            } else {
                num = String.join("", num, String.valueOf(aChar));
            }
        }
        Stack<String> ops = new Stack<>();
        Stack<Double> nums = new Stack<>();
        String operators = "(+-*/";

        for (String s : listStr) {
            if (operators.contains(s)) {
                ops.push(s);
            } else if (s.equals(")")) {
                calc(ops, nums, false);
                ops.pop();
                if (!ops.isEmpty() && (ops.peek().equals("*") || ops.peek().equals("/"))) {
                    calc(ops, nums, true);
                    if (!nums.isEmpty() && nums.peek() != 0) {
                        double peekNum = nums.pop();
                        ops.push(peekNum > 0 ? "+" : "-");
                        nums.push(abs(peekNum));
                    }
                }
            } else {
                double v = Double.parseDouble(s);
                nums.push(v);
                int i = ops.size() == 0 ? 0 : ops.size() - 1;
                if ((ops.isEmpty() || (ops.peek().equals("("))) && v != 0) {
                    ops.push(v > 0 ? "+" : "-");
                }
                if (ops.peek().equals("/") || ops.get(i - (i > 0 ? 1 : 0)).equals("/") || ops.get(i - (i > 1 ? 2 : 0)).equals("/")
                        && !ops.peek().equals("(") && !ops.get(i - (i > 0 ? 1 : 0)).equals("(")) {
                    calc(ops, nums, false);
                }
            }
        }
        calc(ops, nums, false);
        System.out.println((!ops.isEmpty() ? (ops.pop().equals("+") ? "" : "-") : "") + nums.pop());
    }

    private static void calc(Stack<String> ops, Stack<Double> nums, boolean onlyMultOrDiv) {
        while (!ops.isEmpty() && !ops.peek().equals("(") && nums.size() >= 1) {
            if ((ops.peek().equals("+") || ops.peek().equals("-")) && onlyMultOrDiv) {
                break;
            }
            String op = ops.pop();
            switch (op) {
                case "+", "-" -> {
                    double lastNum = op.equals("+") ? nums.pop() : -nums.pop();
                    if (!ops.isEmpty() && !ops.peek().equals("(") && nums.size() >= 1) {
                        boolean minus = ops.peek().equals("-");
                        if (minus || ops.peek().equals("+")) {
                            ops.pop();
                        }
                        double toPush = lastNum + (minus ? -nums.pop() : nums.pop());
                        if (ops.isEmpty() || !ops.peek().equals("(")) {
                            ops.push(toPush > 0 ? "+" : "-");
                            nums.push(abs(toPush));
                        } else nums.push(toPush);
                    } else {
                        nums.push(lastNum);
                    }
                }
                case "*", "/" -> {
                    double val = (op.equals("/") ? (1 / nums.pop()) : nums.pop()) * nums.pop();
                    pushVal(ops, nums, val);
                }
            }
        }
    }

    private static boolean isOperator(char ch) {
        return (ch == ('(') || ch == (')') || ch == ('+') || ch == ('-') || ch == ('*') || ch == ('/'));
    }

    private static void pushVal(Stack<String> ops, Stack<Double> nums, double val) {
        if (!ops.isEmpty() && (ops.peek().equals("+") || ops.peek().equals("-"))) {
            val = ops.pop().equals("+") ? val : -val;
        }
        nums.push(val);
    }
}