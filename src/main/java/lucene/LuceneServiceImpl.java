package lucene;

import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LuceneServiceImpl implements LuceneService {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private static final Path IDX_PATH = Paths.get("./idx");

    @Override
    public boolean write(File file) {
        try {
            String text = extractTextOfDocument(file);
            Document document = new Document();
            document.add(new StringField(LuceneService.TITLE_FIELD, file.getName(), Field.Store.YES));
            document.add(new TextField(LuceneService.BODY_FIELD, text, Field.Store.NO));
            try (IndexWriter iw = new IndexWriter(FSDirectory.open(getIndexDir()), new IndexWriterConfig(new RussianAnalyzer()))) {
                iw.addDocument(document);
            }
            return true;
        } catch (Exception e) {
            logger.error("Failed to create index for file: [{}]", file.getName(), e);
        }
        return false;
    }

    @Override
    public List<String> search(Query query) {
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(getIndexDir())));
            TopDocs found = searcher.search(query, 100);
            List<String> result = new ArrayList<>();
            for (ScoreDoc doc : found.scoreDocs) {
                Document foundDoc = searcher.doc(doc.doc);
                result.add(foundDoc.getField(LuceneService.TITLE_FIELD).stringValue());
            }
            return result;
        } catch (IOException e) {
            logger.error("Failed to execute query: [{}]", query, e);
        }
        return Collections.emptyList();
    }

    @Override
    public Path getIndexDir() {
        return IDX_PATH;
    }

    private String extractTextOfDocument(File file) throws IOException, SAXException, TikaException {
        Parser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        try (InputStream fileStream = new FileInputStream(file)) {
            parser.parse(fileStream, handler, metadata, new ParseContext());
            return handler.toString().trim();
        }
    }
}
