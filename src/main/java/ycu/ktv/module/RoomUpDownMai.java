package ycu.ktv.module;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.mvc.annotation.*;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.Playlist;
import ycu.ktv.entity.Song;
import ycu.ktv.entity.User;
import ycu.ktv.services.TokenControl;

import java.util.ArrayList;
import java.util.List;


public class RoomUpDownMai {

    private Dao dao = GetDao.getDao();

    @Ok("Json")
    @At("/create")
    @GET
    public Message roomUpM(@Param("room_id") String room_id, @Param("token") String token, @Param("song_id") String song_id) {
        Message message = new Message();
        int ex_user_id = Integer.valueOf(TokenControl.analysisToken(token));

        if (!(dao.query(Playlist.class, Cnd.where("kt_user_id", "=", ex_user_id)).isEmpty())) {
            message.setBody(null);
            message.setMessage("排麦重复");
            message.setStatus("5");
            return message;
        }

        int ex_room_id = Integer.valueOf(room_id);
        int ex_song_id = Integer.valueOf(song_id);

        Playlist playlist = new Playlist();
        playlist.setRoom_id(ex_room_id);
        playlist.setSong_id(ex_song_id);
        playlist.setUser_id(ex_user_id);

        System.out.print(playlist.toString());

        dao.insert(playlist);
        message.setBody(null);
        message.setMessage("success");
        message.setStatus("1");

        return message;
    }


    @At("/delete")
    @Ok("Json")
    @DELETE
    public Message roomDownM(@Param("token") String token){
        Message message =new Message();
        int ex_user_id=Integer.valueOf(TokenControl.analysisToken(token));
        if (dao.query(Playlist.class, Cnd.where("kt_user_id", "=", ex_user_id)).isEmpty()) {
            message.setBody(null);
            message.setMessage("失败：不存在该房间");
            message.setStatus("5");
            return message;
        }else{
            dao.clear(Playlist.class,Cnd.where("kt_user_id","=",ex_user_id));
            message.setBody(null);
            message.setMessage("secces");
            message.setStatus("1");
        }
        return message;
    }

    @At("/songs")
    @Ok("Json")
    @GET
    public Message getSort(String room_id){

        Message message=new Message();
        Song song=new Song();
        List<Song> songs=new ArrayList<Song>();
        List<Playlist> playlists =new ArrayList<Playlist>();
        Dao dao =GetDao.getDao();

        if (dao.query(Playlist.class, Cnd.where("kt_room_id", "=", room_id)).isEmpty()) {
            message.setBody(null);
            message.setMessage("失败：不存在该房间");
            message.setStatus("6");
            return message;
        }

        playlists = dao.query(Playlist.class, Cnd.where("kt_room_id","=", room_id));
        int song_id=0;
        for (int i=0;i<playlists.size();i++){
            song_id=playlists.get(i).getSong_id();
            song.setId(song_id);
            songs.add(i,song);
        }
        dao.insert(songs);
        message.setBody(songs);
        message.setStatus("1");
        message.setMessage("success");
        return message;

    }

}
