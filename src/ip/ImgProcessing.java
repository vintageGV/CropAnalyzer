package ip;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created by g on 12/21/16.
 */
public class ImgProcessing {
    //B,G,R
    //private final Scalar threshlow = new Scalar(0,0,170);//red
    //private final Scalar threshhigh = new Scalar(10,10,210);//red
    private final Scalar threshlow = new Scalar(210,210,210);//white
    private final Scalar threshhigh = new Scalar(260,260,260);//white

    public double histoGram(Mat orig, Mat binary){
        Mat ret = orig.clone();

        ArrayList<MatOfPoint> contour = new ArrayList<>();
        Imgproc.findContours(binary.clone(),contour,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        Mat blackBG = orig.clone();

        //System.out.println("Channel: "+blackBG.channels());

        //System.out.println("Channel: "+orig.channels());

        for(int i=0;i<binary.rows();i++){
            for(int j=0;j<binary.cols();j++){
                double[] data = binary.get(i,j);
        //        System.out.println(":"+data[0]);
                if(data[0]==255.0f){
                    //System.out.println("blackBG: "+blackBG.channels());
                    //System.out.println("orig: "+orig.channels());
                    double val[] = orig.get(i,j);
                    blackBG.put(i,j,new double[]{val[0],val[1],val[2]});
                }
                else{

                    blackBG.put(i,j,new double[]{0f,0f,0f});
                }
            }
        }

        Imgcodecs.imwrite("outputs/blackBG.jpg",blackBG);

        /*for(int i=0;i<contour.size();i++){
            Rect rect = Imgproc.boundingRect(contour.get(i));
            Imgcodecs.imwrite("outputs/blackCropped"+i+".jpg",blackBG.submat(rect));
            blackBG = orig.submat(rect).clone();
        }
        */
        //Imgproc.cvtColor(ret,ret,Imgproc.COLOR_BGR2HSV );
        double[] aveColor = new double[3];
        int count = 0;
        for(int i=0;i<blackBG.rows();i++){
            for(int j=0;j<blackBG.cols();j++){
                double[] data = blackBG.get(i,j);
                count++;
                aveColor[0]+=data[0];
                aveColor[1]+=data[1];
                aveColor[2]+=data[2];
            }
        }
        aveColor[0]/=count;
        aveColor[1]/=count;
        aveColor[2]/=count;
        System.out.println("B: "+aveColor[0]);
        System.out.println("G: "+aveColor[1]);
        System.out.println("R: "+aveColor[2]);

        double result = Math.abs(100-((aveColor[2]/aveColor[1])*100));
        System.out.println("Result: "+result);
        //Imgproc.cal
        return result;
    }
    public Mat bgSubtraction(Mat orig){
        Mat ret = orig.clone();
        //pang convert ng color
        //Imgproc.cvtColor(ret,ret,Imgproc.COLOR_BGR2HSV);
        Core.inRange(ret,threshlow,threshhigh,ret);
        //Imgproc.threshold(ret,ret,200,255,Imgproc.THRESH_BINARY);
        Core.bitwise_not(ret,ret);
        return ret;
    }

    public Mat noiseFilter(Mat binary){
        Mat ret = binary.clone();
        //set number of erosion and dilation
        int numEro = 3;
        int numDilate = 3;

        for(int i=0;i<numEro;i++)
            Imgproc.erode(ret,ret,new Mat());
        for(int i=0;i<numDilate;i++)
            Imgproc.dilate(ret,ret,new Mat());
        return ret;

    }

    public Mat makeCrop(Mat orig, Mat binary){
        ArrayList<MatOfPoint> contour = new ArrayList<>();
        Imgproc.findContours(binary.clone(),contour,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        //Imgcodecs.imwrite("outputs/binaryOutput.jpg",binary);
        Mat cropped = null;
        for(int i=0;i<contour.size();i++){
            Rect rect = Imgproc.boundingRect(contour.get(i));
            Imgcodecs.imwrite("outputs/binaryCropped"+i+".jpg",binary.submat(rect));
            Imgcodecs.imwrite("outputs/imgCropped"+i+".jpg",orig.submat(rect));
            cropped = orig.submat(rect).clone();
        }
        return cropped;
    }

    public BufferedImage matToBuff(Mat img){
        int type = 0;
        if(img.channels()==0)
            type=BufferedImage.TYPE_BYTE_BINARY;
        else
            type=BufferedImage.TYPE_3BYTE_BGR;

        BufferedImage ret = new BufferedImage(img.width(),img.height(),type);
        WritableRaster raster = ret.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        img.get(0,0,data);
        return ret;
    }


}
