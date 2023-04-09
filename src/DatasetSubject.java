import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import mslinks.ShellLink;
import mslinks.ShellLinkException;
import mslinks.ShellLinkHelper;
import mslinks.ShellLinkHelper.Options;

public class DatasetSubject {
    File media;
    File descriptor;
    int score;
    String rating;
    String[] tags;
    String id;

    public DatasetSubject(File descriptor) throws IOException {
        this.descriptor = descriptor;
        this.media = new File(Main.rootPath + "\\" + descriptor.getName().substring(0, descriptor.getName().length() - 4));

        List<String> description = Files.readAllLines(descriptor.toPath());

        this.score = Integer.parseInt(description.get(2));
        this.rating = description.get(1);
        this.tags = description.get(3).split("\\*");
        this.id = this.media.getName().split("\\.")[0];
    }

    public boolean meetsRequirements(int minScore, String[] includeTags, String[] excludeTags, String[] includeRating, String[] extensions) {
        if (minScore > this.score) {
            return false;
        }

        if (!Arrays.asList(includeRating).contains(this.rating)) {
            return false;
        }

        int includeTagsMatched = 0;
        for (String includeTag : includeTags) {
            for (String tag : tags) {
                if (includeTag.equals(tag)) {
                    ++includeTagsMatched;
                    break;
                }
            }
            if (includeTagsMatched == includeTags.length) {
                break;
            }
        }

        if (includeTagsMatched != includeTags.length) {
            return false;
        }

        for (String value : excludeTags) {
            for (String tag : tags) {
                if (value.equals(tag)) {
                    return false;
                }
            }
        }

        if (!Arrays.asList(extensions).contains(this.media.getName().split("\\.")[1])) {
            return false;
        }

        return true;
    }

    public void createTagFile(String addDefaultTag) throws IOException {
        String tagsOut = String.join(", ", this.tags);

        File output = new File(Main.outputPath + "\\" + this.id + ".txt");
        output.createNewFile();

        if(addDefaultTag.equals("")){
            Files.writeString(output.toPath(), tagsOut);
        }else{
            Files.writeString(output.toPath(), addDefaultTag + ", " + tagsOut);
        }
    }

    public void createLink(boolean copy) throws IOException, ShellLinkException {
        if(copy){
            Files.copy(media.toPath(), Path.of(Main.outputPath + "\\" + media.getName()));
        }else{
            ShellLink sl = new ShellLink();
            Path targetPath = Paths.get(this.media.toURI()).toAbsolutePath();
            String root = targetPath.getRoot().toString();
            String path = targetPath.subpath(0, targetPath.getNameCount()).toString();
            (new ShellLinkHelper(sl)).setLocalTarget(root, path, Options.ForceTypeFile).saveTo(Main.outputPath + "\\" + this.media.getName() + ".lnk");
        }
    }
}
