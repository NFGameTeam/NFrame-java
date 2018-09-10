/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package com.noahframe.nfcore.iface.math;

public class NFVector3 {

	private float x,y,z;
	private void InitData()
	{
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}
	
	public NFVector3()
	{
		InitData();
	}
	
	public NFVector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public NFVector3(NFVector2 v, float z)
	{
		this.x = v.X();
		this.y = v.Y();
		this.z = z;
	}

	public NFVector3(float[] coordinate)
	{
		this.x = coordinate[0];
		this.y = coordinate[1];
		this.z = coordinate[2];
	}

	public NFVector3(double[] coordinate)
	{
		this.x = (float)coordinate[0];
		this.y = (float)coordinate[1];
		this.z = (float)coordinate[2];
	}

	public NFVector3(NFVector3 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	//----------------------------------------------------------------------------
	public boolean oper_lt( NFVector3 v)
		{
			return this.Length() < v.Length() ;
		}

	public boolean oper_gt( NFVector3 v)
		{
			return this.Length() > v.Length();
		}

	public NFVector3 oper_eq ( NFVector3 v)
		{
			this.x = v.x;
			this.y = v.y;
			this.z = v.z;

			return this;
		}

	public boolean oper_heq ( NFVector3 v)
		{
			return Math.abs(this.x - v.x) < 0.001f && Math.abs(this.y - v.y) < 0.001f && Math.abs(this.z - v.z) < 0.001f;
		}

	public boolean oper_ne ( NFVector3 v)
		{
			return Math.abs(this.x - v.x) >= 0.001f && Math.abs(this.y - v.y) >= 0.001f && Math.abs(this.z - v.z) >= 0.001f;
		}

		//----------------------------------------------------------------------------
	    // Arithmetic Operations
	public NFVector3 oper_add ( NFVector3 v)
		{
			NFVector3 xV=new NFVector3();

			xV.x = this.x + v.x;
			xV.y = this.y + v.y;
			xV.z = this.z + v.z;
			return xV;
		}

	public NFVector3 oper_sub ( NFVector3 v)
		{
			NFVector3 xV=new NFVector3();

			xV.x = this.x - v.x;
			xV.y = this.y - v.y;
			xV.z = this.z - v.z;
			return xV;
		}

	public NFVector3 oper_sub ()
		{
			return new NFVector3(-x, -y, -z);
		}

	public NFVector3 oper_mul (float s)
		{
			return new NFVector3(x * s, y * s, z * s);
		}

	public NFVector3 oper_division (float s)
		{
			if (Math.abs(s) > 0.001f)
			{
				return new NFVector3(x / s, y / s, z / s);
			}

			return Zero();
		}


		//----------------------------------------------------------------------------
	    // Arithmetic Updates
	public NFVector3 oper_addeq ( NFVector3 v)
		{
			x += v.x;
			y += v.y;
			z += v.z;
			return this;
		}

	public NFVector3 oper_subeq ( NFVector3 v)
		{
			x -= v.x;
			y -= v.y;
			z -= v.z;
			return this;
		}

	public NFVector3 oper_muleq (float s)
		{
			x *= s;
			y *= s;
			z *= s;
			return this;
		}

	public NFVector3 oper_division_eq (float s)
		{
			//if (Math.abs(s) > 0.001f)
			{
				return new NFVector3(x / s, y / s, z / s);
			}

			//return Zero();
		}

		//----------------------------------------------------------------------------
	public float X() 
		{
			return this.x;
		}

	public float Y() 
		{
			return this.y;
		}

	public float Z() 
		{
			return this.z;
		}

	public void SetX(float x)
		{
			this.x = x;
		}

	public void SetY(float y)
		{
			this.y = y;
		}

	public void SetZ(float z)
		{
			this.z = z;
		}

		//----------------------------------------------------------------------------
	public boolean IsZero() 
		{
			return Math.abs(x) < 0.001f && Math.abs(y) < 0.001f && Math.abs(z) < 0.001f;
		}
		//----------------------------------------------------------------------------
		public float SquaredMagnitude() 
		{
			return x*x + y*y + z*z;
		}

		//----------------------------------------------------------------------------
		public float SquaredLength()  
		{
			return SquaredMagnitude();
		}

		//----------------------------------------------------------------------------
		public float Magnitude()  
		{
			return (float) Math.sqrt(x*x + y*y + z*z);
		}

		//----------------------------------------------------------------------------
		public float Length()  
		{
			return Magnitude();
		}

		//----------------------------------------------------------------------------
		public NFVector3 Direction()
		{
			if (this.IsZero())
			{
				return Zero();
			}

			float lenSquared = SquaredMagnitude();
			float invSqrt = 1.0f / (float) Math.sqrt(lenSquared);
			return new NFVector3(x * invSqrt, y * invSqrt, z * invSqrt);
		}

		//----------------------------------------------------------------------------
	    public NFVector3 Normalized()
	    {
	        return Direction();
	    }

		//----------------------------------------------------------------------------
		float Distance( NFVector3 v)
		{
			NFVector3 vX = this.oper_sub(v);
			return vX.Length();
		}

		//----------------------------------------------------------------------------
		public boolean FromString( String value)
		{
			String[] values=new String[3];
			values=value.split(",");//Split(value, values, ",");
			if (values.length != 3)
			{
				return false;
			}
			x = Float.valueOf(values[0]);
			y = Float.valueOf(values[1]);
			z = Float.valueOf(values[2]);

			return true;
		}

		//----------------------------------------------------------------------------
		public String ToString() 
		{
			return String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z);
		}

