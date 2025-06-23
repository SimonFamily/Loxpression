package com.loxpression.env.form;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.LoxRunner;
import com.loxpression.env.form.dao.FieldDao;
import com.loxpression.env.form.defines.DataTable;
import com.loxpression.env.form.defines.FieldDefine;
import com.loxpression.env.form.service.DataQuery;
import com.loxpression.execution.chunk.Chunk;

public class FormEnvTest {
	
	@Test
	void dataQueryTest() throws IOException {
		DataQuery dataQuery = new DataQuery("id", "1");
		List<FieldDefine> fields = FieldDao.queryAllFields("formId1");
		for (FieldDefine field : fields) {
			dataQuery.addColumn(field);
		}
		DataTable dataTable = dataQuery.executeQuery();
		assertEquals(1, dataTable.size());
	}
	
	@Test
	void testIntepreter() {
		List<String> lines = new ArrayList<String>();
		lines.add("标题1 * 2 + 1");
		lines.add("标题2 * 标题3 + 标题4");
		lines.add("标题3 ** 2");
		lines.add("标题4 > 0");
		lines.add("标题5 = 标题6 * 标题7");
		lines.add("标题6 = 标题9 + 标题10");
		lines.add("标题7");
		lines.add("标题8 = 标题5 + 标题6 + 标题7 + 标题9 + 标题10");
		lines.add("标题9");
		lines.add("标题10");
		FormEnvironment env = new FormEnvironment("formId1");
		LoxRunner runner = new LoxRunner();
		Object[] r = runner.execute(lines, env);
		checkResult(r);
	}
	
	@Test
	void testChunk() {
		List<String> lines = new ArrayList<String>();
		lines.add("标题1 * 2 + 1");
		lines.add("标题2 * 标题3 + 标题4");
		lines.add("标题3 ** 2");
		lines.add("标题4 > 0");
		lines.add("标题5 = 标题6 * 标题7");
		lines.add("标题6 = 标题9 + 标题10");
		lines.add("标题7");
		lines.add("标题8 = 标题5 + 标题6 + 标题7 + 标题9 + 标题10");
		lines.add("标题9");
		lines.add("标题10");
		LoxRunner runner = new LoxRunner();
		Chunk chunk = runner.compileSource(lines);
		FormEnvironment env = new FormEnvironment("formId1");
		runner = new LoxRunner();
		Object[] r = runner.runChunk(chunk, env);
		checkResult(r);
	}
	
	private void checkResult(Object[] r) {
		assertEquals(5, r[0]);
		assertEquals(17, r[1]);
		assertEquals(16.0, r[2]);
		assertEquals(true, r[3]);
		assertEquals(33, r[4]);
		assertEquals(11, r[5]);
		assertEquals(3, r[6]);
		assertEquals(58, r[7]);
		assertEquals(5, r[8]);
		assertEquals(6, r[9]);
	}
}
