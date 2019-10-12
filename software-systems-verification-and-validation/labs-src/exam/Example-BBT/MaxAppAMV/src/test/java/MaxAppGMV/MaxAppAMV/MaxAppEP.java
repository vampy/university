package MaxAppGMV.MaxAppAMV;


import static org.junit.Assert.*;




import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

public class MaxAppEP {

	private MaxApp ma;	
	private int[] xx;
	
		
	@Test
	public void TC_EP_01_DetMAxApp() {
		xx = new int[3];
		xx[0]=2;xx[1]=3;xx[2]=2;
		ma=new MaxApp(xx);
		assertEquals(1, ma.DetMAxApp());	
	}
	@Test
	public void TC_EP_02_DetMAxApp() {		
		xx=new int[0];
		ma=new MaxApp(xx);
		assertEquals(0, ma.DetMAxApp());
	}
	
	@Ignore
	@Test
	public void TC_EP_03_DetMAxApp() {		
		xx=new int[1];
	//	xx[0]=3.5;
		ma=new MaxApp(xx);
		//assertEquals(1, ma.DetMAxApp());
	}
	@Ignore
	@Test
	public void TC_EP_04_DetMAxApp() {
		//xx = new int[-4];
		//xx[0]=2;xx[1]=3;xx[2]=3;xx[3]=3;
		//ma=new MaxApp(xx);
		//assertEquals(3, ma.DetMAxApp());	
	}
	@Ignore
	@Test
	public void TC_EP_05_DetMAxApp() {
		xx = new int[2];
		xx[0]=1;xx[1]=-3;
		ma=new MaxApp(xx);
		assertEquals(-1, ma.DetMAxApp());	
	}
	@Ignore
	@Test
	public void TC_EP_06_DetMAxApp() {
		xx = new int[3];
		//xx[0]=2;xx[1]=4.5;xx[2]=8;
	//	ma=new MaxApp(xx);
	//	assertEquals(3, ma.DetMAxApp());	
	}
	
	@Ignore
	@Test
	public void TC_EP_07_DetMAxApp() {		
		
	}
	@Ignore
	@Test
	public void TC_EP_08_DetMAxApp() {
			
	}
	@Ignore
	@Test
	public void TC_EP_09_DetMAxApp() {
			
	}
	@Ignore
	@Test
	public void TC_EP_10_DetMAxApp() {
			
	}
}
