package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.filters.HSVRangeFilter;
import com.disnodeteam.dogecv.math.Line;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCameraException;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can identify where skystones are in a line of stones.
 */
public class SkystoneDetector extends OpenCvPipeline {
	/**
	 * Creates a mask of pixels within a color range.
	 * This one finds yellowish pixels.
	 * <p>
	 * The bounds are in HSV with strange ranges:
	 * H: [0, 180]
	 * S: [0, 255]
	 * V: [0, 255]
	 */
	private final HSVRangeFilter rangeFilter = new HSVRangeFilter(
			new Scalar(8.5, 127.5, 127.5),
			new Scalar(53.125, 255, 255)
	);

	/**
	 * The current frame of the video feed.
	 */
	private Mat frame;

	/**
	 * The image to display on the robot controller phone.
	 */
	private Mat display;

	private Telemetry telemetry;

	SkystoneDetector(Telemetry telemetry) {
		this.telemetry = telemetry;
	}

	@Override
	public Mat processFrame(Mat input) {
		// Save the frame
		if (!input.empty()) {
			if (frame != null) {
				frame.release();
			}
			frame = input.clone();
		}

		// If the display Mat is null show the frame,
		// otherwise show display.
		if (display == null || display.cols() != input.cols() || display.rows() != input.rows()) {
			return input;
		}
		return display;
	}

	/**
	 * Finds the positions of the skystones.
	 * The positions are not pixel coordinates;
	 * they are the indexes
	 * (i. e. the <i>n</i>th and <i>k</i>th stones
	 * are skystones.)
	 *
	 * @return a list of the positions of skystones.
	 */
	public List<Integer> skystonePositions() {
		// Return an empty list is frame is null
		if (frame == null || frame.empty()) {
			return new ArrayList<>();
		}

		// Create image to draw on
		Mat drawImage = frame.clone();

		// Make a color mask of yellowish pixels
		Mat colorMask = new Mat(frame.size(), CvType.CV_8UC4);
		if (frame.empty()) {
			telemetry.addLine("Empty frame");
			telemetry.update();
			return null;
		}
		try {
			rangeFilter.process(frame.clone(), colorMask);
		} catch (OpenCvCameraException e) {
			telemetry.addData("Error", e.getMessage());
			telemetry.update();
			return null;
		}

		// Find the contours (edges) of the mask
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(colorMask, contours, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		// Use the contours to find the largest yellowish shape.
		// This is presumably the line of stones.
		double maxArea = 0;
		int maxAreaIndex = 0;
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea(contours.get(i));

			if (area > maxArea) {
				maxArea      = area;
				maxAreaIndex = i;
			}
		}

		// Outline the line of stones
		Imgproc.drawContours(drawImage, contours, maxAreaIndex, new Scalar(0, 255, 0), 2);

		// Create list of positions of the skystones
		List<Integer> positions = new ArrayList<>();

		// If the shape is larger than a threshold
		if (maxArea > 100) {
			// Get where the stone centers should be
			Point[] stoneCenters = stoneCenters(contours.get(maxAreaIndex));

			for (int i = 0; i < stoneCenters.length; i++) {
				// Draw the center points
				Imgproc.circle(drawImage, stoneCenters[i], 1, new Scalar(255, 0, 0), 10);

				// If the center point is not yellowish, it is a skystone
				double[] maskPixel = colorMask.get((int) stoneCenters[i].y, (int) stoneCenters[i].x);
				if (maskPixel != null && maskPixel.length > 0) {
					if (maskPixel[0] == 0) {
						// Add the index of the stone to the list
						positions.add(i);
					}
				}
			}
		}

		if (display != null) {
			display.release();
		}
		colorMask.release();

		// Display drawImage
		display = drawImage;

		// Return the positions
		return positions;
	}

	/**
	 * Finds the centers of the stones.
	 * It uses a minimum area rectangle to find evenly
	 * spaced points along the shape's lengthwise bisector.
	 *
	 * @param contour the edges of the line of stones
	 * @return the center points of the stones
	 */
	private Point[] stoneCenters(MatOfPoint contour) {
		// Convert contour to MatOfPoint2f
		MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

		// Find the minimum area rectangle containing all of the stones
		RotatedRect boundingBox = Imgproc.minAreaRect(contour2f);
		// Get the points of the rectangle
		Point[] boundingPoints = new Point[4];
		boundingBox.points(boundingPoints);

		// Get two adjacent edges
		Line edge1 = new Line(boundingPoints[1], boundingPoints[0]);
		Line edge2 = new Line(boundingPoints[1], boundingPoints[2]);

		// Find which is longer and which is shorter
		Line longEdge, shortEdge;
		if (edge1.compareTo(edge2) > 0) {
			longEdge  = edge1;
			shortEdge = edge2;
		} else {
			shortEdge = edge1;
			longEdge  = edge2;
		}

		// Find the distances between the stones using the length of the shape
		double stoneDistX = (longEdge.x2 - longEdge.x1) / 6;
		double stoneDistY = (longEdge.y2 - longEdge.y1) / 6;
		// Find the vertical center of the stones
		Point shortEdgeCenter = shortEdge.center();

		// Calculate the center points
		Point[] centers = new Point[6];
		for (int i = 0; i < centers.length; i++) {
			centers[i] = new Point(
					(shortEdgeCenter.x + stoneDistX / 2) + stoneDistX * i,
					(shortEdgeCenter.y + stoneDistY / 2) + stoneDistY * i
			);
		}

		// Return the centers
		return centers;
	}
}
