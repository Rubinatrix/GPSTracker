package service;

import DTO.IMGGeoTagDTO;
import service.interfaces.TrackService;
import domain.Track;
import domain.TrackGPX;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utils.GPSTrackerException;
import utils.geotagreader.GeoTag;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TrackServiceHibernateImpl implements TrackService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public Track get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Track track = (Track) session.get(Track.class, id);
        return track;
    }

    @Override
    public List<Track> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t FROM Track as t order by name");
        return query.list();
    }

    @Override
    public void add(Track track) {
        Session session = sessionFactory.getCurrentSession();
        session.save(track);
    }

    @Override
    public void edit(Track track) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(track);
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT tg FROM TrackGPX as tg WHERE tg.track.id = :trackId");
        query.setParameter("trackId", id);
        List contentAsList = query.list();
        if (contentAsList.size() > 0) {
            for (Object result : query.list()) {
                TrackGPX trackGPX = (TrackGPX) result;
                session.delete(trackGPX);
            }
        } else {
            Track track = (Track) session.get(Track.class, id);
            if (track != null) {
                session.delete(track);
            }
        }
    }

    @Override
    public TrackGPX getGPXByTrackId(Long trackId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT tg FROM TrackGPX as tg WHERE tg.track.id = :trackId");
        query.setParameter("trackId", trackId);
        return (TrackGPX) query.uniqueResult();
    }

    @Override
    public void updateGPX(Track track, MultipartFile file) throws GPSTrackerException {
        if ((file == null) || (file.isEmpty())) {
            return;
        }

        Session session = sessionFactory.getCurrentSession();
        try {
            byte[] bytes = file.getBytes();
            TrackGPX trackGPX = getGPXByTrackId(track.getId());
            if (trackGPX == null) {
                trackGPX = new TrackGPX();
                trackGPX.setTrack(track);
            }
            trackGPX.setGPX(bytes);
            session.merge(trackGPX);
        } catch (IOException e) {
            throw new GPSTrackerException(e.getMessage());
        }
    }
}
