package com.three_kingdoms.services.impl;

import com.three_kingdoms.dao.CommentDao;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.services.CommentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServicesImpl implements CommentServices {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Integer save(Comment c) {
        return commentDao.insert(c);
    }

    @Override
    public Integer delete(Long cid) {
        return commentDao.deleteById(cid);
    }

    @Override
    public Integer update(Comment c) {
        return commentDao.updateById(c);
    }

    @Override
    public Comment findById(Long cid) {
        Comment c = commentDao.selectById(cid);
        User u = userDao.selectById(c.getUid());
        c.setUname(u.getUname());
        c.setAvatar(u.getAvatar());
        return commentDao.selectById(cid);
    }
}
