package me.zhengjie.modules.news.rest;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.news.domain.Criteria;
import me.zhengjie.modules.news.domain.NewsComment;
import me.zhengjie.modules.news.service.NewsCommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/newsComment")
public class NewsCommentController {

    private final NewsCommentService newsCommentService;

    @PostMapping
    public ResponseEntity<Object> addComment(@RequestBody NewsComment newsComment)
    {
        return new ResponseEntity<>(newsCommentService.addComment(newsComment),HttpStatus.OK);
    }

    @PostMapping("/parentId")
    public ResponseEntity<Object> addCommentByParentId(@RequestBody NewsComment newsComment)
    {
        return new ResponseEntity<>(newsCommentService.addCommentByParentId(newsComment),HttpStatus.OK);
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<Object> findCommentByCriteria(@PathVariable Integer newsId,
                                                        @RequestParam String sortBy,
                                                        @RequestParam String sortOrder,
                                                        Pageable pageable)
    {
        return new ResponseEntity<>(newsCommentService.findCommentByCriteria(newsId,sortBy,sortOrder,pageable),HttpStatus.OK);
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<Object> findCommentByParentId(@PathVariable Integer parentId, Pageable pageable)
    {
        return new ResponseEntity<>(newsCommentService.findCommentByParentId(parentId,pageable),HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteComment(@RequestBody Integer[] ids)
    {
        newsCommentService.deleteComment(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
