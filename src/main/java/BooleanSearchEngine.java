import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    public final Map<String, List<PageEntry>> wordsPdfs = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {

        File[] PdfArr = pdfsDir.listFiles();
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
                    List<PageEntry> sortedList;

                    if (wordsPdfs.containsKey(word)) {
                        sortedList = wordsPdfs.get(word);

                        if (!sortedList.contains(pageEntry)) {


                            wordsPdfs.remove(word, sortedList);

                            sortedList.add(pageEntry);
                            sortedList.sort(PageEntry::compareTo);


                            wordsPdfs.put(word, sortedList);
                        }
                    } else {
                        sortedList = new ArrayList<>();
                        sortedList.add(pageEntry);
                        wordsPdfs.put(word, sortedList);
                    }
                }


            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        if (wordsPdfs.containsKey(word)) {
            return wordsPdfs.get(word);
        }
        return Collections.emptyList();
    }
}
