package ycu.ktv.entity;

import org.nutz.dao.entity.annotation.Column;

public class SongPaiMai {

    @Column("kt_song_id")
    private int id;

    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Column("kt_song_name")
    private String song_name;

    @Column("kt_song_music_link")
    private String song_music_link;

    @Column("kt_song_lrc_link")
    private String song_lrc_link;

    private String song_singer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSong_singer() {
        return song_singer;
    }

    public void setSong_singer(String song_singer) {
        this.song_singer = song_singer;
    }

    public String getSong_music_link() {
        return song_music_link;
    }

    public void setSong_music_link(String song_music_link) {
        this.song_music_link = song_music_link;
    }

    public String getSong_lrc_link() {
        return song_lrc_link;
    }

    public void setSong_lrc_link(String song_lrc_link) {
        this.song_lrc_link = song_lrc_link;
    }






}
