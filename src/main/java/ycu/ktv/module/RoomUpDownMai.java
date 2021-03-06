package ycu.ktv.module;

import org.nutz.dao.Cnd;
import org.nutz.mvc.annotation.*;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.*;
import ycu.ktv.services.TokenControl;

import java.util.ArrayList;
import java.util.List;


public class RoomUpDownMai {


    /*
     * 上麦功能：根据各个歌曲参数初始化数据库里的歌曲表，根据room_id在Playlist表里加入一条信息
     */
    @Ok("Json")
    @At("/room/singer/create")
    @POST
    public Message roomUpM(@Param("room_id") String room_id, @Param("token") String token, @Param("song_id") String song_id, @Param("song_name") String song_name, @Param("song_singer") String song_singer, @Param("song_music_link") String song_music_link, @Param("song_lrc_link") String song_lrc_link) {

        Message message = new Message();
        Song song = new Song();
        String a = "";
        int ex_room_id = 0;
        int ex_song_id = 0;

        try {
            ex_room_id = Integer.valueOf(room_id);
            ex_song_id = Integer.valueOf(song_id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            message.setBody(null);
            message.setMessage("失败：数据转换出错（String转int）");
            message.setStatus("3");
            return message;
        }
        try {
            if (!(GetDao.getDao().query(Song.class, Cnd.where("kt_song_id", "=", song_id)).isEmpty())) {
                System.out.println(a);
            } else {
                song.setId(ex_song_id);
                song.setSong_name(song_name);
                song.setSong_singer(song_singer);
                song.setSong_music_link(song_music_link);
                song.setSong_lrc_link(song_lrc_link);
                GetDao.getDao().insert(song);
                a = "，通过传入的歌曲参数已将歌曲插入数据库";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("插入歌曲（数据库）异常");
        }

        try {
            int ex_user_id = 0;
            try {
                ex_user_id = Integer.valueOf(TokenControl.analysisToken(token));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                message.setBody(null);
                message.setMessage("token出错" + a);
                message.setStatus("3");
                return message;
            }
            if (!(GetDao.getDao().query(Playlist.class, Cnd.where("kt_user_id", "=", ex_user_id)).isEmpty())) {
                message.setBody(null);
                message.setMessage("排麦重复" + a);
                message.setStatus("5");
                return message;
            } else {
                Playlist playlist = new Playlist();
                playlist.setRoom_id(ex_room_id);
                playlist.setSong_id(ex_song_id);
                playlist.setUser_id(ex_user_id);

                GetDao.getDao().insert(playlist);
                message.setBody(null);
                message.setMessage("success");
                message.setStatus("1");
                return message;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            message.setBody(null);
            message.setMessage("未知错误");
            message.setStatus("0");
            return message;
        }
    }


    /*
     * 下麦功能:根据token获取用户id，根据用户id删除Playlis表里的一条记录
     */
    @At("/room/singer/delete")
    @Ok("Json")
    @DELETE
    public Message roomDownM(@Param("token") String token) {
        Message message = new Message();

        try {
            int ex_user_id = 0;
            try {
                ex_user_id = Integer.valueOf(TokenControl.analysisToken(token));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                message.setBody(null);
                message.setMessage("失败：token解析出错");
                message.setStatus("3");
                return message;
            }
            if (GetDao.getDao().query(Playlist.class, Cnd.where("kt_user_id", "=", ex_user_id)).isEmpty()) {
                message.setBody(null);
                message.setMessage("失败：不存在该用户");
                message.setStatus("2");
                return message;
            } else {
                GetDao.getDao().clear(Playlist.class, Cnd.where("kt_user_id", "=", ex_user_id));
                message.setBody(null);
                message.setMessage("secces");
                message.setStatus("1");
                return message;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            message.setBody(null);
            message.setMessage("未知错误");
            message.setStatus("0");
            return message;
        }

    }


    /*
     *  排麦功能：获取room_id,根据room_id（所有相同）在数据库的song表里加入song的各项信息，最后返回List<song>集合..
     */
    @At("/room/songs")
    @Ok("Json")
    @GET
    public Message getSort(@Param("room_id") String room_id) {

        Message message = new Message();

        List<Playlist> playlists = new ArrayList<Playlist>();
        int ex_room_id = 0;
        try {
            ex_room_id = Integer.valueOf(room_id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            message.setBody(null);
            message.setMessage("失败：数据转换出错（String转int）");
            message.setStatus("3");
            return message;
        }
        try {
            if (GetDao.getDao().query(Playlist.class, Cnd.where("kt_room_id", "=", ex_room_id)).isEmpty()) {
                List<SongPaiMai> songPaiMais1 = new ArrayList<SongPaiMai>();
                message.setBody(songPaiMais1);
                message.setMessage("succes");
                message.setStatus("1");
                return message;
            }
            playlists = GetDao.getDao().query(Playlist.class, Cnd.where("kt_room_id", "=", ex_room_id));
            int song_id = 0;

            List<Song> songs = new ArrayList<Song>();
            List<SongPaiMai> songPaiMais =new ArrayList<SongPaiMai>();
            List<User> users = new ArrayList<User>();

            for (int i=0;i<playlists.size();i++
                 ) {

                List<User> tempusers = new ArrayList<User>();
                tempusers=GetDao.getDao().query(User.class,Cnd.where("kt_user_id","=",playlists.get(i).getUser_id()));

                users.addAll(i,tempusers);
            }

            for (Playlist pl : playlists) {
                Song song = new Song();
                song_id = pl.getSong_id();
                song.setId(song_id);
                songs.add(song);
            }

            for (int i=0;i<playlists.size();i++){
                SongPaiMai temsongPaiMai =new SongPaiMai();
                List<Song> tempsong = new ArrayList<Song>();
                tempsong=GetDao.getDao().query(Song.class,Cnd.where("kt_song_id","=",songs.get(i).getId()));
                temsongPaiMai.setUser_id(users.get(i).getId());
                temsongPaiMai.setSong_singer(users.get(i).getUser_name());
                temsongPaiMai.setId(tempsong.get(0).getId());
                temsongPaiMai.setSong_lrc_link(tempsong.get(0).getSong_lrc_link());
                temsongPaiMai.setSong_music_link(tempsong.get(0).getSong_music_link());
                temsongPaiMai.setSong_name(tempsong.get(0).getSong_name());
               songPaiMais.add(i,temsongPaiMai);
            }

            if (!songPaiMais.isEmpty()&&songPaiMais!=null){
                message.setBody(songPaiMais);
                message.setStatus("1");
                message.setMessage("success");
            }else {
                message.setBody(songPaiMais);
                message.setStatus("8");
                message.setMessage("数据库的Playlist有song_id,但是数据库的Song表里没有这个song_id");
            }

            return message;
        } catch (Exception e) {
            e.printStackTrace();
            message.setBody(null);
            message.setMessage("未知错误");
            message.setStatus("0");
            return message;
        }
    }
}
