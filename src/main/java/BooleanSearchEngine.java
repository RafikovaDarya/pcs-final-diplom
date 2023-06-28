import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> wordsPdfs = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        File[] PdfArr = new File("pdfs").listFiles();
        for (File pdf : PdfArr) {

            var doc = new PdfDocument(new PdfReader(pdf));
            for (int currentPage = 1; currentPage <= doc.getNumberOfPages(); currentPage++) {

                var page = doc.getPage(currentPage);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                for (String word : freqs.keySet()) {
                    PageEntry pageEntry = new PageEntry(pdf.getName(), currentPage, freqs.get(word));
                    if (wordsPdfs.containsKey(word)) {
                        wordsPdfs.get(word).add(pageEntry);
                    } else {
                        wordsPdfs.put(word, new ArrayList<>());
                        wordsPdfs.get(word).add(pageEntry);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        List<PageEntry> result = new ArrayList<>();

        if (wordsPdfs.get(word.toLowerCase()) != null) {
            for (PageEntry pageEntry : wordsPdfs.get(word.toLowerCase())) {
                result.add(pageEntry);
            }
        }
        Collections.sort(result);
        return result;
    }
}
