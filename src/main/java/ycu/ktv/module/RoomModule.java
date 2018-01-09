package ycu.ktv.module;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.*;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.Room;
import ycu.ktv.entity.Roommate;
import ycu.ktv.entity.User;
import ycu.ktv.services.TokenControl;
import java.util.ArrayList;
import java.util.List;
import static org.nutz.dao.Cnd.where;

public class RoomModule {

    @Ok("json")
    @At("/rooms")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message getRoomlist(@Param("page") int page){
        //dao.createPager 第一个参数是第几页，第二参数是一页有多少条记录
        //condition 条件
        List<Room> room=GetDao.getDao().query(Room.class, null, GetDao.getDao().createPager(page, 8));
        Message message =new Message();
        if(room.size()!=0){
            message.setBody(room);
            message.setMessage("success");
            message.setStatus("1");
        } else {
            message.setBody(null);
            message.setMessage("没有更多");
            message.setStatus("2");
        }
        return message;
    }

    @Ok("json")
    @At("/joinroom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message JoinRoom(@Param("token")String compactJws,@Param("room_id")int room_id){
        Roommate roommate=new Roommate();
        String user_id=TokenControl.analysisToken(compactJws);
        roommate.setRoom_id(room_id);
        roommate.setUser_id(Integer.parseInt(user_id));
        Message message=new Message();
        if(roommate!=null){
            try{
                GetDao.getDao().insert(roommate);
                message.setMessage("success");
                message.setStatus("1");
            }catch (Exception e){
                message.setMessage("加入失败");
                message.setStatus("0");
            }
        }
        message.setBody(null);
        return message;
    }

    @Ok("json")
    @At("/exitroom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message ExitRoom(@Param("token")String compactJws){
        Message message=new Message();
        int user_id=Integer.parseInt(TokenControl.analysisToken(compactJws));
        try{
            GetDao.getDao().clear(Roommate.class,where("user_id", "=", user_id));
            message.setMessage("success");
            message.setStatus("1");
        }catch (Exception e){
            message.setMessage("未知错误");
            message.setStatus("2");
        }
        message.setBody(null);
        return message;
    }

    @Ok("json")
    @At("/createroom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message createRoom(@Param("room_name")String room_name,@Param("token")String compactJws){
        Message message=new Message();
        Room room=new Room();
        int user_id=Integer.parseInt(TokenControl.analysisToken(compactJws));
        try{
//            int user_id=Integer.parseInt(TokenControl.analysisToken(compactJws));
            String user_name=GetDao.getDao().query(User.class,where("user_id","=",user_id)).get(0).getUser_name();
            room.setRoom_name(room_name);
            room.setRoom_owner(user_name);
            GetDao.getDao().insert(room);
            message.setMessage("success");
            message.setStatus("1");
        }catch (Exception e){
            message.setMessage("失败");
            message.setStatus("2");
        }finally {
            message.setBody("");
            message.setStatus("");
        }
        return message;
    }

    @Ok("json")
    @At("/RoomToOff")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message RoomToOff(@Param("room_id")int room_id){
        Message message=new Message();
        try{
            GetDao.getDao().clear(Room.class,where("kt_room_id","=",room_id));
            message.setMessage("success");
            message.setStatus("1");
        }catch (Exception e){
            message.setMessage("未知错误");
            message.setStatus("2");
        }finally {
            message.setBody(null);
        }
        return message;
    }




}
