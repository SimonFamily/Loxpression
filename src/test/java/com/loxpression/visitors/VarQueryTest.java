package com.loxpression.visitors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class VarQueryTest {

	@Test
	void test() {
		VarsQuery varQuery = new VarsQuery();
		VariableSet result = varQuery.execute("x = y = a + b*(2 + (z = h * i)) - abs(sum(c, d - e/f**g))");
		System.out.println(result);
		assertEquals("x,y,z = a,b,c,d,e,f,g,h,i", result.toString());
	}
	
	@Test
	void ifElseTest() {
		VarsQuery varQuery = new VarsQuery();
		VariableSet result = varQuery.execute("if(a == b + c, if (a > d, x = y = m + n, p = q = u + v), z = w * 2)");
		System.out.println(result);
		assertEquals("p,q,x,y,z = a,b,c,d,u,v,w,m,n", result.toString());
	}
}
