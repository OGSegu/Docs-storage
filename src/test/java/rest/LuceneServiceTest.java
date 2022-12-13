package rest;

import lucene.LuceneServiceImpl;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LuceneServiceTest {

    @TempDir(cleanup = CleanupMode.ALWAYS)
    Path IDX_DIR;

    @Spy
    LuceneServiceImpl luceneService;

    @BeforeEach
    public void setUp() {
        when(luceneService.getIndexDir()).thenReturn(IDX_DIR);
    }

    @Test
    void test() throws Exception {
        File temp1 = File.createTempFile("temp1", null);
        BufferedWriter writer = new BufferedWriter(new FileWriter(temp1));
        writer.write("test words");
        writer.close();
        luceneService.write(temp1);

        QueryBuilder queryBuilder = new QueryBuilder(new RussianAnalyzer());
        Query phraseQuery = queryBuilder.createPhraseQuery("body", "test");
        List<String> search = luceneService.search(phraseQuery);

        assertEquals(search.get(0), temp1.getName());
    }

    @Test
    void test2() throws IOException {
        when(luceneService.getIndexDir()).thenReturn(Paths.get("./idx"));
        File temp1 = new File("temp1");
        BufferedWriter writer = new BufferedWriter(new FileWriter(temp1));
        writer.write("test words");
        writer.close();
        luceneService.write(temp1);
    }
}