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
import java.io.IOException;
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
        } else {
            message.setBody(null);
        }
        message.setMessage("");
        message.setStatus("");
        return message;
    }

    @Ok("json")
    @At("/JoinRoom")
    @Encoding(input = "utf-8", output = "utf-8")
    @GET
    public Message JoinRoom(@Param("user_id")int user_id,@Param("room_id")int room_id){
        Boolean result=false;
        Roommate roommate=new Roommate();
        roommate.setRoom_id(room_id);
        roommate.setUser_id(user_id);

        Dao dao=getDao();
        if(roommate!=null){
            dao.insert(roommate);
            result=true;
        }

        Message message=new Message();
        message.setBody(null);
        message.setStatus("");
        message.setMessage(result.toString());
        
        return message;
    }

    public static Dao getDao() {
        Ioc ioc = new NutIoc(new JsonLoader("ioc/dao.js"));
        DataSource ds = ioc.get(DataSource.class);
        Dao dao = new NutDao(ds); //如果已经定义了dao,那么改成dao = ioc.get(Dao.class);
        return dao;
    }
}
