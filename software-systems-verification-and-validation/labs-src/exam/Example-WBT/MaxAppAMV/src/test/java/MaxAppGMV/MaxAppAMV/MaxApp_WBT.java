package MaxAppGMV.MaxAppAMV;

import static org.junit.Assert.*;

import org.junit.Test;

public class MaxApp_WBT {

	private MaxApp ma;	
	private int[] xx;
	
		
	
	@Test
	public void TC_WBT_01_DetMAxApp() {		
		xx=new int[2];
		xx[0]=1;xx[1]=5;
		ma=new MaxApp(xx);		
		assertEquals(1, ma.DetMAxApp());
	}

}
