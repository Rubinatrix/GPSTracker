package controller.mvc;

import org.springframework.web.bind.annotation.*;
import service.interfaces.ImageService;
import service.interfaces.TrackService;
import domain.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import utils.GPSTrackerException;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TrackControllerMVC {

    private static final Logger logger = LoggerFactory.getLogger(TrackControllerMVC.class);

    @Autowired
    @Resource
    private TrackService trackService;

    @Autowired
    @Resource
    private ImageService imageService;

    @RequestMapping(value = {"/","/tracks"}, method = RequestMethod.GET)
    public String getAllTracksPage(Model model){

        List<Track> tracks = trackService.getAll();
        model.addAttribute("tracks", tracks);

        return "track-list";
    }

    @RequestMapping(value = "/track/{trackId}", method = RequestMethod.GET)
    public String getEditTrackPage(@PathVariable("trackId") Long trackId, Model model) {

        Track existingTrack = trackService.get(trackId);
        model.addAttribute("type", "existing");
        model.addAttribute("track", existingTrack);
        model.addAttribute("images", imageService.getAllByTrackId(trackId));

        return "track";
    }

    @RequestMapping(value = "/track/{trackId}", method = RequestMethod.POST)
    public String updateTrack(@PathVariable("trackId") Long trackId, @RequestParam("file") MultipartFile file, @ModelAttribute("track") Track track) throws GPSTrackerException{

        track.setId(trackId);
        trackService.edit(track);
        trackService.updateGPX(track, file);

        return "redirect:/tracks";
    }

    @RequestMapping(value = "/track/new", method = RequestMethod.GET)
    public String getAddTrackPage(Model model) {

        Track track = new Track();
        model.addAttribute("type", "new");
        model.addAttribute("track", track);

        return "track";
    }

    @RequestMapping(value = "/track/new", method = RequestMethod.POST)
    public String addTrack(@RequestParam("file") MultipartFile file, @ModelAttribute("track") Track track) throws GPSTrackerException{

        trackService.add(track);
        trackService.updateGPX(track, file);

        return "redirect:/tracks";
    }

    @RequestMapping(value = "/track/delete", method = RequestMethod.GET)
    public String deleteTrack(@RequestParam("id") Long trackId) {

        trackService.delete(trackId);

        return "redirect:/tracks";
    }

    @RequestMapping(value = "/track/{trackId}/map", method = RequestMethod.GET)
    public String getTrackMapPage(@PathVariable("trackId") Long trackId, Model model) throws GPSTrackerException {
        model.addAttribute("trackId", trackId);
        return "track-map";
    }

}
