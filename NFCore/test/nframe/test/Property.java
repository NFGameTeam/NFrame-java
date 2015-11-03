/**
 * 
 */
package nframe.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFDataList;
import nframe.NFIData;
import nframe.NFIDataList;
import nframe.NFIProperty;
import nframe.NFIPropertyHandler;
import nframe.NFIdent;
import nframe.NFProperty;

/**
 * @author Xiong
 * 测试属性
 */
public class Property {
	public static final NFIdent objId1 = new NFIdent(0,1);
	public static final NFIdent objId2 = new NFIdent(0,2);
	
	public class Handler1 implements NFIPropertyHandler {
		@Override
		public void handle(NFIdent id, String propName, NFIData oldVar, NFIData newVar) {
			assertTrue(id.equals(objId1));
			assertTrue(propName.equals("test prop1"));
			assertTrue(oldVar.getInt() == 5);
			assertTrue(newVar.getInt() == 10);
		}
	}
	
	public class Handler2 implements NFIPropertyHandler {
		@Override
		public void handle(NFIdent id, String propName, NFIData oldVar, NFIData newVar) {
			assertTrue(id.equals(objId1));
			assertTrue(propName.equals("test prop1"));
			assertTrue(oldVar.getInt() == 5);
			assertTrue(newVar.getInt() == 10);
		}
	}
	
	public class Handler3 implements NFIPropertyHandler {
		@Override
		public void handle(NFIdent id, String propName, NFIData oldVar, NFIData newVar) {
			assertTrue(id.equals(objId2));
			assertTrue(propName.equals("test prop2"));
			assertTrue(Double.compare(oldVar.getFloat(), 2.5f) == 0);
			assertTrue(newVar.getString().equals("my new val"));
		}
	}
	
	@Test
	public void test() {
		NFIProperty prop1 = new NFProperty(new NFIdent(0,1), "test prop1", 5);
		assertTrue(prop1.getInt() == 5);
		assertTrue(prop1.getType() == NFIData.Type.INT);
		
		prop1.addCallback(new Handler1());
		prop1.addCallback(new Handler2());
		
		prop1.set(5);
		prop1.set(10);
		
		NFIProperty prop2 = new NFProperty(new NFIdent(0,2), "test prop2", 2.5f);
		assertTrue(Double.compare(prop2.getFloat(), 2.5f) == 0);
		assertTrue(prop2.getType() == NFIData.Type.FLOAT);
		
		prop2.addCallback(new Handler3());
		
		prop2.set("my new val");
	}
}
