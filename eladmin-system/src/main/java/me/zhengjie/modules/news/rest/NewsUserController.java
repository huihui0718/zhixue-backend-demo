package me.zhengjie.modules.news.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.news.domain.Criteria;
import me.zhengjie.modules.news.domain.NewsUser;
import me.zhengjie.modules.news.service.NewsUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newsUser")
public class NewsUserController {

    private final NewsUserService newsUserService;

    @PostMapping
    public ResponseEntity<Object> addNewsUser (@RequestBody NewsUser newsUser){
        return new ResponseEntity<>(newsUserService.addNewsUser(newsUser), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAll (@RequestBody Integer[] ids){
        newsUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findAll (Pageable pageable){
        return new ResponseEntity<>(newsUserService.findAll(pageable), HttpStatus.OK);
    }
}
