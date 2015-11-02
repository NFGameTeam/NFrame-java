package nframe.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFDataList;
import nframe.NFIDataList;
import nframe.NFIdent;

public class DataList {

	@Test
	public void test() {
		NFIDataList varList = new NFDataList(1, 1.0f, 2.3f, "My Name");
		
		assertTrue(varList.size() == 4);
		assertTrue(varList.getInt(0) == 1);
		assertTrue(varList.getString(3).equals("My Name"));
		assertTrue(varList.add(4500) == 4);
		assertTrue(varList.size() == 5);
		
		NFIdent objId = new NFIdent(1, 10);
		assertTrue(varList.add(objId) == 5);
		assertTrue(varList.size() == 6);
		assertTrue(varList.getObject(5) == objId);
		assertTrue(varList.getObject(5).equals(objId));
		
		varList.set(4, 4600);
		assertTrue(varList.getInt(4) == 4600);
		
		NFIDataList newVarList = new NFDataList(2, 2.0f, 3.4f, "Not My Name");
		varList.set(newVarList);
		assertTrue(varList.getInt(0) == 2);
		assertTrue(varList.getString(3).equals("Not My Name"));
		assertTrue(varList.size() == 4);
		
		NFIDataList varList2 = new NFDataList(varList);
		assertTrue(varList2.getInt(0) == 2);
		assertTrue(varList2.getString(3).equals("Not My Name"));
		assertTrue(varList2.size() == 4);
		
		varList.clear();
		assertTrue(varList.size() == 0);
		assertTrue(varList.isEmpty());
	}

}
