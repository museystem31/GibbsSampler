import java.util.Random;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GibbsSampler {
	
	static String pickedVar = "a";
	public static String outputFile= "C:/Users/Muse/Desktop/sample.xls";
	
	public static void main(String[] args){
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			
			// initiating sample
			Tuple[] sample = new Tuple[4];
			Tuple a = new Tuple("a", 0);
			Tuple b = new Tuple("b", 1);
			Tuple c = new Tuple("c", 0);
			Tuple d = new Tuple("d",0);
			sample[0] = a;
			sample[1] = b;
			sample[2] = c;
			sample[3] = d;
			
			Random random = new Random();
			System.out.println("a="+sample[0].val+ ", b=" + sample[1].val + ", c=" + sample[2].val + ", d=" + sample[3].val);
			
			for(int i=0; i<65535 ; i++){
				
				System.out.println("Picked var: " + pickedVar);
				
				//generate a random float for sample use
				float randomVal = random.nextFloat();
				System.out.println(randomVal);
				
				if(pickedVar.equals("a")){
					//sample A
					sample[0].changeVal(sampleA(randomVal,sample[3]));
				}
				
				if(pickedVar.equals("c")){
					//sample C
					sample[2].changeVal(sampleC(randomVal,sample[3]));				
				}
				
				if(pickedVar.equals("d")){
					//sample D
					sample[3].changeVal(sampleD(randomVal,sample[0], sample[2]));
				}
			
				System.out.println("a="+sample[0].val+ ", b=" + sample[1].val + ", c=" + sample[2].val + ", d=" + sample[3].val);
				// locate the cell and write to the file
				HSSFRow row = sheet.createRow(i);
				for(int j=0 ; j < 4 ; j++){
					HSSFCell cell = row.createCell(j);
					cell.setCellValue(sample[j].val);
				}
				
				// pick another variable for generating the next sample
				updatePickedVar();
			}

			FileOutputStream fOut = new FileOutputStream(outputFile);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
			System.out.println("samples created.");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updatePickedVar(){
		// the order is a->c->d->a
		if (pickedVar.equals("a")){
			pickedVar = "c";
		}else if(pickedVar.equals("c")){
			pickedVar = "d";
		}else{
			pickedVar = "a";
		}
	}
	
	public static int sampleA(float random,Tuple d){
		int dVal = d.val;
		int newA = 0;
		
		if(dVal == 0){
			if(random > 0.0196){
				newA = 0;
			}else{
				newA = 1;
			}
		}else{
			if(random > 0.005){
				newA = 1;
			}else{
				newA = 0;
			}
		}
			
		return newA;
	}
	
	public static int sampleC(float random,Tuple d){
		int dVal = d.val;
		int newC = 0;
		
		if(dVal == 0){
			if(random > 0.0001){
				newC = 1;
			}else{
				newC = 0;
			}
		}else{
			if(random > 0.5){
				newC = 1;
			}else{
				newC = 0;
			}
		}
			
		return newC;
	}
	
	public static int sampleD(float random,Tuple a,Tuple c){
		int aVal = a.val;
		int cVal = c.val;
		
		int newD = 0;
		
		if(aVal == 0 && cVal == 0){
			if(random > 0.5){
				newD = 0;
			}else{
				newD = 1;
			}
		}else if(aVal == 1 && cVal == 0){
			if(random > 0.0001){
				newD = 1;
			}else{
				newD = 0;
			}
		}else if(aVal == 0 && cVal ==1){
			if(random > 0.0001){
				newD = 0;
			}else{
				newD = 1;
			}
		}else{
			if(random > 0.5){
				newD = 1;
			}else {
				newD = 0;
			}
		}
		return newD;
	}
}

class Tuple{
	String var;
	int val;
	
	public Tuple(String var, int val){
		this.var = var;
		this.val = val;
	}
	
	public void changeVal(int val){
		this.val = val;
	}
}
