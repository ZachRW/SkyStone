package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@TeleOp
public class SkystoneTest extends OpMode {
	private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
	private static final String STONE_LABEL = "Stone";
	private static final String SKYSTONE_LABEL = "Skystone";
	private static final String VUFORIA_KEY =
			"AZLv+a7/////AAAAGdyzndpq4khMnz5IMjSvhiR0XbtOlL7ZfQytGj9s4zFCFoa+IqUA1Cj" +
					"v4ghfSjfRAlRguu6cVbQVM+0Rxladi3AIKhUjIL6v5ToFrK/fxrWdwAzkQfEPM1" +
					"S3ijrTSm1N8DuZ6UoqiKoVmQGzyiWhDpTQoR1zIVkj88rOhBDYwBf0CnW++pxZ0" +
					"pHlQBbh/bzBjt63ANcuI9JyHU3/JLGSBhoIm04G3UnrjVrjKfPFlX9NOwWQLOYj" +
					"Q+4B1l4M8u9BdihYgmfMST0BHON+MQ7qC5dMs/2OSZlSKSZISN/L+x606xzc2Sv" +
					"5G+ULUpaUiChG7Zlv/rncu337WhZjJ1X2pQGY7gIBcSH+TUw81n2jYKkm";

	private VuforiaLocalizer vuforia;
	private TFObjectDetector objectDetector;

	@Override
	public void init() {
		initVuforia();
		if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
			initTensorFlow();
		}

		objectDetector.activate();
	}

	@Override
	public void loop() {
		List<Recognition> recognitions = objectDetector.getRecognitions();
		if (recognitions != null) {
			int skyStones = 0;
			int stones = 0;

			for (Recognition recognition : recognitions) {
				if (recognition.getLabel().equals(SKYSTONE_LABEL)) {
					skyStones++;
				} else {
					stones++;
				}
			}

			telemetry.addData("SkyStones", skyStones);
			telemetry.addData("Stones", stones);
			telemetry.update();
		}
	}

	@Override
	public void stop() {
		objectDetector.shutdown();
	}

	private void initVuforia() {
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

		vuforia = ClassFactory.getInstance().createVuforia(parameters);
	}

	private void initTensorFlow() {
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
				"tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());

		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfodParameters.minimumConfidence = 0.6;

		objectDetector = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		objectDetector.loadModelFromAsset(TFOD_MODEL_ASSET, STONE_LABEL, SKYSTONE_LABEL);
	}
}
