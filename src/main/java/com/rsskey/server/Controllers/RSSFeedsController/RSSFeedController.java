package com.rsskey.server.Controllers.RSSFeedsController;

import com.rsskey.server.Models.APIError;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.RSSParser.RSSFeedParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.lang.Long;

@RestController
@RequestMapping(path="/feeds")
public class RSSFeedController {

    @RequestMapping(method = RequestMethod.GET)
    public List<RSSFeed> getAllFeeds() {
        RSSFeed feed = new RSSFeed("Title", "link", "description", "language", "copyright", "date", new Long(01));
        List array = new ArrayList<RSSFeed>();
        array.add(feed);
        return array;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity putFeed(@RequestBody RSSFeedPutRequestBody url) {
        try {
            RSSFeedParser rssFeedParser = new RSSFeedParser(url.url);
            RSSFeed rssFeed = rssFeedParser.readFeed();
            return new ResponseEntity<>(rssFeed, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(new APIError(HttpStatus.BAD_REQUEST, e), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFeed(@PathVariable("id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getFeed(Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity getCategories() {
        return ResponseEntity.ok(null);
    }
}
