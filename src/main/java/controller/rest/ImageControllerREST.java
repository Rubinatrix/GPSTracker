package controller.rest;

import DTO.IMGGeoTagDTO;
import com.fasterxml.jackson.annotation.JsonView;
import domain.Track;
import domain.TrackIMG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import service.interfaces.ImageService;
import service.interfaces.TrackService;
import utils.GPSTrackerException;
import utils.geotagreader.GeoTag;
import utils.geotagreader.JpegGeoTagReader;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageControllerREST {

    @Autowired
    @Resource
    private TrackService trackService;

    @Autowired
    @Resource
    private ImageService imageService;

    @JsonView(TrackIMG.class)
    @RequestMapping(value = "/track/{trackId}/image/", method = RequestMethod.POST)
    public ResponseEntity<TrackIMG> addImage(@PathVariable("trackId") Long trackId, @RequestParam("file") MultipartFile file, UriComponentsBuilder ucBuilder) throws GPSTrackerException{

        Track currentTrack = trackService.get(trackId);
        if (currentTrack == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TrackIMG image = imageService.add(trackId, file);

        HttpHeaders headers = new HttpHeaders();
        Map<String, Long> map = new HashMap<>();
        map.put("trackId", trackId);
        map.put("imageId", image.getId());
        headers.setLocation(ucBuilder.path("/api/track/{trackId}/image/{imageId}").buildAndExpand(map).toUri());
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/track/{trackId}/image/{imageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Track> deleteImage(@PathVariable("trackId") Long trackId, @PathVariable("imageId") Long imageId) {

        TrackIMG image = imageService.get(imageId);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        imageService.delete(imageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/track/{trackId}/geotags", method = RequestMethod.GET)
    public List<IMGGeoTagDTO> getGeotagsByTrackId(@PathVariable("trackId") Long trackId) {

        List<IMGGeoTagDTO> result = new ArrayList<>();
        List<TrackIMG> trackIMGList = imageService.getAllByTrackId(trackId);

        for (TrackIMG trackIMG: trackIMGList) {
            if (trackIMG == null) {
                continue;
            }

            JpegGeoTagReader jpegGeoTagReader = new JpegGeoTagReader();
            try {
                GeoTag geoTag = jpegGeoTagReader.readMetadata(new ByteArrayInputStream(trackIMG.getIMG()));
                result.add(new IMGGeoTagDTO(trackIMG.getId(), geoTag.getLatitude(), geoTag.getLongitude()));
            } catch (Exception e){
                continue;
            }
        }

        return result;
    }

}