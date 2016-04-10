package controller.rest;

import domain.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import service.interfaces.TrackService;
import utils.GPSTrackerException;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TrackControllerREST {

    @Autowired
    @Resource
    private TrackService trackService;

    @RequestMapping(value = "/track/", method = RequestMethod.GET)
    public ResponseEntity<List<Track>> listAllTracks() {

        List<Track> tracks = trackService.getAll();
        if (tracks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @RequestMapping(value = "/track/{id}", method = RequestMethod.GET)
    public ResponseEntity<Track> getTrack(@PathVariable("id") Long trackId) {

        Track track = trackService.get(trackId);
        if (track == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(track, HttpStatus.OK);
    }

    @RequestMapping(value = "/track/", method = RequestMethod.POST)
    public ResponseEntity<Track> addTrack(@RequestBody Track track, UriComponentsBuilder ucBuilder) throws GPSTrackerException {

        trackService.add(track);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/track/{id}").buildAndExpand(track.getId()).toUri());
        return new ResponseEntity<>(track, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public ResponseEntity<Track> addTrackWithGetRequest(@RequestParam("name") String name, UriComponentsBuilder ucBuilder) throws GPSTrackerException {

        Track track = new Track();
        track.setName(name);
        trackService.add(track);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/track/{id}").buildAndExpand(track.getId()).toUri());
        return new ResponseEntity<>(track, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/track/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Track> updateTrack(@PathVariable("id") Long trackId, @RequestBody Track track) {

        Track currentTrack = trackService.get(trackId);

        if (currentTrack == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentTrack.setName(track.getName());
        trackService.edit(currentTrack);

        return new ResponseEntity<>(currentTrack, HttpStatus.OK);
    }

    @RequestMapping(value = "/track/{id}/gpx/", method = RequestMethod.POST)
    public ResponseEntity<Track> updateTrackGPX(@PathVariable("id") Long trackId, @RequestParam MultipartFile file) throws GPSTrackerException{

        Track currentTrack = trackService.get(trackId);

        if (currentTrack == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        trackService.updateGPX(currentTrack, file);
        return new ResponseEntity<>(currentTrack, HttpStatus.OK);
    }

    @RequestMapping(value = "/track/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Track> deleteTrack(@PathVariable("id") Long trackId) {

        Track track = trackService.get(trackId);
        if (track == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        trackService.delete(trackId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
