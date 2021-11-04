# scala_examples

## Build Structure

Source: https://stackoverflow.com/questions/6758258/running-a-maven-scala-project

        $ mvn archetype:generate -DarchetypeGroupId=net.alchim31.maven -DarchetypeArtifactId=scala-archetype-simple
            groupId: com.mycomp
            artifactId: 01_scala_basics_maven
            version: 1.0-SNAPSHOT

        Edit pom.xml:
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>1.1</version>
                <configuration>
                <mainClass>com.mycomp.App</mainClass>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.3.1</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.mycomp.App</mainClass>
                            <addClasspath>true</addClasspath>
                            <classpathLayoutType>custom</classpathLayoutType>
                            <customClasspathLayout>lib/$${artifact.artifactId}-$${artifact.version}$${dashClassifier?}.$${artifact.extension}
                            </customClasspathLayout>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>2.3</version>
                <configuration>
                    <outputDirectory>${project.build.directory}/lib</outputDirectory>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

        $ mvn package exec:java
        $ mvn package
        $ java -jar target/test-1.0-SNAPSHOT.jar