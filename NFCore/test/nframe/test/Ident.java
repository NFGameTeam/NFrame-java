/**
 * 
 */
package nframe.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFIdent;

/**
 * @author Xiong
 * 测试NFIdent
 */
public class Ident {

	@Test
	public void test() {
		NFIdent objId1 = new NFIdent();
		NFIdent objId2 = new NFIdent(0, 10);
		final NFIdent objIdNull = new NFIdent();
		
		assertTrue(objId1.equals(objIdNull));
		assertTrue(objId1.isNull());
		assertTrue(!objId2.equals(objIdNull));
		assertTrue(!objId2.isNull());
		
		assertTrue(NFIdent.parse(objId1.toString(), objId2));
		assertTrue(objId2.equals(objIdNull));
		assertTrue(objId2.isNull());
	}

}
