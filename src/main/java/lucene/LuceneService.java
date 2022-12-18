package main.java.lucene;

import org.apache.lucene.search.Query;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface LuceneService {

    String TITLE_FIELD = "title";
    String BODY_FIELD = "body";

    boolean write(File file);
    List<String> search(Query query);
    Path getIndexDir();
}
