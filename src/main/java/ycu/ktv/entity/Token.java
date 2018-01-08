package ycu.ktv.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("kt_token")
public class Token {
    @Id
    private int id;
    @Column("kt_user_id")
    private int user_id;
    @Column("kt_token_info")
    private String token_info;
    @Column("kt_token_life")
    private String token_life;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken_info() {
        return token_info;
    }

    public void setToken_info(String token_info) {
        this.token_info = token_info;
    }

    public String getToken_life() {
        return token_life;
    }

    public void setToken_life(String token_life) {
        this.token_life = token_life;
    }
}
