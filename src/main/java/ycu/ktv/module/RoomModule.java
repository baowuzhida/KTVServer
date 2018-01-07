package ycu.ktv.module;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.*;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.Room;
import ycu.ktv.entity.Roommate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.nutz.dao.Cnd.where;
@At("RoomModule")
public class RoomModule {
    @Ok("json")
    @At("/Rooms")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message getRoomlist(@Param("page") int page){
        Dao dao=getDao();
        List<Json> Roomlist =new ArrayList<Json>();
        //dao.createPager 第一个参数是第几页，第二参数是一页有多少条记录
        //condition 条件
        List<Room> room=dao.query(Room.class, null, dao.createPager(page, 8));
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
    @At("/JoinRoom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message JoinRoom(@Param("user_id")int user_id,@Param("room_id")int room_id){
        Roommate roommate=new Roommate();
        roommate.setRoom_id(room_id);
        roommate.setUser_id(user_id);
        Message message=new Message();
        Dao dao=getDao();
        if(roommate!=null){
            try{
                dao.insert(roommate);
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
    @At("/ExitRoom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message ExitRoom(@Param("user_id")int user_id){
        Dao dao=getDao();
        Message message=new Message();
        try{
            dao.delete(Roommate.class,user_id);
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
    @At("/ExitRoom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message createRoom(@Param("room_name")String room_name,@Param("user_name")String user_name){
        Dao dao=getDao();
        Message message=new Message();
        Room room=new Room();
        room.setRoom_name(room_name);
        room.setRoom_owner(user_name);
        try{
            dao.insert(room);
            message.setMessage("success");
            message.setStatus("1");
        }catch (Exception e){
            message.setMessage("未知错误");
            message.setStatus("2");
        }finally {
            message.setBody(null);
            message.setStatus("");
        }
        return message;
    }

    @Ok("json")
    @At("/RoomToOff")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message RoomToOff(@Param("")String sd){

        return null;
    }


    public static Dao getDao() {
        Ioc ioc = new NutIoc(new JsonLoader("ioc/dao.js"));
        DataSource ds = ioc.get(DataSource.class);
        Dao dao = new NutDao(ds); //如果已经定义了dao,那么改成dao = ioc.get(Dao.class);
        return dao;
    }

}
