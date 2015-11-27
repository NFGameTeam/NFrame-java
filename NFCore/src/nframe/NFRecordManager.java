/**
 * 
 */
package nframe;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author Xiong
 *
 */
public class NFRecordManager implements NFIRecordManager {
	/** 记录表 */
	private Map<String, NFIRecord> records = new Hashtable<String, NFIRecord>();
	/** 对象id */
	private NFGUID guid;
	
	public NFRecordManager(NFGUID guid){
		this.guid = guid;
	}

	@Override
	public NFIRecord addRecord(String recordName, int maxRow, NFIDataList varTypes, NFIDataList varTags) {
		NFIRecord rec = null;
		if (!records.containsKey(recordName)){
			rec = new NFRecord(guid, recordName, maxRow, varTypes, varTags);
			records.put(recordName, rec);
		}
		return rec;
	}

	@Override
	public NFIRecord getRecord(String recordName) {
		return records.get(recordName);
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, long var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, column, var);
		}
		return false;
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, double var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, column, var);
		}
		return false;
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, String var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, column, var);
		}
		return false;
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, NFGUID var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, column, var);
		}
		return false;
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, long var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, colTag, var);
		}
		return false;
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, double var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, colTag, var);
		}
		return false;
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, String var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, colTag, var);
		}
		return false;
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, NFGUID var) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.set(row, colTag, var);
		}
		return false;
	}

	@Override
	public long getRecordInt(String recordName, int row, int column) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getInt(row, column);
		}
		return NFIData.INT_NIL;
	}

	@Override
	public double getRecordFloat(String recordName, int row, int column) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getFloat(row, column);
		}
		return NFIData.FLOAT_NIL;
	}

	@Override
	public String getRecordString(String recordName, int row, int column) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getString(row, column);
		}
		return NFIData.STRING_NIL;
	}

	@Override
	public NFGUID getRecordObject(String recordName, int row, int column) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getObject(row, column);
		}
		return NFIData.OBJECT_NIL;
	}

	@Override
	public long getRecordInt(String recordName, int row, String colTag) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getInt(row, colTag);
		}
		return NFIData.INT_NIL;
	}

	@Override
	public double getRecordFloat(String recordName, int row, String colTag) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getFloat(row, colTag);
		}
		return NFIData.FLOAT_NIL;
	}

	@Override
	public String getRecordString(String recordName, int row, String colTag) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getString(row, colTag);
		}
		return NFIData.STRING_NIL;
	}

	@Override
	public NFGUID getRecordObject(String recordName, int row, String colTag) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			return rec.getObject(row, colTag);
		}
		return NFIData.OBJECT_NIL;
	}

	@Override
	public boolean addCallback(String recordName, NFIRecordHandler cb) {
		NFIRecord rec = getRecord(recordName);
		if (rec != null){
			rec.addCallback(cb);
			return true;
		}
		return false;
	}

}
