import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
    public static final String rootPath = "D:\\Users\\jilek\\Documents\\Hand";
    public static final String outputPath = "D:\\Users\\jilek\\Documents\\HandFilter";

    public static void main(String[] args) throws IOException {
        File[] files = (new File(rootPath)).listFiles();
        LinkedList<DatasetSubject> dataset = new LinkedList<>();

        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                dataset.add(new DatasetSubject(file));
            }
        }

        String addDefaultTag = "";
        String[] extensions = new String[]{"png", "jpg", "jpeg", "webp"};
        int minScore = 50;
        String[] includeTags = {"loli"};
        String[] excludeTags = {"ai-generated"};
        String[] includeRating = new String[]{"safe", "explicit", "questionable", "sensitive"}; //"safe", "explicit", "questionable", "sensitive"

        for(DatasetSubject datasetSubject : dataset){
            if (datasetSubject.meetsRequirements(minScore, includeTags, excludeTags, includeRating, extensions)) {
                datasetSubject.createTagFile(addDefaultTag);
                datasetSubject.copy();
            }
        }
    }
}