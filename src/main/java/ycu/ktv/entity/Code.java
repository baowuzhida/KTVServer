package ycu.ktv.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;


@Table("kt_code")
public class Code {
    @Id
    private int id;

    @Column("kt_code_info")
    private int code_info;

    @Column("kt_user_phone")
    private String user_phone;

    @Column("kt_code_life")
    private String code_life;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode_info() {
        return code_info;
    }

    public void setCode_info(int code_info) {
        this.code_info = code_info;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getCode_life() {
        return code_life;
    }

    public void setCode_life(String code_life) {
        this.code_life = code_life;
    }
}
