/**   
 * @Title: ${name} 
 * @Package ${package_name} 
 * @Description: 略
 * @author zoecee yideal_formula@126.com  
 * @date 2017.7.6 
 * @version V1.0   
 */
package cn.yeegro.nframe.comm.code.math;

public class NFVector2 {

	private float x, y;

	private void InitData() {
		x = 0.0f;
		y = 0.0f;
	}

	public NFVector2() {
		InitData();
	}

	public NFVector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public NFVector2(float[] coordinate) {
		this.x = coordinate[0];
		this.y = coordinate[1];
	}

	public NFVector2(double[] coordinate) {
		this.x = (float) coordinate[0];
		this.y = (float) coordinate[1];
	}

	public NFVector2(NFVector2 v) {
		this.x = v.x;
		this.y = v.y;
	}

	public boolean oper_lt(NFVector2 v) {
		return this.Length() < v.Length();
	}

	public boolean oper_gt(NFVector2 v) {
		return this.Length() > v.Length();
	}

	public NFVector2 oper_eq(NFVector2 v) {
		this.x = v.x;
		this.y = v.y;

		return this;
	}

	public boolean oper_heq(NFVector2 v) {
		return Math.abs(this.x - v.x) < 0.001f
				&& Math.abs(this.y - v.y) < 0.001f;
	}

	public boolean oper_ne(NFVector2 v) {
		return Math.abs(this.x - v.x) >= 0.001f
				|| Math.abs(this.y - v.y) >= 0.001f;
	}

	// ----------------------------------------------------------------------------
	// Arithmetic Operations
	public NFVector2 oper_add(NFVector2 v) {
		NFVector2 xV = new NFVector2();

		xV.x = this.x + v.x;
		xV.y = this.y + v.y;
		return xV;
	}

	public NFVector2 oper_sub(NFVector2 v) {
		NFVector2 xV = new NFVector2();

		xV.x = this.x - v.x;
		xV.y = this.y - v.y;
		return xV;
	}

	public NFVector2 oper_sub() {
		return new NFVector2(-x, -y);
	}

	public NFVector2 oper_mul(float s) {
		return new NFVector2(x * s, y * s);
	}

	public NFVector2 oper_division(float s) {
		if (Math.abs(s) > 0.001f) {
			return new NFVector2(x / s, y / s);
		}

		return Zero();
	}

	// ----------------------------------------------------------------------------
	// Arithmetic Updates
	public NFVector2 oper_addeq(NFVector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}

	public NFVector2 oper_subeq(NFVector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public NFVector2 oper_muleq(float s) {
		x *= s;
		y *= s;
		return this;
	}

	public NFVector2 oper_division_eq(float s) {
		// if (Math.abs(s) > 0.001f)
		{
			return new NFVector2(x / s, y / s);
		}

		// return Zero();
	}

	public float X() {
		return this.x;
	}

	public float Y() {
		return this.y;
	}

	public void SetX(float x) {
		this.x = x;
	}

	public void SetY(float y) {
		this.y = y;
	}

	public boolean IsZero() {
		return x < 0.001f && y < 0.001f;
	}

	// ----------------------------------------------------------------------------
	public float SquaredMagnitude() {
		return x * x + y * y;
	}

	// ----------------------------------------------------------------------------
	public float SquaredLength() {
		return SquaredMagnitude();
	}

	// ----------------------------------------------------------------------------
	public float Magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	// ----------------------------------------------------------------------------
	public float Length() {
		return Magnitude();
	}

	// ----------------------------------------------------------------------------
	public NFVector2 Direction() {
		if (this.IsZero()) {
			return Zero();
		}

		float lenSquared = SquaredMagnitude();
		float invSqrt = (float) (1.0f / Math.sqrt(lenSquared));
		return new NFVector2(x * invSqrt, y * invSqrt);
	}

	// ----------------------------------------------------------------------------
	public NFVector2 Normalized() {
		return Direction();
	}

	// ----------------------------------------------------------------------------
	public float Distance(NFVector2 v) {
		NFVector2 vX = this.oper_sub(v);
		return vX.Length();
	}

	public String ToString() {
		return String.valueOf(x) + "," + String.valueOf(this.y);
	}

	// Special values.
	public static NFVector2 Zero() {
		NFVector2 v = new NFVector2(0, 0);
		return v;
	}

	public static NFVector2 One() {
		NFVector2 v = new NFVector2(1, 1);
		return v;
	}

	public static NFVector2 UnitX() {
		NFVector2 v = new NFVector2(1, 0);
		return v;
	}

	public static NFVector2 UnitY() {
		NFVector2 v = new NFVector2(0, 1);
		return v;
	}

	public boolean FromString(String value) {

		String[] values = new String[2];
		values = value.split(",");// Split(value, values, ",");
		if (values.length != 2) {
			return false;
		}
		x = Float.valueOf(values[0]);
		y = Float.valueOf(values[1]);

		return true;
	}

	// boolean Split( String str, Vector<String> result, String delim)
	// {
	// // if (str.isEmpty()))
	// // {
	// // return false;
	// // }
	// //
	// // String tmp;
	// // int pos_begin = str.indexOf(delim);//.find_first_not_of(delim);
	// // int pos = 0;
	// // while (pos_begin != -1)
	// // {
	// // pos = str.find(delim, pos_begin);
	// // if (pos != -1)
	// // {
	// // tmp = str.substr(pos_begin, pos - pos_begin);
	// // pos_begin = pos + delim.length();
	// // }
	// // else
	// // {
	// // tmp = str.substr(pos_begin);
	// // pos_begin = pos;
	// // }
	// //
	// // if (!tmp.empty())
	// // {
	// // result.push_back(tmp);
	// // tmp.clear();
	// // }
	// // }
	// return true;
	// }

}
