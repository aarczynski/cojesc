package pl.arczynskiadam.cojesc.client.google.ocr;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

@Service
public class OcrService {

    public static final int TIMEOUT = 3000;

    public boolean imageContainsKeywords(URL imageUrl, String... keywords) {
        var foundWords = processImage(imageUrl).getResponses(0).getTextAnnotationsList().stream()
                .map(a -> a.getDescription().toLowerCase())
                .collect(toList());

        var requiredWords = Arrays.stream(keywords)
                .map(String::toLowerCase)
                .collect(toList());

        return foundWords.containsAll(requiredWords);
    }

    private BatchAnnotateImagesResponse processImage(URL url)  {
        // Instantiates a client
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(imageAnnotatorSettings())) {
            var imageBytes = ByteString.copyFrom(downloadFile(url));

            // Builds the image annotation request
            var googleCloudVisionRequests = new ArrayList<AnnotateImageRequest>();

            var image = Image.newBuilder().setContent(imageBytes).build();
            var feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
            var ocrRequest = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(image)
                    .build();

            googleCloudVisionRequests.add(ocrRequest);

            // Performs label detection on the image file
            return vision.batchAnnotateImages(googleCloudVisionRequests);
        } catch (IOException e) {
            throw new RuntimeException("Failed to instantiate Google Cloud Vision ImageAnnotatorClient");
        }
    }

    private byte[] downloadFile(URL url)
    {
        try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
            var connection = url.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.connect();
            IOUtils.copy(connection.getInputStream(), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download image from " + url);
        }
    }

    private ImageAnnotatorSettings imageAnnotatorSettings() {
        try {
            var keyPath = "/Users/adam.arczynski/Downloads/cojesc-e1ccdc6f35c1.json";
            var credentials = ServiceAccountCredentials.fromStream(new FileInputStream(keyPath));

            return ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Google Cloud Vision service key");
        }
    }
}
