package ycu.ktv;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Code;
import ycu.ktv.entity.User;

public class Test {
    public static void main(String[] args) {
        System.out.println(GetDao.getDao().update(Code.class, Chain.makeSpecial("kt_code_info", "+1"), Cnd.where("kt_code_id","=", "1")));
    }
}
