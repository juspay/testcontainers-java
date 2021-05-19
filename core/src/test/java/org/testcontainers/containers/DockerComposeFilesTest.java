package org.testcontainers.containers;


import com.google.common.collect.Lists;
import java.io.File;

import com.sun.tools.javac.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerComposeFilesTest {
    @Test
    public void shouldGetDependencyImages() {
        DockerComposeFiles dockerComposeFiles = new DockerComposeFiles(Lists.newArrayList(new File("src/test/resources/docker-compose-imagename-parsing-v2.yml")));
        assertThat(dockerComposeFiles.getDependencyImages())
            .containsExactlyInAnyOrder("postgres", "redis", "mysql");
    }

    @Test
    public void shouldGetDependencyImagesWhenOverriding() {
        DockerComposeFiles dockerComposeFiles = new DockerComposeFiles(
            Lists.newArrayList(new File("src/test/resources/docker-compose-imagename-overriding-a.yml"),
                               new File("src/test/resources/docker-compose-imagename-overriding-b.yml")));
        assertThat(dockerComposeFiles.getDependencyImages())
            .containsExactlyInAnyOrder("alpine:3.2", "redis:b", "mysql:b", "aservice");
    }

    @Test
    public void shouldGetExternalVolumes() {
        DockerComposeFiles files = new DockerComposeFiles(
            List.of(
                new File("src/test/resources/docker-compose-volumenames-parsing.yml")
            )
        );
        assertThat(files.getExternalVolumes())
            .containsExactlyInAnyOrder("euler-api-txns-nix-store")
            .doesNotContain("not-an-external-volume");
    }
}
