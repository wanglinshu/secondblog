package com.secondblog.secondblog.service;

import com.secondblog.secondblog.dao.CommentRepository;
import com.secondblog.secondblog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort=new Sort("createTime");
        List<Comment> comments=commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView=new ArrayList<>();
        for(Comment comment:comments){
            Comment c=new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments){
        for (Comment comment:comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1 : replys1){
                recursively(reply1);
            }
            comment.setReplyComments(temReplys);
            temReplys=new ArrayList<>();
        }
    }

    private List<Comment> temReplys=new ArrayList<>();

    private void recursively(Comment comment){
        temReplys.add(comment);
        if(comment.getReplyComments().size()>0){
            List<Comment> replys=comment.getReplyComments();
            for (Comment reply:replys){
                temReplys.add(reply);
                if(reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }
    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId=comment.getParentComment().getId();
        if(parentCommentId!=-1){
            comment.setParentComment(commentRepository.findOne(parentCommentId));
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}
