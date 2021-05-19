package org.testcontainers.containers;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class DockerComposeFiles {

    private List<ParsedDockerComposeFile> parsedComposeFiles;

    public DockerComposeFiles(List<File> composeFiles) {
        this.parsedComposeFiles = composeFiles.stream().map(ParsedDockerComposeFile::new).collect(Collectors.toList());
    }

    public Set<String> getDependencyImages() {

        Map<String, Set<String>> mergedServiceNameToImageNames = mergeServiceDependencyImageNames();

        return getImageNames(mergedServiceNameToImageNames);
    }

    private Map<String, Set<String>> mergeServiceDependencyImageNames() {
        Map<String, Set<String>> mergedServiceNameToImageNames = new HashMap();
        for (ParsedDockerComposeFile parsedComposeFile : parsedComposeFiles) {
            for (Entry<String, Set<String>> entry : parsedComposeFile.getServiceNameToImageNames().entrySet()) {
                mergedServiceNameToImageNames.put(entry.getKey(), entry.getValue());
            }
        }
        return mergedServiceNameToImageNames;
    }

    private Set<String> getImageNames(Map<String, Set<String>> serviceToImageNames) {
        Set<String> imageNames = new HashSet<>();
        serviceToImageNames.values().stream().forEach(imageNames::addAll);
        return imageNames;
    }

    /**
     * @return set of external volumes referenced in `volumes` section in all docker files.
     */
    public Set<String> getExternalVolumes() {
        return parsedComposeFiles.stream()
            .flatMap(f -> f.getExternalVolumes().stream()).collect(toSet());
    }
}
