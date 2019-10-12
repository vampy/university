package MaxAppGMV.MaxAppAMV;

public class MaxAppMain {
	private static MaxApp ma;	
	
	 public static void main(String[] args) {
		 int nn=3;
		 int[] xx=new int[3];
		 
		 xx[0]=1;xx[1]=2;xx[2]=3;
		 ma=new MaxApp(xx);
		 ma.DetMAxApp();
		System.out.println("Numerele: \n ");
		for(int j=0;j<nn;j++)
			 System.out.println(" the "+(j+1)+"th element= "+ma.getElemAtIndex(j));
			
	    }
	 }