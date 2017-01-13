package ann;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by g on 1/14/17.
 */
public class ANN {
    public String analyze(String imgDir){
        String ret = "";
        try{
            Process p = Runtime.getRuntime().exec("python resources/ANN/label_image.py "+imgDir);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String output = null;

            while((output = stdInput.readLine())!=null){
                String cmdRet[] = output.split(":");
                if(cmdRet.length>1){
                    if(cmdRet[0].trim().equals("state")){
                        ret = cmdRet[1];
                    }
                    else
                        ret = "no classification";
                }
            }

        }catch (Exception e){
            System.out.println("ANN failed");
        }
        return ret;
    }
}
