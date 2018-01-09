package ycu.ktv.module;

import org.nutz.json.Json;
import org.nutz.mvc.annotation.*;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.Room;
import ycu.ktv.entity.Roommate;
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

        List<Json> Roomlist =new ArrayList<Json>();
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

    public Message JoinRoom(@Param("room_id")int room_id,@Param("token")String token){
        Roommate roommate=new Roommate();
        Message message=new Message();
            try{
                int user_id=Integer.parseInt(TokenControl.analysisToken(token));
                roommate.setRoom_id(room_id);
                roommate.setUser_id(user_id);
                List<Room> rooms=GetDao.getDao().query(Room.class,where("kt_room_id","=",room_id));
                List<Roommate> roommates=GetDao.getDao().query(Roommate.class,where("kt_room_id","=",room_id));
                List<Roommate> user_roommates=GetDao.getDao().query(Roommate.class,where("kt_user_id","=",user_id));
                if(user_roommates.size()!=0){
                    GetDao.getDao().delete(user_roommates);
                }
                if(rooms.size()==0){
                    message.setBody("");
                    message.setMessage("房间不存在");
                    message.setStatus("6");
                }else if(roommates.size()>=11){
                    message.setBody("");
                    message.setStatus("2");
                    message.setMessage("人数已满");
                }else {
                    GetDao.getDao().insert(roommate);
                    Room room=GetDao.getDao().query(Room.class,where("kt_room_id","=",room_id)).get(0);
                    message.setMessage("success");
                    message.setStatus("1");
                    message.setBody(room);
                }
            }catch (Exception e){
                message.setMessage("未知错误");
                message.setStatus("0");
                message.setBody("");
                e.printStackTrace();
            }

        return message;
    }

    @Ok("json")
    @At("/exitroom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message ExitRoom(@Param("token")String token){
        Message message=new Message();
        try{
            int user_id=Integer.parseInt(TokenControl.analysisToken(token));
            GetDao.getDao().clear(Roommate.class,where("user_id", "=", user_id));
            message.setMessage("success");
            message.setStatus("1");
        }catch (Exception e){
            message.setMessage("失败");
            message.setStatus("2");
        }
        message.setBody(null);
        return message;
    }

    @Ok("json")
    @At("/createroom")
    @Encoding(input = "utf-8", output = "utf-8")
    @POST
    public Message createRoom(@Param("room_name")String room_name,@Param("token")String compactJws){
        Message message=new Message();
        Room room=new Room();
        try{
            String user_id=TokenControl.analysisToken(compactJws);
            room.setRoom_name(room_name);
            room.setRoom_owner(user_id);
            GetDao.getDao().insert(room);
            message.setMessage("success");
            message.setStatus("1");
            message.setBody(room);
        }catch (Exception e) {
            message.setMessage("失败");
            message.setStatus("2");
            message.setBody("");
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
