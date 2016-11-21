package justyna.giphy;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyData;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Justyna Salacinska
 */
public class GiphyConnector {

    private static final String API_KEY = "dc6zaTOxFJmzC";

    private final Giphy giphy;

    public GiphyConnector() {
        giphy = new Giphy(API_KEY);
    }

    public Map<String, String> searchForUrls(String keyword, int numberOfResults) throws GiphyException {
        SearchFeed feed = giphy.search(keyword, numberOfResults, 0);

        Map<String, String> results = new HashMap<>();
        List<GiphyData> dataList = feed.getDataList();
        for (GiphyData giphyData : dataList) {
            String imgUrl = giphyData.getImages().getOriginal().getUrl();
            String id = giphyData.getId();
            results.put(id, imgUrl);
        }

        return results;
    }
}
