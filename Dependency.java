public class Dependency {
    String groupId;
    String artifactId;
    String version;

    public Dependency() {}

    public void addConfiguredGroupId(GroupId obj) {
        this.groupId = obj.groupId;
    }

    public void addConfiguredArtifactId(ArtifactId obj) {
        this.artifactId = obj.artifactId;
    }

    public void addConfiguredVersion(Version obj) {
        this.version = obj.version;
    }
}
