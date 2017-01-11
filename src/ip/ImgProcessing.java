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
    private final Scalar threshlow = new Scalar(0,0,170);
    private final Scalar threshhigh = new Scalar(10,10,210);
    //private final Scalar threshlow = new Scalar(210,210,210);
    //private final Scalar threshhigh = new Scalar(255,255,255);

    public Mat bgSubtraction(Mat orig){
        System.out.println("test");
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
        System.out.println("test3");
        return ret;

    }

    public void makeCrop(Mat orig, Mat binary){
        ArrayList<MatOfPoint> contour = new ArrayList<>();
        Imgproc.findContours(binary.clone(),contour,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        Imgcodecs.imwrite("outputs/binaryOutput.png",binary);
        for(int i=0;i<contour.size();i++){
            Rect rect = Imgproc.boundingRect(contour.get(i));
            Imgcodecs.imwrite("outputs/binaryCropped.png",binary.submat(rect));
            Imgcodecs.imwrite("outputs/imgCropped.png",orig.submat(rect));
        }
    System.out.println("test2");
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
