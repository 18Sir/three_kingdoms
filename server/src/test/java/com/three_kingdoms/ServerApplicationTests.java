package com.three_kingdoms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.dao.*;
import com.three_kingdoms.domain.*;
import com.three_kingdoms.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ServerApplicationTests {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ActorDao actorDao;
    @Autowired
    private AddrDao addrDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private CommentDao commentDao;

    @Test
    void getUid() {
        Map<String, Object> map = JWTUtil.parseToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVpZCI6MTAwNiwidW5hbWUiOiJsYWxhbGEifSwiZXhwIjoxNzA4NDgzNDIxfQ.89Fo58fnJSXiFBzPKUdKakbZb0ZpmrVJCBgsIB3Prms");
        System.out.println(map.get("uid").getClass());
    }

    @Test
    void getRelativePath() {
        String path = System.getProperty("user.dir");
        System.out.println(path);
    }

    @Test
    void getCommentAll() {
        List<Comment> commentList = commentDao.selectList(null);
        System.out.println(commentList);
    }

    @Test
    void getMsgAll() {
        List<Msg> msgList = msgDao.selectList(null);
        System.out.println(msgList);
    }

    @Test
    void getEventAll() {
        List<Event> eventList = eventDao.selectList(null);
        System.out.println(eventList);
    }

    @Test
    void getAddrAll() {
        List<Addr> addrList = addrDao.selectList(null);
        System.out.println(addrList);
    }

    @Test
    void getActorAll() {
        List<Actor> actorList = actorDao.selectList(null);
        System.out.println(actorList);
    }


    @Test
    void getUserAll() {
        List<User> userList = userDao.selectList(null);
        System.out.println(userList);
    }

    @Test
    void save() {
        User user = new User();
        user.setUname("grace");
        user.setPassword("666");
        user.setEmail("123456789");
        user.setAdmin(0);
        userDao.insert(user);
    }

    @Test
    void update() {
        User user = new User();
        user.setUid(1002);
        user.setUname("lalala");
        userDao.updateById(user);
    }

    @Test
    void delete() {
        userDao.deleteById(1002);
    }

    @Test
    void getByPage() {
        IPage page = new Page(1, 1);
        userDao.selectPage(page, null);
        System.out.println("当前页码:" + page.getCurrent());
        System.out.println("每页数据总量:" + page.getSize());
        System.out.println("总页数:" + page.getPages());
        System.out.println("数据总量:" + page.getTotal());
        System.out.println("当前页数据:" + page.getRecords());
        page.setCurrent(2);
        System.out.println("当前页码:" + page.getCurrent());
        System.out.println("当前页数据:" + page.getRecords());
    }

    @Test
    void test() throws IOException {
        File file = new File("oos.doc");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(new User());
        oos.close();

        File f = new File("oos.doc");
        ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(f));
    }

}
