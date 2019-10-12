package MaxAppGMV.MaxAppAMV;

public class MaxApp {

	private int[] x;

	
	public MaxApp(int[] x){
		this.x=x.clone();		
	}
	
	public int getN(){
		return this.x.length;
	}
	
	
	public int getElemAtIndex(int i){
		return x[i];
	}
	
	
	public int DetMAxApp(){
		if (x.length > 100 )
			return -1;
		int k=0;
		int i=0, p=0;
		while (i<this.x.length)
		{
			if (x[i] <0) return -1;
			
			if (x[i]>x[p])
			{
				p=i;
				k=1;
			}
			else
				if (x[p]==x[i])
					k++;
			i++;
		}
		return k;
	}		
}


