/**
 *
 * @author Soumyava
 */

import java.util.*;
import java.io.*;

class Pair{
	long vi;
	long vj;
	
	@Override
	public String toString() {
		return "Pair [vi=" + vi + ", vj=" + vj + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (vi != other.vi)
			return false;
		if (vj != other.vj)
			return false;
		return true;
	}

	public Pair(long i, long j){
		vi = i;
		vj = j;
	}
	
	public Pair() {
		vi =0;
		vj=0;
	}

	public long getVi() {
		return vi;
	}
	public void setVi(long vi) {
		this.vi = vi;
	}
	public long getVj() {
		return vj;
	}
	public void setVj(long vj) {
		this.vj = vj;
	}
	
	public void set(long i, long j){
		vi = i;
		vj = j;
	}
}

public class RMAT {
    static Random randomGen = new Random();

    // parameters for the Kronecker graph generator
    final static double a = 0.57;
    final static double b = 0.19;
    final static double c = 0.19;
    final static double d = 1-(a+b+c);
    final static int na = (int)(Integer.MAX_VALUE*a);
    final static int nb = (int)(Integer.MAX_VALUE*(a+b));
    final static int nc = (int)(Integer.MAX_VALUE*(a+b+c));

    static long vi;
    static long vj;

    private FileWriter fileWritter = null;
    private BufferedWriter bufferWritter = null; 
    private static PrintWriter foutput;
    
    public static PrintWriter openWriteFile(String fileName){
        PrintWriter outputFile = null;
        try{
            outputFile = new PrintWriter(new FileWriter(fileName));
        }     
        catch(IOException IOE){    
          outputFile = null;
        }
       finally{
          return outputFile;
       }
    }
    
    
    static void generateEdge ( long N ) {
	long imin = 0;
	long imax = N;
	long jmin = 0;
	long jmax = N;
	do {
	    int n = Math.abs(randomGen.nextInt());
	    if (n <= na) {
		imax = (imin+imax) >> 1;
		jmax = (jmin+jmax) >> 1;
	    } else if (n <= nb) {
		imin = (imin+imax) >> 1;
		jmax = (jmin+jmax) >> 1;
	    } else if (n <= nc) {
		imax = (imin+imax) >> 1;
		jmin = (jmin+jmax) >> 1;
	    } else {
		imin = (imin+imax) >> 1;
		jmin = (jmin+jmax) >> 1;
	    }
	} while ((imin != imax) || (jmin != jmax));
	vi = imin;
	vj = jmin;
	if(vi==vj)
            generateEdge(N);
    }

    public static void main ( String[] args ) throws Exception {
	long N;   // nodes
	long M;   // edges
        int vLabels;
        int eLabels;
        HashMap<Integer,String> vMap = new HashMap<>();
	if (args.length == 0) {
	    BufferedReader str = new BufferedReader(new InputStreamReader(System.in));
	    System.err.print("Nodes? ");
	    N = Long.parseLong(str.readLine());
	    System.err.print("Edges? ");
	    M = Long.parseLong(str.readLine());
            System.err.print("Unique Vertex Labels ? ");
            vLabels = Integer.parseInt(str.readLine());
            System.err.print("Unique Edge Labels ? ");
            eLabels = Integer.parseInt(str.readLine());
            System.err.println("Output file ?");
            foutput = openWriteFile(str.readLine());
            
	} else if(args.length == 5){
	    N = Long.parseLong(args[0]);
	    M = Long.parseLong(args[1]);
            vLabels = Integer.parseInt(args[2]);
            eLabels = Integer.parseInt(args[3]);
            foutput = openWriteFile(args[4]);
	}
	else{
            System.err.println("Incorrect args: specify #V, #E, #VL, #EL");
            System.err.println("Switching to default values");
            N = 10;
            M = 50;
            vLabels = 5;
            eLabels = 10;
            foutput = openWriteFile("out.txt");
	}
	System.out.println("This might generate duplicate edges");
	System.out.println("Write a post processor to remove duplicates");
	int i=0;
	int j=0;
        Random r;
        int low = 0;
        int result;
	for(j=0;j<N-1;j++){
            r = new Random();
            result = r.nextInt(vLabels-low) + low;
            if(!vMap.containsKey(j))
                vMap.put(j, "v"+result);
            result = r.nextInt(vLabels-low) + low;
            if(!vMap.containsKey(j+1))
                vMap.put(j+1, "v"+result);
            result = r.nextInt(eLabels-low)+low;
            //System.out.println("e"+result+","+j+","+vMap.get(j)+","+(j+1)+","+vMap.get(j+1));
            foutput.println("e"+result+","+j+","+vMap.get(j)+","+(j+1)+","+vMap.get(j+1));
	    
	}
        r = new Random();
        result = r.nextInt(eLabels-low)+low;
        //System.out.println("e"+result+","+(N-1)+","+vMap.get((int)N-1)+",0,"+vMap.get(0));
        foutput.println("e"+result+","+(N-1)+","+vMap.get((int)N-1)+",0,"+vMap.get(0));
        i=j;
	while( i < M ) {
	    generateEdge(N);
            result = r.nextInt(eLabels-low)+low;
            int source = (int)vi;
            int dest = (int)vj;
            //System.out.println("e"+result+","+source+","+vMap.get(source)+","+dest+","+vMap.get(dest));
            foutput.println("e"+result+","+source+","+vMap.get(source)+","+dest+","+vMap.get(dest));
	    i++;    	
        }
        foutput.close();
    }
}

