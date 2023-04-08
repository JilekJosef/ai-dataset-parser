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
        String var10003 = descriptor.getName();
        int var10005 = descriptor.getName().length();
        this.media = new File("D:\\Users\\jilek\\Documents\\Dataset\\" + var10003.substring(0, var10005 - 4));
        List<String> description = Files.readAllLines(descriptor.toPath());
        this.score = Integer.parseInt((String)description.get(2));
        this.rating = (String)description.get(1);
        this.tags = ((String)description.get(3)).split("\\*");
        this.id = this.media.getName().split("\\.")[0];
    }

    public boolean meetsRequirements(int minScore, String[] includeTags, String[] excludeTags, String[] includeRating, String[] extensions) {
        if (minScore > this.score) {
            return false;
        } else if (!Arrays.asList(includeRating).contains(this.rating)) {
            return false;
        } else {
            int includeTagsMatched = 0;
            String[] var7 = includeTags;
            int var8 = includeTags.length;

            int var9;
            String excludeTag;
            String[] var11;
            int var12;
            int var13;
            String tag;
            for(var9 = 0; var9 < var8; ++var9) {
                excludeTag = var7[var9];
                var11 = this.tags;
                var12 = var11.length;

                for(var13 = 0; var13 < var12; ++var13) {
                    tag = var11[var13];
                    if (excludeTag.equals(tag)) {
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
            } else {
                var7 = excludeTags;
                var8 = excludeTags.length;

                for(var9 = 0; var9 < var8; ++var9) {
                    excludeTag = var7[var9];
                    var11 = this.tags;
                    var12 = var11.length;

                    for(var13 = 0; var13 < var12; ++var13) {
                        tag = var11[var13];
                        if (excludeTag.equals(tag)) {
                            return false;
                        }
                    }
                }

                if (!Arrays.asList(extensions).contains(this.media.getName().split("\\.")[1])) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public void createTagFile(String addDefaultTag) throws IOException {
        String tagsOut = String.join(", ", this.tags);
        File output = new File("D:\\Users\\jilek\\Documents\\working\\" + this.id + ".txt");
        output.createNewFile();
        Files.writeString(output.toPath(), addDefaultTag + ", " + tagsOut);
    }

    public void createLink() throws IOException, ShellLinkException {
        ShellLink sl = new ShellLink();
        Path targetPath = Paths.get(this.media.toURI()).toAbsolutePath();
        String root = targetPath.getRoot().toString();
        String path = targetPath.subpath(0, targetPath.getNameCount()).toString();
        (new ShellLinkHelper(sl)).setLocalTarget(root, path, Options.ForceTypeFile).saveTo("D:\\Users\\jilek\\Documents\\working\\" + this.media.getName() + ".lnk");
    }
}
