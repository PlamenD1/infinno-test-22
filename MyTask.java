import org.apache.tools.ant.BuildException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MyTask extends org.apache.tools.ant.Task {
    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    String destDir;
    String repoBaseURL;

    List<Dependency> dependencies = new ArrayList<>();
    public void addConfiguredDependency(Dependency d) {
        dependencies.add(d);
    }

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    public void setRepoBaseURL(String repoBaseURL) {
        this.repoBaseURL = repoBaseURL;
    }

    public void execute() throws BuildException {
        for (Dependency d : dependencies) {
            String jarName = d.artifactId + "-" + d.version;
            String url = buildURL(d, jarName);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            try {
                client.send(request, HttpResponse.BodyHandlers.ofFile(Path.of(destDir + "/" + jarName + ".jar")));
            } catch (Exception e) {
                System.out.println("Package with url: " + url + " doesn't exist or could not be downloaded!");
                throw new RuntimeException(e);
            }
        }
    }

    String buildURL(Dependency d, String jarName) {
        StringBuilder url = new StringBuilder();

        String groupId = d.groupId.replaceAll("\\.", "/");
        if (!repoBaseURL.endsWith("/"))
            repoBaseURL += "/";

        url.append(repoBaseURL);
        url.append(groupId).append("/");
        url.append(d.artifactId).append("/");
        url.append(d.version).append("/");
        url.append(jarName);

        return url.toString();
    }
}
