package controller;

import domain.TrackGPX;
import domain.TrackIMG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.interfaces.ImageService;
import service.interfaces.TrackService;
import utils.GPSTrackerErrorType;
import utils.GPSTrackerException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping({"","/api"})
public class DownloadController {

    @Value("${buffer_size}")
    private int bufferSize;

    @Autowired
    @Resource
    private TrackService trackService;

    @Autowired
    @Resource
    private ImageService imageService;

    @RequestMapping(value = "/track/{trackId}/map/download", method = RequestMethod.GET)
    public String downloadGPX(@PathVariable("trackId") Long trackId, HttpServletResponse response, Model model) throws GPSTrackerException {

        String filename = "track_" + trackId.toString() + ".gpx";
        TrackGPX trackGPX = trackService.getGPXByTrackId(trackId);

        if (trackGPX == null) {
            model.addAttribute("contentError", GPSTrackerErrorType.LACK_OF_TRACK_FILE.getName());
            return "redirect:/tracks";
        }

        response.setContentType("application/xml");
        response.setContentLength(trackGPX.getGPX().length);
        String contentDispositionType = "inline";
        response.setHeader("Content-Disposition", String.format(contentDispositionType + "; filename=\"" + filename + "\""));

        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(trackGPX.getGPX());
                OutputStream outputStream = response.getOutputStream();
        ) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead = -1;

            while ((bytesRead = byteArrayInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new GPSTrackerException(e.getMessage());
        }

        return null;
    }

    @RequestMapping(value = "/track/{trackId}/image/{imageId}", method = RequestMethod.GET)
    public String downloadIMG(@PathVariable("trackId") Long trackId, @PathVariable("imageId") Long imageId, HttpServletResponse response, Model model) throws GPSTrackerException {

        String filename = "img_" + imageId.toString() + ".jpg";
        TrackIMG trackIMG = imageService.get(imageId);

        if (trackIMG == null) {
            model.addAttribute("contentError", GPSTrackerErrorType.LACK_OF_IMAGE_FILE.getName());
            return "redirect:/track/"+trackId;
        }

        response.setContentType("image/jpeg");
        response.setContentLength(trackIMG.getIMG().length);
        String contentDispositionType = "inline";
        response.setHeader("Content-Disposition", String.format(contentDispositionType + "; filename=\"" + filename + "\""));

        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(trackIMG.getIMG());
                OutputStream outputStream = response.getOutputStream();
        ) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead = -1;

            while ((bytesRead = byteArrayInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new GPSTrackerException(e.getMessage());
        }

        return null;
    }

}
