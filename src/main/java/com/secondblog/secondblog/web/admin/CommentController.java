package com.secondblog.secondblog.web.admin;

import com.secondblog.secondblog.dao.CommentRepository;
import com.secondblog.secondblog.po.Comment;
import com.secondblog.secondblog.po.User;
import com.secondblog.secondblog.service.BlogService;
import com.secondblog.secondblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){//用post方式提交评论
        Long blogId =comment.getBlog().getId();//拿博客的id
        comment.setBlog(blogService.getBlog(blogId));
        User user=(User) session.getAttribute("user");
        if(user!=null){//是否是管理员
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else{
            comment.setAvatar(avatar);//所有游客统一一个头像
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+blogId;
    }
}
