package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Article;
import com.xupt.outpatientms.vo.ArticleVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {

    int addArticle(Article article);

    int delArticle(Integer articleId);

    List<ArticleVO> listArticle();

    Article getArticle(Integer articleId);

}
