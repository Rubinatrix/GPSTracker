package domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

@Entity
@Table(name="TRACK_IMG")
public class TrackIMG {

    @JsonView(TrackIMG.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Long id;

    @JsonView(TrackIMG.class)
    @Column(name="NAME")
    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRACK_ID")
    Track track;

    @Column(name = "IMG", length = 10000000)
    @Lob
    byte[] IMG;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public byte[] getIMG() {
        return IMG;
    }

    public void setIMG(byte[] IMG) {
        this.IMG = IMG;
    }
}
