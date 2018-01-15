package ycu.ktv.module;

import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.*;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.Room;
import ycu.ktv.entity.Roommate;
import ycu.ktv.entity.User;
import ycu.ktv.services.TokenControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nutz.dao.Cnd.where;

public class RoomModule {

    @Ok("json")
    @At("/rooms")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message getRoomlist(@Param("page") int page){
        Message message =new Message();
        List<Map> Roomlist =new ArrayList<Map>();
        //dao.createPager 第一个参数是第几页，第二参数是一页有多少条记录
        //condition 条件
        try{
            List<Room> rooms=GetDao.getDao().query(Room.class, null, GetDao.getDao().createPager(page, 8));
            if(rooms.size()!=0){
                for (Room room:rooms){
                    User user=GetDao.getDao().query(User.class,where("kt_user_id","=",room.getRoom_owner())).get(0);
                    Map<String,Object> map=new HashMap<String, Object>();
                    Map<String,String> user_map=new HashMap<String, String>();
                    user_map.put("user_id",user.getId()+"");
                    user_map.put("user_name",user.getUser_name()+"");
                    user_map.put("user_avatar",user.getUser_avatar()+"");
                    map.put("user",user_map);
                    map.put("room",room);
                    map.put("count",GetDao.getDao().func("kt_roommate","count","kt_roommate_id", Cnd.where("kt_room_id","=",room.getId())));
                    Roomlist.add(map);
                }
                message.setBody(Roomlist);
                message.setMessage("success");
                message.setStatus("1");
            } else {
                message.setBody(null);
                message.setMessage("没有更多");
                message.setStatus("2");
            }
        }catch (Exception e){
           message.setBody(null);
           message.setStatus("3");
           message.setMessage("未知错误");
           e.printStackTrace();
        }
        return message;
    }

    @Ok("json")
    @At("/roommates")
    @Encoding(input = "utf-8",output = "utf-8")
    @GET
    public Message getRoommates(@Param("room_id")int room_id){
        Message message=new Message();
        try{
            List<Roommate> roommates=GetDao.getDao().query(Roommate.class,where("kt_room_id","=",room_id));
            List<Map> mates_list=new ArrayList<Map>();
            for (Roommate roommate:roommates){
                User user=GetDao.getDao().query(User.class,where("kt_user_id","=",roommate.getUser_id())).get(0);
                Map<String,String> roommate_map=new HashMap<String, String>();
                roommate_map.put("user_id",user.getId()+"");
                roommate_map.put("user_avatar",user.getUser_avatar()+"");
                roommate_map.put("user_name",user.getUser_name()+"");
                mates_list.add(roommate_map);
            }
            if(roommates.size()==0){
                message.setMessage("房间内为空");
                message.setStatus("3");
                message.setBody(null);
            }else {
                message.setMessage("success");
                message.setStatus("1");
                message.setBody(mates_list);
            }
        }catch (Exception e){
            message.setBody(null);
            message.setStatus("2");
            message.setMessage("未知错误");
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
                    message.setBody(null);
                    message.setMessage("房间不存在");
                    message.setStatus("6");
                }else if(roommates.size()>=11){
                    message.setBody(null);
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
                message.setBody(null);
                e.printStackTrace();
            }

        return message;
    }

    @Ok("json")
    @At("/exitroom")
    @Encoding(input = "utf-8", output = "utf-8")
    @POST
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
            if(user_id.equals("")){
                message.setBody(null);
                message.setStatus("2");
                message.setMessage("失败");
            }else {
                room.setRoom_name(room_name);
                room.setRoom_owner(user_id);
                GetDao.getDao().insert(room);
                message.setMessage("success");
                message.setStatus("1");
                message.setBody(room);
            }
        }catch (Exception e) {
            message.setMessage("失败");
            message.setStatus("2");
            message.setBody(null);
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
