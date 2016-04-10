package controller.mvc;

import DTO.IMGGeoTagDTO;
import domain.TrackIMG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.interfaces.ImageService;
import utils.GPSTrackerErrorType;
import utils.GPSTrackerException;
import utils.geotagreader.GeoTag;
import utils.geotagreader.JpegGeoTagReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageControllerMVC {

    private static final Logger logger = LoggerFactory.getLogger(TrackControllerMVC.class);

    @Autowired
    @Resource
    private ImageService imageService;

    @RequestMapping(value = "/track/{trackId}/image/new", method = RequestMethod.GET)
    public String getAddImagePage(@PathVariable("trackId") Long trackId, Model model) {

        model.addAttribute("trackId", trackId);

        return "image";
    }

    @RequestMapping(value = "/track/{trackId}/image/new", method = RequestMethod.POST)
    public String addImage(@PathVariable("trackId") Long trackId, @RequestParam("file") MultipartFile file) throws GPSTrackerException{

        imageService.add(trackId, file);

        return "redirect:/track/" + trackId;
    }

    @RequestMapping(value = "/track/{trackId}/image/delete", method = RequestMethod.GET)
    public String deleteImage(@PathVariable("trackId") Long trackId, @RequestParam("id") Long imageId) {

        imageService.delete(imageId);

        return "redirect:/track/" + trackId;
    }

}
