package huxg.framework.aspect;

import java.util.Date;
import java.util.Stack;

public class StackDemo {

	public static void main(String[] args) {
		Stack stack=new Stack();
		stack.push("0");
		stack.push(new Integer(1));
		stack.push(2.0);
		stack.push(new Date());
		
		stack.pop();

    }
}