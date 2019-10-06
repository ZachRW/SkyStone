package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.OpenCVPipeline;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SkystoneDetector extends OpenCVPipeline {
	SkystoneDetector() {
		VUFORIA_KEY = "AZLv+a7/////AAAAGdyzndpq4khMnz5IMjSvhiR0XbtOlL7ZfQ" +
				"ytGj9s4zFCFoa+IqUA1Cjv4ghfSjfRAlRguu6cVbQVM+0Rxladi3AIKh" +
				"UjIL6v5ToFrK/fxrWdwAzkQfEPM1S3ijrTSm1N8DuZ6UoqiKoVmQGzyi" +
				"WhDpTQoR1zIVkj88rOhBDYwBf0CnW++pxZ0pHlQBbh/bzBjt63ANcuI9" +
				"JyHU3/JLGSBhoIm04G3UnrjVrjKfPFlX9NOwWQLOYjQ+4B1l4M8u9Bdi" +
				"hYgmfMST0BHON+MQ7qC5dMs/2OSZlSKSZISN/L+x606xzc2Sv5G+ULUp" +
				"aUiChG7Zlv/rncu337WhZjJ1X2pQGY7gIBcSH+TUw81n2jYKkm";
	}

	@Override
	public Mat processFrame(Mat rgba, Mat gray) {
		Mat threshold = new Mat(rgba.rows(), rgba.cols(), CvType.CV_8UC4);
		Core.inRange(rgba, new Scalar(130, 70, 0, 0), new Scalar(255, 220, 30, 255), threshold);

		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(threshold, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		Mat contourImg = new Mat(rgba.rows(), rgba.cols(), CvType.CV_8UC4);
		Imgproc.drawContours(contourImg, contours, -1, new Scalar(255, 255, 255));
		return contourImg;
	}
}
