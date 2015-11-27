/**
 * 
 */
package nframe.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFDataList;
import nframe.NFGUID;
import nframe.NFIData;
import nframe.NFIDataList;
import nframe.NFIRecord;
import nframe.NFIRecord.Optype;
import nframe.NFIRecordHandler;
import nframe.NFRecord;

/**
 * @author Xiong
 * 测试Record
 */
public class Record {
	static class Counter {
		private int count = 0;
		
		public void set(int c){
			count = c;
		}
		
		public int get(){
			return count;
		}
	}
	private static final NFGUID guid1 = new NFGUID(0,1);
	private static final NFGUID guid2 = new NFGUID(0,2);
	private static final Counter step = new Counter();

	class Handler implements NFIRecordHandler {
		@Override
		public void handle(NFGUID guid, String recordName, Optype optype, int arg1, int arg2, NFIDataList oldVar, NFIDataList newVar){
			assertTrue(guid.equals(guid1));
			assertTrue(recordName.equals("my record"));
			
			if (optype == Optype.ADD){
				if (arg1 == 1){
					assertTrue(step.get() == 1);
				}else if (arg1 == 5){
					assertTrue(step.get() == 2);
				}else if (arg1 == 9){
					assertTrue(step.get() == 13);
				}
			}else if (optype == Optype.UPDATE){
				if (arg1 == 1 && arg2 == 0){
					assertTrue(step.get() == 10);
					assertTrue(oldVar.getInt(0) == NFIData.INT_NIL);
					assertTrue(newVar.getInt(0) == 2);
				}

				if (arg1 == 5 && arg2 == 2){
					assertTrue(step.get() == 11);
					assertTrue(Double.compare(oldVar.getFloat(0), 0.2f) == 0);
					assertTrue(Double.compare(newVar.getFloat(0), 101.01f) == 0);
				}
			}else if (optype == Optype.SWAP){
				assertTrue(step.get() == -1);
				assertTrue(arg1 == 1);
				assertTrue(arg2 == 9);
			}else if (optype == Optype.DEL){
				assertTrue(step.get() == 42);
				assertTrue(arg1 == 5);
			}
		}
		
	}

	@Test
	public void test(){
		NFIRecord rec = 
			new NFRecord(
				guid1, 
				"my record", 
				10,
				new NFDataList(NFIData.INT_NIL, NFIData.STRING_NIL, NFIData.FLOAT_NIL, NFIData.OBJECT_NIL), 
				new NFDataList("id", "name", "factor", "object")
			);
		
		rec.addCallback(new Handler());
		
		assertTrue(rec.getRowNum() == 0);
		assertTrue(rec.getColumnNum() == 4);
		assertTrue(rec.getMaxRowNum() == 10);
		assertTrue(rec.getColumnType(0) == NFIData.Type.INT);
		assertTrue(rec.getColumnType(1) == NFIData.Type.STRING);
		assertTrue(rec.getColumnType(2) == NFIData.Type.FLOAT);
		assertTrue(rec.getColumnType(3) == NFIData.Type.OBJECT);
		assertTrue("id".equals(rec.getColumnTag(0)));
		assertTrue("name".equals(rec.getColumnTag(1)));
		assertTrue("factor".equals(rec.getColumnTag(2)));
		assertTrue("object".equals(rec.getColumnTag(3)));
		
		step.set(1);
		assertTrue(rec.addRow(1) == 1);
		step.set(2);
		assertTrue(rec.addRow(5) == 5);
		assertTrue(rec.setRow(5, new NFDataList(1, "name1", 0.2f, guid1)) == 5);
		
		assertTrue(rec.getInt(5, 0) == 1);
		assertTrue("name1".equals(rec.getString(5, 1)));
		assertTrue(Double.compare(rec.getFloat(5, 2), 0.2f) == 0);
		assertTrue(guid1.equals(rec.getObject(5, 3)));
		assertTrue(rec.getInt(5, "id") == 1);
		assertTrue("name1".equals(rec.getString(5, "name")));
		assertTrue(Double.compare(rec.getFloat(5, "factor"), 0.2f) == 0);
		assertTrue(guid1.equals(rec.getObject(5, "object")));
		
		assertTrue(rec.set(0, 1, 2) == false);
		assertTrue(rec.set(1, 1, 2) == false);

		step.set(10);
		assertTrue(rec.set(1, 0, 2) == true);
		
		step.set(11);
		assertTrue(rec.set(5, "factor", 101.01f) == true);
		
		step.set(13);
		assertTrue(rec.addRow(9, new NFDataList(9, "name2", 0.3f, guid2)) == 9);
		
		assertTrue(rec.getInt(9, 0) == 9);
		assertTrue("name2".equals(rec.getString(9, 1)));
		assertTrue(Double.compare(rec.getFloat(9, 2), 0.3f) == 0);
		assertTrue(guid2.equals(rec.getObject(9, 3)));
		assertTrue(rec.getInt(9, "id") == 9);
		assertTrue("name2".equals(rec.getString(9, "name")));
		assertTrue(Double.compare(rec.getFloat(9, "factor"), 0.3f) == 0);
		assertTrue(guid2.equals(rec.getObject(9, "object")));
		
		step.set(-1);
		assertTrue(rec.swapRow(1, 9) == true);
		
		assertTrue(rec.getInt(1, 0) == 9);
		assertTrue("name2".equals(rec.getString(1, 1)));
		assertTrue(Double.compare(rec.getFloat(1, 2), 0.3f) == 0);
		assertTrue(guid2.equals(rec.getObject(1, 3)));
		assertTrue(rec.getInt(1, "id") == 9);
		assertTrue("name2".equals(rec.getString(1, "name")));
		assertTrue(Double.compare(rec.getFloat(1, "factor"), 0.3f) == 0);
		assertTrue(guid2.equals(rec.getObject(1, "object")));
		
		assertTrue(rec.getRowNum() == 3);
		
		assertTrue(rec.delRow(0) == false);
		step.set(42);
		assertTrue(rec.delRow(5) == true);
		assertTrue(rec.getRowNum() == 2);
		
		rec.clear();
		assertTrue(rec.getRowNum() == 0);
	}

}
