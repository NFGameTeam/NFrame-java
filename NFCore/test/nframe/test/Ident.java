/**
 * 
 */
package nframe.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFGUID;

/**
 * @author Xiong
 * 测试NFGUID
 */
public class Ident {

	@Test
	public void test() {
		NFGUID objId1 = new NFGUID();
		NFGUID objId2 = new NFGUID(0, 10);
		final NFGUID objIdNull = new NFGUID();
		
		assertTrue(objId1.equals(objIdNull));
		assertTrue(objId1.isNull());
		assertTrue(!objId2.equals(objIdNull));
		assertTrue(!objId2.isNull());
		
		assertTrue(NFGUID.parse(objId1.toString(), objId2));
		assertTrue(objId2.equals(objIdNull));
		assertTrue(objId2.isNull());
	}

}
