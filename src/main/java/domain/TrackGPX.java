package domain;

import javax.persistence.*;

@Entity
@Table(name="TRACK_GPX")
public class TrackGPX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "TRACK_ID")
    Track track;

    @Column(name = "GPX", length = 10000000)
    @Lob
    byte[] GPX;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public byte[] getGPX() {
        return GPX;
    }

    public void setGPX(byte[] GPX) {
        this.GPX = GPX;
    }
}
