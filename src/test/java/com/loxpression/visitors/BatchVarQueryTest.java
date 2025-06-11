package com.loxpression.visitors;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BatchVarQueryTest {
	@Test
	void batchTest() {
		System.out.println("批量查询变量测试：");
		int cnt = 10000;
		System.out.println("公式总数：" + cnt);
		List<String> lines = new ArrayList<String>();
		String fml = "A! = 1 + 2 * 3 - 6 - 1 + B! + C! * (D! - E! + 10 ** 2 / 5 - (12 + 8)) - F! * G! +  100 / 5 ** 2 ** 1";
		
		for (int i = 1; i <= cnt; i++) {
			lines.add(fml.replaceAll("!", String.valueOf(i)));
		}
		long start = System.currentTimeMillis();
		VarsQuery varQuery = new VarsQuery();
		VariableSet result = null;
		for (int i = 0; i < lines.size(); i++) {
			String expr = lines.get(i);
			result = varQuery.execute(expr);
		}
		System.out.println(result);
		System.out.println("time: " + (System.currentTimeMillis() - start));
		System.out.println("==========");
	}
}
