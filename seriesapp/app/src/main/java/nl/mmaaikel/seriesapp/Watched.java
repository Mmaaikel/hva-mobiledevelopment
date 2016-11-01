package nl.mmaaikel.seriesapp;

/**
 * Created by Maikel on 28-09-16.
 */

public class Watched {

    private long id;
    private long serieId;
    private String season;
    private String episode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSerieId() {
        return serieId;
    }

    public void setSerieId(long serieId) {
        this.serieId = serieId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    @Override
    public String toString() {
        return "Season " + season + ", episode: " + episode;
    }
}
