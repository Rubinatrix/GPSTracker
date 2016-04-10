package service;

import domain.Track;
import domain.TrackIMG;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import service.interfaces.ImageService;
import utils.GPSTrackerException;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ImageServiceHibernateImpl implements ImageService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public List<TrackIMG> getAllByTrackId(Long trackId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT ti FROM TrackIMG as ti where ti.track.id = :trackId order by ti.id");
        query.setParameter("trackId", trackId);
        return query.list();
    }

    @Override
    public TrackIMG get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        TrackIMG image = (TrackIMG) session.get(TrackIMG.class, id);
        return image;
    }

    @Override
    public TrackIMG add(Long trackId, MultipartFile file) throws GPSTrackerException {
        if ((file == null) || (file.isEmpty())) {
            return null;
        }

        Session session = sessionFactory.getCurrentSession();
        Track track = (Track) session.get(Track.class, trackId);

        try {
            byte[] bytes = file.getBytes();
            TrackIMG image = new TrackIMG();
            image.setName(file.getOriginalFilename());
            image.setTrack(track);
            image.setIMG(bytes);
            session.save(image);
            return image;
        } catch (IOException e) {
            throw new GPSTrackerException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        TrackIMG image = (TrackIMG) session.get(TrackIMG.class, id);
        if (image != null) {
            session.delete(image);
        }
    }

}
