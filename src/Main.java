import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import mslinks.ShellLinkException;

public class Main {
    public static final String rootPath = "D:\\Users\\jilek\\Documents\\Dataset";
    public static final String outputPath = "D:\\Users\\jilek\\Documents\\working";

    public static void main(String[] args) throws IOException, ShellLinkException {
        File[] files = (new File(rootPath)).listFiles();
        LinkedList<DatasetSubject> dataset = new LinkedList<>();

        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                dataset.add(new DatasetSubject(file));
            }
        }

        String addDefaultTag = "";
        String[] extensions = new String[]{"png", "jpg", "jpeg", "webp"};
        int minScore = 5;
        String[] includeTags = {"laffey (azur lane)"};
        String[] excludeTags = {"2girls", "1boy", "ai-generated"};
        String[] includeRating = new String[]{"safe"}; //"safe", "explicit", "questionable", "sensitive"

        for(DatasetSubject datasetSubject : dataset){
            if (datasetSubject.meetsRequirements(minScore, includeTags, excludeTags, includeRating, extensions)) {
                datasetSubject.createTagFile(addDefaultTag);
                datasetSubject.createLink(true);
            }
        }
    }
}