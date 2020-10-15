package com.secondblog.secondblog.web;

import com.secondblog.secondblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.print.Pageable;

@Controller
public class AboutShowController {
    @Autowired
    private TagService tagService;
    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("tags",tagService.listTagTop(6));
        return "about";
    }
}
