package MaxAppGMV.MaxAppAMV;


import static org.junit.Assert.*;




import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;


public class MaxAppBVA {

	private MaxApp ma;	
	private int[] xx;
	
		
	
	public void TC_BVA_01_DetMAxApp() {		
		xx=new int[1];
		xx[0]=2;
		ma=new MaxApp(xx);		
		assertEquals(1, ma.DetMAxApp());
	}	
	@Test
	public void TC_BVA_02_DetMAxApp() {		
		xx=new int[100];
		for (int j=0;j<100-1;j++)
			xx[j]=j;
		ma=new MaxApp(xx);
		assertEquals(1, ma.DetMAxApp());
	}
	@Test
	public void TC_BVA_03_DetMAxApp() {		
		xx=new int[100];
		for (int j=0;j<100;j++)
			xx[j]=j;
		ma=new MaxApp(xx);
		assertEquals(1, ma.DetMAxApp());
	}
	@Test
	public void TC_BVA_04_DetMAxApp() {		
		xx=new int[2];
		xx[0]=1;xx[1]=0;
		ma=new MaxApp(xx);		
		assertEquals(1, ma.DetMAxApp());
	}	
	
	@Ignore
	@Test
	public void TC_BVA_05_DetMAxApp() {
		xx = new int[1];
		xx[0]=-1;
		ma=new MaxApp(xx);
		int k= ma.DetMAxApp();
		//assertEquals(-1, k);		
	}
	
	@Test
	public void TC_BVA_06_DetMAxApp() {		
		xx=new int[2];
		xx[0]=1;xx[1]=2;
		ma=new MaxApp(xx);
		assertEquals(1, ma.DetMAxApp());
	}	
	@Test
	public void TC_BVA_07_DetMAxApp() {		
		xx=new int[2];
		xx[0]=1;xx[1]=Integer.MAX_VALUE-1;
		ma=new MaxApp(xx);
		assertEquals(1, ma.DetMAxApp());
	}
	@Test
	public void TC_BVA_08_DetMAxApp() {		
		xx=new int[2];
		xx[0]=1;xx[1]=Integer.MAX_VALUE;
		ma=new MaxApp(xx);
		assertEquals(1, ma.DetMAxApp());
	}
	
	@Ignore
	@Test
	public void TC_BVA_09_DetMAxApp() {		
		xx=new int[Integer.MAX_VALUE-1];
		for (int j=0;j<Integer.MAX_VALUE-1;j++)
			xx[j]= 1;
		xx[Integer.MAX_VALUE-1]=2;
		
		ma=new MaxApp(xx);
		assertEquals(Integer.MAX_VALUE-1, ma.DetMAxApp());
	}
	
	@Ignore
	@Test
	public void TC_BVA_10_DetMAxApp() {		
		xx=new int[Integer.MAX_VALUE];
		for (int j=0;j<Integer.MAX_VALUE;j++)
			xx[j]= 1;
		
		ma=new MaxApp(xx);
		assertEquals(Integer.MAX_VALUE, ma.DetMAxApp());
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
