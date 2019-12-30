package hansson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlFile {
    private List<Shareholding> myList = new ArrayList<Shareholding>();
    private final String itemsSelector = ".allocation-item";
    private final String nameSelector = ".u-ellipsis";
    private final String amountSelector = ".percent";

    public HtmlFile(String filePath) {
        File input = new File(filePath);

        try {
            Document doc = Jsoup.parse(input, "UTF-8", "");
            Elements items = doc.select(itemsSelector);

            for (Element item : items) {
                String name = item.select(nameSelector).first().text().trim();
                double amount = Utils.convertToDouble(item.select(amountSelector).first().text().trim());

                Shareholding share = new Shareholding.Builder(name).withAmount(amount).build();
                // System.out.println(share.getLabel());

                this.myList.add(share);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<Shareholding> getShareholdings() {
        return this.myList;
    }
}