import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import mslinks.ShellLinkException;

public class Main {
    public static final String rootPath = "D:\\Users\\jilek\\Documents\\Dataset";
    public static final String outputPath = "D:\\Users\\jilek\\Documents\\working";

    public static void main(String[] args) throws IOException, ShellLinkException {
        File[] files = (new File("D:\\Users\\jilek\\Documents\\Dataset")).listFiles();
        LinkedList<DatasetSubject> dataset = new LinkedList<>();

        for(int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getName().endsWith(".txt")) {
                dataset.add(new DatasetSubject(file));
            }
        }

        String addDefaultTag = "loli";
        String[] extensions = new String[]{"png", "jpg", "jpeg", "webp"};
        int minScore = 5;
        String[] includeTags = new String[0];
        String[] excludeTags = new String[0];
        String[] includeRating = new String[]{"safe", "explicit", "questionable", "sensitive"};
        Iterator var9 = dataset.iterator();

        while(var9.hasNext()) {
            DatasetSubject datasetSubject = (DatasetSubject)var9.next();
            if (datasetSubject.meetsRequirements(minScore, includeTags, excludeTags, includeRating, extensions)) {
                datasetSubject.createTagFile(addDefaultTag);
                datasetSubject.createLink();
            }
        }

    }
}