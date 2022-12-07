package rest;

import java.io.File;
import java.util.List;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lucene.LuceneService;
import service.PersistenceService;

@RestController
public class MainControllerImpl {

    private final LuceneService luceneService;

    @Autowired
    public MainControllerImpl(LuceneService luceneService) {
        this.luceneService = luceneService;
    }

    @GetMapping("/search")
    List<String> search(@RequestParam String text) {
        QueryBuilder queryBuilder = new QueryBuilder(new RussianAnalyzer());
        Query phraseQuery = queryBuilder.createPhraseQuery(LuceneService.BODY_FIELD, text);
        return luceneService.search(phraseQuery);
    }

}
