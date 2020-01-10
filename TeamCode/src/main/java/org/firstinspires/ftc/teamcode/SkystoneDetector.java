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
            new Scalar(8.5, 127.5, 100),
            new Scalar(53.125, 255, 255)
    );

    private final Point[] stoneCenters = {
            new Point(150, 810),
            new Point(350, 790),
            new Point(550, 770)
    };

    /**
     * The current frame of the video feed.
     */
    private Mat frame;

    /**
     * The image to display on the robot controller phone.
     */
    private Mat display;

    private final Telemetry telemetry;

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
    int skystonePosition() {
        // Return an empty list is frame is null
        if (frame == null || frame.empty()) {
            return -1;
        }

        // Create image to draw on
        Mat drawImage = frame.clone();

        // Make a color mask of yellowish pixels
        Mat colorMask = new Mat(frame.size(), CvType.CV_8UC4);
        if (frame.empty()) {
            telemetry.addLine("Empty frame");
            telemetry.update();
            return -1;
        }
        try {
            rangeFilter.process(frame.clone(), colorMask);
        } catch (OpenCvCameraException e) {
            telemetry.addData("Error", e.getMessage());
            telemetry.update();
            return -1;
        }

        int position = -1;

        // Find the contours (edges) of the mask
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(colorMask, contours, new Mat(),
                Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Outline the line of stones
        Imgproc.drawContours(drawImage, contours, -1, new Scalar(0, 255, 0), 2);

        // Draw the center points
        for (Point stoneCenter : stoneCenters) {
            Imgproc.circle(drawImage, stoneCenter, 1, new Scalar(255, 0, 0), 10);
        }

        for (int i = 0; i < stoneCenters.length; i++) {
            // If the center point is not yellowish, it is a skystone
            double[] maskPixel = colorMask.get((int) stoneCenters[i].y, (int) stoneCenters[i].x);
            if (maskPixel != null && maskPixel.length > 0) {
                if (maskPixel[0] == 0) {
                    position = i;
                    break;
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
        return position;
    }
}
