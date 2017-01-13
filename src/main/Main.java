package main;

import ann.ANN;
import ip.ImgProcessing;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

/**
 * Created by g on 12/21/16.
 */


public class Main extends JFrame{
    private JTextField directory = new JTextField(40);
    private JButton browse = new JButton("Browse");
    private JButton scan = new JButton("Scan");
    private JLabel label = new JLabel("Direcory");
    public Main(){
        setLayout(new FlowLayout());
        add(label);
        add(directory);
        add(scan);

        scan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mat imgOrig = Imgcodecs.imread(directory.getText());
                ImgProcessing ip = new ImgProcessing();
                ANN ann = new ANN();

                Mat bgs = ip.bgSubtraction(imgOrig);
                Mat nf = ip.noiseFilter(bgs);
                Mat saver = ip.makeCrop(imgOrig,nf);

                String state = ann.analyze("outputs/imgCropped0.jpg");
                System.out.println("STATE: "+state);
                double hist = ip.histoGram(imgOrig, nf);
                System.out.print("Histogram result: ");
                if(hist<=10.0f)
                    System.out.println("Severely Bad");
                else if(hist>10.0f&&hist<=20.0f)
                    System.out.println("Bad");
                else if(hist>20.0f&&hist<=30.0f)
                    System.out.println("Below Average");
                else if(hist>30.0f&&hist<=40.0f)
                    System.out.println("Average");
                else if(hist>40.0f)
                    System.out.println("Healthy");
            }
        });

    }


    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Main gui = new Main();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(800,80);
        gui.setVisible(true);
        gui.setTitle("Crop Analyzer");
        //Imgprocessing:
        //Imgproc- image processing library galing sa opencv
        //Core - for image conversion halos ung mga functions
        //Imgcodecs - for read and writing images
        //Mat - matrix variable, container ng images
        //MatOfPoint - point matrix (lagayang ng points o floating points) pang contour
        //contour - object
        //CvType - ito ung type ng matrix
                // - binary matrix - isa lng  (1)
                // - colored - (3) colorspace (r,g,b)

        //Steps for image classification:
        //Background subtraction - tanggalin ung background, example blue background
        //Image filter o Noise filter - tanggalin ung noise after ng subtraction
        //Segmentation - kukunin na ung mga contours
        //Object filter - tanggalin ung unwanted object na nadetect
        //Contour Cropping - save ung mga gustong objects
        //Classification - gamitin ung ANN pang classify ng object


        //Connect Opencv Libraries to the project (Initialization)
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //Load Image
        /*Scanner scanner = new Scanner(System.in);
        System.out.print("Enter img directory: ");
        String imgDir = scanner.nextLine();
        Mat imgOrig = Imgcodecs.imread(imgDir);
        ImgProcessing ip = new ImgProcessing();
        Mat bgs = ip.bgSubtraction(imgOrig);
        Mat nf = ip.noiseFilter(bgs);
        ip.makeCrop(imgOrig,nf);*/
    }
}
