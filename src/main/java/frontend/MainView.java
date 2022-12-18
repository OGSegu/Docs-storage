package main.java.frontend;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import main.java.lucene.LuceneService;
import main.java.service.PersistenceService;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends HorizontalLayout {

    private final PersistenceService persistenceService;
    private final LuceneService luceneService;

    @Autowired
    public MainView(PersistenceService persistenceService, LuceneService luceneService) {
        this.persistenceService = persistenceService;
        this.luceneService = luceneService;


        createUploadComponent();

        Grid<String> grid = createGrid();
        TextField textField = createTextField();
        Button search = new Button("Search", e -> {
            List<String> result = search(textField.getValue());
            grid.setItems(result);
        });
        add(search);

    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setPlaceholder("Search");
        textField.setPrefixComponent(VaadinIcon.SEARCH.create());
        add(textField);
        return textField;
    }

    private void createUploadComponent() {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload uploadComponent = new Upload(memoryBuffer);
        uploadComponent.addSucceededListener(e ->
                saveFile(memoryBuffer.getFileName(), memoryBuffer.getInputStream()));
        add(uploadComponent);
    }

    private Grid<String> createGrid() {
        Grid<String> grid = new Grid<>(String.class, false);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(s -> s).setHeader("File name")
                .setAutoWidth(true);
        add(grid);
        return grid;
    }

    private void saveFile(String fileName, InputStream fileContent) {
        try {
            File storedFile = persistenceService.store(fileName, fileContent);
            luceneService.write(storedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> search(String text) {
        QueryBuilder queryBuilder = new QueryBuilder(new RussianAnalyzer());
        Query phraseQuery = queryBuilder.createPhraseQuery(LuceneService.BODY_FIELD, text);
        return luceneService.search(phraseQuery);
    }

}
