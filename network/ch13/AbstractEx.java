package ch13;

import java.awt.Button;
import java.awt.Color;
import java.awt.Label;

abstract class Shape{
	abstract void draw(); // �߻�޼ҵ� : ��ü�� ���� ���� ����.
}

class Rectangle extends Shape{
	@Override
	void draw() {
		
	}
}

class Circle extends Shape{
	@Override
	void draw() {
		
	}
}

public class AbstractEx extends Object {  //extends object�� �����Ǿ��ִ�.

	public AbstractEx(){
		super(); //super()�� ����Ŭ���� ������ Object. SUPER�� Object�� ��Ÿ����
	}
	
	public static void main(String[] args) {
		AbstractEx ae = new AbstractEx();
		//java.awt;
		Button btn = new Button("��ư"); //��ư�̸� �־���
		btn.setBackground(Color.red);
		Label label = new Label("��");
		
		
		try {
	    int arr[] = new int[3];
	    arr[3] = 22; //�迭 ��ȣ 0 1 2 �ε� 3���� �ϴ�.
	  } catch(Exception e) {
		System.out.println("�迭 ����");    
	}
  }
}










