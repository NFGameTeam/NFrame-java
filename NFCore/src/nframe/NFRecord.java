/**
 * 
 */
package nframe;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nframe.NFIData.Type;

/**
 * @author Xiong
 * 记录类
 */
public class NFRecord implements NFIRecord {
	
	private NFGUID guid;
	private String name;
	private int maxRow;
	private NFIDataList varTypes;
	private NFIDataList varTags;
	private Map<Integer, NFIDataList> table = new Hashtable<Integer, NFIDataList>();
	private List<NFIRecordHandler> callbacks;
	private NFIDataList oldVar = new NFDataList();
	private NFIDataList newVar = new NFDataList();
	
	/**
	 * 构造
	 * @param guid
	 * @param name
	 * @param varTypes
	 * @param varTags
	 */
	public NFRecord(NFGUID guid, String name, int maxRow, NFIDataList varTypes, NFIDataList varTags){
		this.guid = guid;
		this.name = name;
		this.maxRow = maxRow;
		this.varTypes = new NFDataList(varTypes);
		this.varTags = new NFDataList(varTags);
	}
	
	/**
	 * 构造，带索引（类似sql）
	 * @param guid
	 * @param name
	 * @param varTypes
	 * @param varTags
	 * @param uniqueIndexColumn 唯一索引的列
	 * @param indexColumn 不唯一索引的列
	 */
	public NFRecord(NFGUID guid, String name, int maxRow, NFIDataList varTypes, NFIDataList varTags, int uniqueIndexColumn, int indexColumn){
		this.guid = guid;
		this.name = name;
		this.maxRow = maxRow;
		this.varTypes = new NFDataList(varTypes);
		this.varTags = new NFDataList(varTags);
		// TODO 完成linq索引
	}

	@Override
	public int getRowNum() {
		return table.size();
	}

	@Override
	public int getColumnNum() {
		return varTypes.size();
	}
	
	@Override
	public int getMaxRowNum(){
		return maxRow;
	}

	@Override
	public Type getColumnType(int column) {
		return varTypes.getType(column);
	}

	@Override
	public String getColumnTag(int column) {
		return varTags.getString(column);
	}

	@Override
	public int addRow(int row) {
		return addRow(row, varTypes);
	}

	@Override
	public int addRow(int row, NFIDataList var) {
		if (row >= 0 && row < maxRow){
			if (!table.containsKey(row)){
				NFIDataList newVar = new NFDataList(var);
				table.put(row, newVar);
				if (callbacks != null){
					this.oldVar.clear();
					this.newVar.set(newVar);
					for (NFIRecordHandler cb : callbacks){
						cb.handle(guid, name, Optype.ADD, row, 0, this.oldVar, this.newVar);
					}
				}
				return row;
			}
		}
		return -1;
	}

	@Override
	public int setRow(int row, NFIDataList var) {
		assert checkVars(var);
		if (row >= 0 && row < maxRow){
			table.put(row, new NFDataList(var));
			return row;
		}
		return -1;
	}

