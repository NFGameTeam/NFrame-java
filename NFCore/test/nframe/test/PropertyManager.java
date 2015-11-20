/**
 * 
 */
package nframe.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFIData;
import nframe.NFIProperty;
import nframe.NFIPropertyHandler;
import nframe.NFIPropertyManager;
import nframe.NFData;
import nframe.NFGUID;
import nframe.NFPropertyManager;

/**
 * @author Xiong
 * 测试属性管理器
 */
public class PropertyManager {
	public static final NFGUID oid1 = new NFGUID(0,1);
	
	public class Handler1 implements NFIPropertyHandler {
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar) {
			assertTrue(oid.equals(oid1));
			assertTrue(propName.equals("prop1"));
			assertTrue(oldVar.getInt() == 5);
			assertTrue(newVar.getInt() == 10);
		}
	}
	
	public class Handler2 implements NFIPropertyHandler {
		@Override
		public void handle(NFGUID oid, String propName, NFIData oldVar, NFIData newVar) {
			assertTrue(oid.equals(oid1));
			assertTrue(propName.equals("prop2"));
			assertTrue(Double.compare(oldVar.getFloat(), 2.5f) == 0);
			assertTrue(newVar.getString().equals("my new val"));
		}
	}

	@Test
	public void test() {
		NFIPropertyManager propMgr = new NFPropertyManager(oid1);
		
		NFIProperty prop1 = propMgr.addProperty("prop1", 5);
		NFIProperty prop2 = propMgr.addProperty("prop2", 2.5f);
		assertTrue(prop1 != null);
		assertTrue(propMgr.addProperty("prop1", 1323.5f) == null);
		assertTrue(prop1 == propMgr.getProperty("prop1"));
		assertTrue(prop2 == propMgr.getProperty("prop2"));
		
		propMgr.addCallback("prop1", new Handler1());
		prop1.set(5);
		prop1.set(10);
		
		propMgr.addCallback("prop2", new Handler2());
		propMgr.addCallback("prop2", new NFIPropertyHandler() {
			@Override
			public void handle(NFGUID id, String propName, NFIData oldVar, NFIData newVar){
				assertTrue(id.equals(oid1));
				assertTrue(propName.equals("prop2"));
				assertTrue(Double.compare(oldVar.getFloat(), 2.5f) == 0);
				assertTrue(newVar.getString().equals("my new val"));
			}
		});
		prop2.set(new NFData("my new val"));
		
		NFIProperty[] propList = propMgr.getPropertyList();
		assertTrue(propList.length == 2);
	}

}
