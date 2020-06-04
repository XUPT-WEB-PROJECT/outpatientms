package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Article;
import com.xupt.outpatientms.mapper.ArticleMapper;
import com.xupt.outpatientms.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public int addArticle(Article article) {
        return articleMapper.addArticle(article);
    }

    @Override
    public int delArticle(Integer articleId) {
        return articleMapper.delArticle(articleId);
    }

    @Override
    public List<ArticleVO> listArticle() {
        return articleMapper.listArticle();
    }

    @Override
    public Article getArticle(Integer articleId) {
        return articleMapper.getArticle(articleId);
    }
}