	    // Special values.
	    public static  NFVector3 Zero()
	    {
	        NFVector3 v=new NFVector3(0, 0, 0);
	        return v;
	    }
	    public static  NFVector3 One()
	    {
	    	NFVector3 v=new NFVector3(1, 1, 1);
	        return v;
	    }
	    public static  NFVector3 UnitX()
	    {
	    	NFVector3 v=new NFVector3(1, 0, 0);
	        return v;
	    }
	    public static  NFVector3 UnitY()
	    {
	    	NFVector3 v=new NFVector3(0, 1, 0);
	        return v;
	    }
		public static  NFVector3 UnitZ()
		{
			NFVector3 v=new NFVector3(0, 0, 1);
			return v;
		}

		public NFVector2 xx()
		{
			return new NFVector2(this.x, this.x);
		}

		public NFVector2 yx()
		{
			return new NFVector2(this.y, this.x);
		}

		public NFVector2 zx()
		{
			return new NFVector2(this.z, this.x);
		}

		public NFVector2 xy()
		{
			return new NFVector2(this.x, this.y);
		}

		public NFVector2 yy()
		{
			return new NFVector2(this.y, this.y);
		}

		public NFVector2 zy()
		{
			return new NFVector2(this.z, this.y);
		}

		public NFVector2 xz()
		{
			return new NFVector2(this.x, this.z);
		}

		public NFVector2 yz()
		{
			return new NFVector2(this.y, this.z);
		}

		public NFVector2 zz()
		{
			return new NFVector2(this.z, this.z);
		}

//		boolean Split( String str, std::vector<String>& result, String delim)
//		{
//			if (str.empty())
//			{
//				return false;
//			}
//
//			String tmp;
//			size_t pos_begin = str.find_first_not_of(delim);
//			size_t pos = 0;
//			while (pos_begin != String::npos)
//			{
//				pos = str.find(delim, pos_begin);
//				if (pos != String::npos)
//				{
//					tmp = str.substr(pos_begin, pos - pos_begin);
//					pos_begin = pos + delim.length();
//				}
//				else
//				{
//					tmp = str.substr(pos_begin);
//					pos_begin = pos;
//				}
//
//				if (!tmp.empty())
//				{
//					result.push_back(tmp);
//					tmp.clear();
//				}
//			}
//			return true;
//		}
	
	
	
}
