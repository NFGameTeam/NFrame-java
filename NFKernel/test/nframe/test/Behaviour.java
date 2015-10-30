/**
 * 
 */
package nframe.test;

import static org.junit.Assert.*;

import org.junit.Test;

import nframe.NFBehaviour;

/**
 * @author Xiong
 * NF基础行为测试
 */
public class Behaviour {
	
	/**
	 * @author Xiong
	 * 定义一个自己的行为来测试
	 */
	private class MyBehaviour extends NFBehaviour {

		@Override
		public void init() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterInit() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeShut() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void shut() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Test
	public void test() {
		NFBehaviour behav = new MyBehaviour();
		behav.init();
		behav.afterInit();
		behav.execute();
		behav.beforeShut();
		behav.shut();
		assertTrue(true);
	}

}
