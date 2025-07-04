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
		assertEquals("x,y,z = a,b,c,d,e,f,g,h,i", result.toString());
	}
	
	@Test
	void ifElseTest() {
		VarsQuery varQuery = new VarsQuery();
		VariableSet result = varQuery.execute("if(a == b + c, if (a > d, x = y = m + n, p = q = u + v), z = w * 2)");
		assertEquals("p,q,x,y,z = a,b,c,d,m,n,u,v,w", result.toString());
	}
	
	@Test
	void objTest() {
		VarsQuery varQuery = new VarsQuery();
		VariableSet result = varQuery.execute("A.x = A.y = B.a + B.b*(2 + (A.z = C.D.h * C.D.i)) - abs(sum(B.c, B.d - C.D.e/C.D.f**C.D.g))");
		assertEquals("A.x,A.y,A.z = B.a,B.b,B.c,B.d,C.D.e,C.D.f,C.D.g,C.D.h,C.D.i", result.toString());
	}
}

