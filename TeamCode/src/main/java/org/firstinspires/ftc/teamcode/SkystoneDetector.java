package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.OpenCVPipeline;
import com.disnodeteam.dogecv.filters.HSVRangeFilter;
import com.disnodeteam.dogecv.math.Line;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SkystoneDetector extends OpenCVPipeline {
	private final HSVRangeFilter rangeFilter = new HSVRangeFilter(
			new Scalar(8.5, 127.5, 127.5),
			new Scalar(53.125, 255, 255)
	);
	private final Telemetry telemetry;

	SkystoneDetector(Telemetry telemetry) {
		VUFORIA_KEY = "AZLv+a7/////AAAAGdyzndpq4khMnz5IMjSvhiR0XbtOlL7ZfQ" +
				"ytGj9s4zFCFoa+IqUA1Cjv4ghfSjfRAlRguu6cVbQVM+0Rxladi3AIKh" +
				"UjIL6v5ToFrK/fxrWdwAzkQfEPM1S3ijrTSm1N8DuZ6UoqiKoVmQGzyi" +
				"WhDpTQoR1zIVkj88rOhBDYwBf0CnW++pxZ0pHlQBbh/bzBjt63ANcuI9" +
				"JyHU3/JLGSBhoIm04G3UnrjVrjKfPFlX9NOwWQLOYjQ+4B1l4M8u9Bdi" +
				"hYgmfMST0BHON+MQ7qC5dMs/2OSZlSKSZISN/L+x606xzc2Sv5G+ULUp" +
				"aUiChG7Zlv/rncu337WhZjJ1X2pQGY7gIBcSH+TUw81n2jYKkm";
		this.telemetry = telemetry;
	}

	@Override
	public Mat processFrame(Mat rgba, Mat gray) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		Mat colorMask = new Mat(rgba.size(), CvType.CV_8UC4);
		rangeFilter.process(rgba.clone(), colorMask);

		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(colorMask, contours, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		double maxArea = 0;
		int maxAreaIndex = 0;
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea(contours.get(i));

			if (area > maxArea) {
				maxArea = area;
				maxAreaIndex = i;
			}
		}

		Imgproc.drawContours(rgba, contours, maxAreaIndex, new Scalar(0, 255, 0), 2);

		if (maxArea > 0) {
			for (Point stoneCenter : stoneCenters(contours.get(maxAreaIndex))) {
				Imgproc.circle(rgba, stoneCenter, 1, new Scalar(255, 0, 0), 34);
				telemetry.addData("Visible", stoneCenter.inside(new Rect(new Point(), rgba.size())));
			}
			telemetry.update();
		}

		return rgba;
	}

	private Point[] stoneCenters(MatOfPoint contour) {
		MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

		RotatedRect boundingBox = Imgproc.minAreaRect(contour2f);
		Point[] boundingPoints = new Point[4];
		boundingBox.points(boundingPoints);

		Line edge1 = new Line(boundingPoints[0], boundingPoints[1]);
		Line edge2 = new Line(boundingPoints[1], boundingPoints[2]);

		Line longEdge, shortEdge;
		if (edge1.compareTo(edge2) > 0) {
			longEdge = edge1;
			shortEdge = edge2;
		} else {
			shortEdge = edge1;
			longEdge = edge2;
		}

		double stoneDistX = (longEdge.x2 - longEdge.x1) / 6;
		double stoneDistY = (longEdge.y2 - longEdge.y1) / 6;
		Point shortEdgeCenter = shortEdge.center();

		Point[] centers = new Point[6];
		for (int i = 0; i < centers.length; i++) {
			centers[i] = new Point(
					(shortEdgeCenter.x + stoneDistX / 2) + stoneDistX * i,
					(shortEdgeCenter.y + stoneDistY / 2) + stoneDistY * i
			);
		}

		return centers;
	}
}

enum StoneType {
	STONE, SKYSTONE
}