	@Override
	public boolean set(int row, int column, long var) {
		if (row >= 0 && row < maxRow){
			if (varTypes.getType(column) == NFIData.Type.INT){
				NFIDataList vars = table.get(row);
				if (vars == null){
					addRow(row);
					vars = table.get(row);
				}
				long value = vars.getInt(column);
				if (value != var){
					vars.set(column, var);
					if (callbacks != null){
						this.oldVar.clear();
						this.oldVar.add(value);
						this.newVar.clear();
						this.newVar.add(var);
						for (NFIRecordHandler cb : callbacks){
							cb.handle(guid, name, Optype.UPDATE, row, column, this.oldVar, this.newVar);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean set(int row, int column, double var) {
		if (row >= 0 && row < maxRow){
			if (varTypes.getType(column) == NFIData.Type.FLOAT){
				NFIDataList vars = table.get(row);
				if (vars == null){
					addRow(row);
					vars = table.get(row);
				}
				double value = vars.getFloat(column);
				if (Double.compare(value, var) != 0){
					vars.set(column, var);
					if (callbacks != null){
						this.oldVar.clear();
						this.oldVar.add(value);
						this.newVar.clear();
						this.newVar.add(var);
						for (NFIRecordHandler cb : callbacks){
							cb.handle(guid, name, Optype.UPDATE, row, column, this.oldVar, this.newVar);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean set(int row, int column, String var) {
		if (row >= 0 && row < maxRow){
			if (varTypes.getType(column) == NFIData.Type.STRING){
				NFIDataList vars = table.get(row);
				if (vars == null){
					addRow(row);
					vars = table.get(row);
				}
				String value = vars.getString(column);
				if (!value.equals(var)){
					vars.set(column, var);
					if (callbacks != null){
						this.oldVar.clear();
						this.oldVar.add(value);
						this.newVar.clear();
						this.newVar.add(var);
						for (NFIRecordHandler cb : callbacks){
							cb.handle(guid, name, Optype.UPDATE, row, column, this.oldVar, this.newVar);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean set(int row, int column, NFGUID var) {
		if (row >= 0 && row < maxRow){
			if (varTypes.getType(column) == NFIData.Type.OBJECT){
				NFIDataList vars = table.get(row);
				if (vars == null){
					addRow(row);
					vars = table.get(row);
				}
				NFGUID value = vars.getObject(column);
				if (!value.equals(var)){
					vars.set(column, var);
					if (callbacks != null){
						this.oldVar.clear();
						this.oldVar.add(value);
						this.newVar.clear();
						this.newVar.add(var);
						for (NFIRecordHandler cb : callbacks){
							cb.handle(guid, name, Optype.UPDATE, row, column, this.oldVar, this.newVar);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean set(int row, String colTag, long var) {
		int column = getColumnId(colTag);
		if (column < 0){
			return false;
		}
		return set(row, column, var);
	}

	@Override
	public boolean set(int row, String colTag, double var) {
		int column = getColumnId(colTag);
		if (column < 0){
			return false;
		}
		return set(row, column, var);
	}

	@Override
	public boolean set(int row, String colTag, String var) {
		int column = getColumnId(colTag);
		if (column < 0){
			return false;
		}
		return set(row, column, var);
	}

	@Override
	public boolean set(int row, String colTag, NFGUID var) {
		int column = getColumnId(colTag);
		if (column < 0){
			return false;
		}
		return set(row, column, var);
	}

	@Override
	public NFIDataList getRow(int row) {
		return table.get(row);
	}

	@Override
	public boolean swapRow(int originRow, int targetRow) {
		if (originRow >= 0 && originRow < maxRow && targetRow >= 0 && targetRow < maxRow){
			NFIDataList varOrigin = table.get(originRow);
			NFIDataList varTarget = table.get(targetRow);
			if (varTarget == null){
				table.remove(originRow);
			}else{
				table.put(originRow, varTarget);
			}
			
			if (varOrigin == null){
				table.remove(targetRow);
			}else{
				table.put(targetRow, varOrigin);
			}

			if (callbacks != null){
				this.oldVar.clear();
				this.newVar.clear();
				for (NFIRecordHandler cb : callbacks){
					cb.handle(guid, name, Optype.SWAP, originRow, targetRow, this.oldVar, this.newVar);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public long getInt(int row, int column) {
		NFIDataList vars = getRow(row);
		if (vars != null){
			return vars.getInt(column);
		}
		return NFIData.INT_NIL;
	}

	@Override
	public double getFloat(int row, int column) {
		NFIDataList vars = getRow(row);
		if (vars != null){
			return vars.getFloat(column);
		}
		return NFIData.FLOAT_NIL;
	}

	@Override
	public String getString(int row, int column) {
		NFIDataList vars = getRow(row);
		if (vars != null){
			return vars.getString(column);
		}
		return NFIData.STRING_NIL;
	}

	@Override
	public NFGUID getObject(int row, int column) {
		NFIDataList vars = getRow(row);
		if (vars != null){
			return vars.getObject(column);
		}
		return NFIData.OBJECT_NIL;
	}

	@Override
	public long getInt(int row, String colTag) {
		int column = getColumnId(colTag);
		if (column >= 0){
			return getInt(row, column);
		}
		return NFIData.INT_NIL;
	}

	@Override
	public double getFloat(int row, String colTag) {
		int column = getColumnId(colTag);
		if (column >= 0){
			return getFloat(row, column);
		}
		return NFIData.FLOAT_NIL;
	}

	@Override
	public String getString(int row, String colTag) {
		int column = getColumnId(colTag);
		if (column >= 0){
			return getString(row, column);
		}
		return NFIData.STRING_NIL;
	}

	@Override
	public NFGUID getObject(int row, String colTag) {
		int column = getColumnId(colTag);
		if (column >= 0){
			return getObject(row, column);
		}
		return NFIData.OBJECT_NIL;
	}

	@Override
	public int find(int column, long value, NFIDataList result) {
		if (column < 0 || column >= varTypes.size()){
			return -1;
		}
		
		// TODO 完成linq
		int count = 0;
		for (Entry<Integer, NFIDataList> e : table.entrySet()){
			NFIDataList vars = e.getValue();
			if (vars.getInt(column) == value){
				if (result != null){
					result.add(e.getKey());
				}
				++count;
			}
		}
		return count;
	}

	@Override
	public int find(int column, double value, NFIDataList result) {
		if (column < 0 || column >= varTypes.size()){
			return -1;
		}
		
		// TODO 完成linq
		int count = 0;
		for (Entry<Integer, NFIDataList> e : table.entrySet()){
			NFIDataList vars = e.getValue();
			if (Double.compare(vars.getFloat(column), value) == 0){
				if (result != null){
					result.add(e.getKey());
				}
				++count;
			}
		}
		return count;
	}

	@Override
	public int find(int column, String value, NFIDataList result) {
		assert value != null;
		if (column < 0 || column >= varTypes.size()){
			return -1;
		}
		
		// TODO 完成linq
		int count = 0;
		for (Entry<Integer, NFIDataList> e : table.entrySet()){
			NFIDataList vars = e.getValue();
			if (value.equals(vars.getString(column))){
				if (result != null){
					result.add(e.getKey());
				}
				++count;
			}
		}
		return count;
	}

	@Override
	public int find(int column, NFGUID value, NFIDataList result) {
		assert value != null;
		if (column < 0 || column >= varTypes.size()){
			return -1;
		}
		
		// TODO 完成linq
		int count = 0;
		for (Entry<Integer, NFIDataList> e : table.entrySet()){
			NFIDataList vars = e.getValue();
			if (value.equals(vars.getObject(column))){
				if (result != null){
					result.add(e.getKey());
				}
				++count;
			}
		}
		return count;
	}

	@Override
	public int find(String colTag, long value, NFIDataList result) {
		return find(getColumnId(colTag), value, result);
	}

	@Override
	public int find(String colTag, double value, NFIDataList result) {
		return find(getColumnId(colTag), value, result);
	}

	@Override
	public int find(String colTag, String value, NFIDataList result) {
		return find(getColumnId(colTag), value, result);
	}

	@Override
	public int find(String colTag, NFGUID value, NFIDataList result) {
		return find(getColumnId(colTag), value, result);
	}

	@Override
	public boolean delRow(int row) {
		NFIDataList vars = table.get(row);
		if (vars != null){
			if (callbacks != null){
				this.oldVar.set(vars);
				this.newVar.clear();
				for (NFIRecordHandler cb : callbacks){
					cb.handle(guid, name, Optype.DEL, row, 0, this.oldVar, this.newVar);
				}
			}
			table.remove(row);
			return true;
		}
		return false;
	}

	@Override
	public void delAllRows(){
		List<Integer> rows = new ArrayList<Integer>(table.size());
		for (int row : table.keySet()){
			rows.add(row);
		}
		
		for (int row : rows){
			delRow(row);
		}
	}

	@Override
	public void clear() {
		table.clear();
	}

	@Override
	public void addCallback(NFIRecordHandler cb) {
		if (callbacks == null){
			callbacks = new ArrayList<NFIRecordHandler>(1);
		}
		callbacks.add(cb);
	}

	/**
	 * 使用tag获取列id
	 * @param colTag
	 * @return 如果未找到，返回-1
	 */
	private int getColumnId(String colTag){
		assert colTag != null;
		for (int i=0; i<varTags.size(); ++i){
			if (colTag.equals(varTags.getString(i))){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 检查输入的参数是否和record的格式相符
	 * @param vars
	 * @return
	 */
	private boolean checkVars(NFIDataList vars){
		if (vars.size() != varTypes.size()){
			return false;
		}
		
		for (int i=0; i<vars.size(); ++i){
			if (vars.getType(i) != varTypes.getType(i)){
				return false;
			}
		}
		
		return true;
	}
}
