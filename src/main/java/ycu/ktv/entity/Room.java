package ycu.ktv.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("kt_room")
public class Room {
    @Id
    private int id;

    @Column("kt_room_owner")
    private String room_owner;

    @Column("kt_room_name")
    private String room_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_owner() {
        return room_owner;
    }

    public void setRoom_owner(String room_owner) {
        this.room_owner = room_owner;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }
}
