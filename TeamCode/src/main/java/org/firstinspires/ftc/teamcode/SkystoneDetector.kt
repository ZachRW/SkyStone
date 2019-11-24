package org.firstinspires.ftc.teamcode

import com.disnodeteam.dogecv.filters.HSVRangeFilter
import com.disnodeteam.dogecv.math.Line

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.RotatedRect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvCameraException
import org.openftc.easyopencv.OpenCvPipeline

import java.util.ArrayList

/**
 * This class can identify where skystones are in a line of stones.
 */
class SkystoneDetector internal constructor(private val telemetry: Telemetry) : OpenCvPipeline() {
    /**
     * Creates a mask of pixels within a color range.
     * This one finds yellowish pixels.
     *
     *
     * The bounds are in HSV with strange ranges:
     * H: [0, 180]
     * S: [0, 255]
     * V: [0, 255]
     */
    private val rangeFilter = HSVRangeFilter(
            Scalar(8.5, 127.5, 127.5),
            Scalar(53.125, 255.0, 255.0)
    )

    /**
     * The current frame of the video feed.
     */
    private var frame: Mat? = null

    /**
     * The image to display on the robot controller phone.
     */
    private var display: Mat? = null

    override fun processFrame(input: Mat): Mat {
        // Save the frame
        if (!input.empty()) {
            if (frame != null) {
                frame!!.release()
            }
            frame = input.clone()
        }

        // If the display Mat is null show the frame,
        // otherwise show display.
        return if (display == null || display!!.cols() != input.cols() || display!!.rows() != input.rows()) {
            input
        } else display
    }

    /**
     * Finds the positions of the skystones.
     * The positions are not pixel coordinates;
     * they are the indexes
     * (i. e. the *n*th and *k*th stones
     * are skystones.)
     *
     * @return a list of the positions of skystones.
     */
    internal fun skystonePositions(): List<Int>? {
        // Return an empty list is frame is null
        if (frame == null || frame!!.empty()) {
            return ArrayList()
        }

        // Create image to draw on
        val drawImage = frame!!.clone()

        // Make a color mask of yellowish pixels
        val colorMask = Mat(frame!!.size(), CvType.CV_8UC4)
        if (frame!!.empty()) {
            telemetry.addLine("Empty frame")
            telemetry.update()
            return null
        }
        try {
            rangeFilter.process(frame!!.clone(), colorMask)
        } catch (e: OpenCvCameraException) {
            telemetry.addData("Error", e.message)
            telemetry.update()
            return null
        }

        // Find the contours (edges) of the mask
        val contours = ArrayList<MatOfPoint>()
        Imgproc.findContours(colorMask, contours, Mat(),
                Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

        // Use the contours to find the largest yellowish shape.
        // This is presumably the line of stones.
        var maxArea = 0.0
        var maxAreaIndex = 0
        for (i in contours.indices) {
            val area = Imgproc.contourArea(contours[i])

            if (area > maxArea) {
                maxArea = area
                maxAreaIndex = i
            }
        }

        // Outline the line of stones
        Imgproc.drawContours(drawImage, contours, maxAreaIndex, Scalar(0.0, 255.0, 0.0), 2)

        // Create list of positions of the skystones
        val positions = ArrayList<Int>()

        // If the shape is larger than a threshold
        if (maxArea > 100) {
            // Get where the stone centers should be
            val stoneCenters = stoneCenters(contours[maxAreaIndex])

            for (i in stoneCenters.indices) {
                // Draw the center points
                Imgproc.circle(drawImage, stoneCenters[i], 1, Scalar(255.0, 0.0, 0.0), 10)

                // If the center point is not yellowish, it is a skystone
                val maskPixel = colorMask.get(stoneCenters[i].y.toInt(), stoneCenters[i].x.toInt())
                if (maskPixel != null && maskPixel.size > 0) {
                    if (maskPixel[0] == 0.0) {
                        // Add the index of the stone to the list
                        positions.add(i)
                    }
                }
            }
        }

        if (display != null) {
            display!!.release()
        }
        colorMask.release()

        // Display drawImage
        display = drawImage

        // Return the positions
        return positions
    }

    /**
     * Finds the centers of the stones.
     * It uses a minimum area rectangle to find evenly
     * spaced points along the shape's lengthwise bisector.
     *
     * @param contour the edges of the line of stones
     * @return the center points of the stones
     */
    private fun stoneCenters(contour: MatOfPoint): Array<Point> {
        // Convert contour to MatOfPoint2f
        val contour2f = MatOfPoint2f(*contour.toArray())

        // Find the minimum area rectangle containing all of the stones
        val boundingBox = Imgproc.minAreaRect(contour2f)
        // Get the points of the rectangle
        val boundingPoints = arrayOfNulls<Point>(4)
        boundingBox.points(boundingPoints)

        // Get two adjacent edges
        val edge1 = Line(boundingPoints[1], boundingPoints[0])
        val edge2 = Line(boundingPoints[1], boundingPoints[2])

        // Find which is longer and which is shorter
        val longEdge: Line
        val shortEdge: Line
        if (edge1.compareTo(edge2) > 0) {
            longEdge = edge1
            shortEdge = edge2
        } else {
            shortEdge = edge1
            longEdge = edge2
        }

        // Find the distances between the stones using the length of the shape
        val stoneDistX = (longEdge.x2 - longEdge.x1) / 6
        val stoneDistY = (longEdge.y2 - longEdge.y1) / 6
        // Find the vertical center of the stones
        val shortEdgeCenter = shortEdge.center()

        // Calculate the center points
        val centers = arrayOfNulls<Point>(6)
        for (i in centers.indices) {
            centers[i] = Point(
                    shortEdgeCenter.x + stoneDistX / 2 + stoneDistX * i,
                    shortEdgeCenter.y + stoneDistY / 2 + stoneDistY * i
            )
        }

        // Return the centers
        return centers
    }
}
